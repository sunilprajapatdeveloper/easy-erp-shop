package nextpos.app.nextpos.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateUserRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.UserRegisterRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateUserRequest;
import nextpos.app.nextpos.model.dto.request.EmailRequest;
import nextpos.app.nextpos.model.dto.request.LoginRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePasswordRequest;
import nextpos.app.nextpos.model.dto.response.JwtResponse;
import nextpos.app.nextpos.model.dto.response.MediaResponse;
import nextpos.app.nextpos.model.dto.response.UserResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.UserRole;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.jwt.JwtUtils;
import nextpos.app.nextpos.service.email.MailService;
import nextpos.app.nextpos.service.interf.MediaService;
import nextpos.app.nextpos.service.interf.UserService;
import nextpos.app.nextpos.service.queue.EmailQueuePublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserWarehouseRepository userWarehouseRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final MailService mailService;
    private final EmailQueuePublisher emailQueuePublisher;
    private final MediaService mediaService;

    @Override
    @Transactional
    public UserResponse createUser(final CreateUserRequest request) {

        if (isBlank(request.getEmail()) || isBlank(request.getPhone())) {
            throw new RuntimeException("Email and phone are required.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthenticated creation is not allowed.");
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create core User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setMfaEnabled(request.getMfaEnabled() != null ? request.getMfaEnabled() : false);

        String rawPassword = generateStrongPassword(12);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(Boolean.TRUE);
        user.setRole(role);
        user.setCompanyId(currentUser.getCompanyId());
        user.setCreatedBy(currentUser.getId());

        User savedUser = userRepository.save(user);

        // Create UserProfile
        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .profileImageUrl(request.getProfileImageUrl())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .timezone(request.getTimezone())
                .language(request.getLanguage())
                .gender(request.getGender())
                .department(request.getDepartment())
                .positionTitle(request.getPositionTitle())
                .build();
        userProfileRepository.save(profile);
        savedUser.setProfile(profile);

        if (request.getWarehouseIds() != null && !request.getWarehouseIds().isEmpty()) {
            Set<UserWarehouse> assignments = new HashSet<>();
            for (Long whId : request.getWarehouseIds()) {
                Warehouse wh = warehouseRepository.findById(whId)
                        .orElseThrow(() -> new RuntimeException("Warehouse not found: " + whId));

                // Avoid duplicate assignment
                if (savedUser.getUserWarehouses().stream().noneMatch(uw -> uw.getWarehouse().getId().equals(whId))) {
                    UserWarehouse uw = new UserWarehouse();
                    uw.setUser(savedUser);
                    uw.setWarehouse(wh);
                    uw.setActive(true);
                    uw.setCreatedAt(LocalDateTime.now());
                    uw.setCreatedBy(currentUser.getId());
                    assignments.add(uw);
                }
            }
            assignments.forEach(savedUser::addUserWarehouse);
        }

        // Default warehouse
        Warehouse defaultWarehouse = null;

        if (request.getDefaultWarehouseId() != null) {
            Warehouse requestedWh = warehouseRepository.findById(request.getDefaultWarehouseId())
                    .orElseThrow(() -> new RuntimeException(
                            "Default warehouse not found: " + request.getDefaultWarehouseId()));

            // Ensure default is among assigned warehouses
            boolean validDefault = savedUser.getUserWarehouses().stream()
                    .anyMatch(uw -> uw.getWarehouse().getId().equals(requestedWh.getId()));

            if (validDefault) {
                defaultWarehouse = requestedWh;
            }
        }

        // Fallback: first assigned warehouse if default not provided or invalid
        if (defaultWarehouse == null && !savedUser.getUserWarehouses().isEmpty()) {
            defaultWarehouse = savedUser.getUserWarehouses().iterator().next().getWarehouse();
        }

        savedUser.setDefaultWarehouse(defaultWarehouse);

        // Save user again with warehouse assignments & default warehouse
        savedUser = userRepository.save(savedUser);

        // Send password email asynchronously
        if (!isBlank(request.getEmail())) {
            String companyName = "EasyErpShop";
            if (currentUser.getCompanyId() != null) {
                Company company = companyRepository.findById(currentUser.getCompanyId()).orElse(null);
                if (company != null && StringUtils.hasText(company.getCompanyName())) {
                    companyName = company.getCompanyName();
                }
            }

            String content = mailService.buildPasswordEmail(rawPassword, companyName);

            EmailRequest emailRequest = EmailRequest.builder()
                    .companyId(null)
                    .to(List.of(request.getEmail()))
                    .subject("Your Account Password")
                    .content(content)
                    .isHtml(true)
                    .build();

            emailQueuePublisher.publishEmail(emailRequest);
        }

        // Get profile image from media service
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(savedUser.getId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return UserResponse.fromEntity(savedUser, mediaResponse);
    }

    @Override
    @Transactional
    public UserResponse signup(final UserRegisterRequest request, final Long companyId) {
        // Validate required fields
        if (isBlank(request.getEmail()) || isBlank(request.getPhone())) {
            throw new RuntimeException("Email and phone are required.");
        }

        // Check uniqueness for user
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        // Validate that the company exists
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        // Assign default role: COMPANY_OWNER
        Role defaultRole = roleRepository.findByName(UserRole.COMPANY_OWNER.name())
                .orElseThrow(() -> new RuntimeException("Default COMPANY_OWNER role not found"));

        // Generate random password
        String rawPassword = generateStrongPassword(12);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encodedPassword)
                .status(true)
                .role(defaultRole)
                .companyId(companyId)
                .build();

        User savedUser = userRepository.save(user);

        // Create user profile
        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();

        userProfileRepository.save(profile);
        savedUser.setProfile(profile);

        // Send password email
        String content = mailService.buildPasswordEmail(rawPassword, company.getCompanyName());
        EmailRequest emailRequest = EmailRequest.builder()
                .companyId(companyId)
                .to(List.of(request.getEmail()))
                .subject("Your Account Password")
                .content(content)
                .isHtml(true)
                .build();

        emailQueuePublisher.publishEmail(emailRequest);

        // Get profile image (if any)
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(savedUser.getId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return UserResponse.fromEntity(savedUser, mediaResponse);
    }

    @Override
    public UserResponse getUserById(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure profile is loaded (optional, but response needs it)
        if (user.getProfile() == null) {
            log.warn("User {} has no profile", userId);
        }
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(userId);
        } catch (Exception e) {
            log.warn("Could not retrieve profile image: {}", e.getMessage());
        }

        return UserResponse.fromEntity(user, mediaResponse);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Unauthorized");
        }
        final String email = auth.getName();
        final User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        // Include all associated users + self
        final List<User> users = userRepository.findAllAssociatedUsers(currentUser.getId());
        if (users.stream().noneMatch(u -> u.getId().equals(currentUser.getId()))) {
            users.add(currentUser);
        }

        // Get all user IDs
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        Long companyId = currentUser.getCompanyId();

        // Get profile images for all users in batch
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities("USER", userIds);
        if (mediaMap == null) {
            mediaMap = Collections.emptyMap();
        }

        final Map<Long, List<MediaResponse>> finalMediaMap = mediaMap;

        return users.stream()
                .map(user -> {
                    List<MediaResponse> userMedia = finalMediaMap.get(user.getId());
                    MediaResponse profileImage = (userMedia != null && !userMedia.isEmpty())
                            ? userMedia.get(0)
                            : null;

                    // This now works because UserResponse.fromEntity handles null profileImage
                    return UserResponse.fromEntity(user, profileImage);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(final Long userId, final UpdateUserRequest request) {
        // Authenticate
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthorized");
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Load target user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Uniqueness checks
        if (!isBlank(request.getEmail())) {
            userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(userId)) {
                    throw new RuntimeException("Email already in use");
                }
            });
        }
        if (!isBlank(request.getPhone())) {
            userRepository.findByPhone(request.getPhone()).ifPresent(existing -> {
                if (!existing.getId().equals(userId)) {
                    throw new RuntimeException("Phone number already in use");
                }
            });
        }

        // Role update
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        // Update core User fields
        if (!isBlank(request.getEmail()))
            user.setEmail(request.getEmail());
        if (!isBlank(request.getPhone()))
            user.setPhone(request.getPhone());
        if (request.getMfaEnabled() != null)
            user.setMfaEnabled(request.getMfaEnabled());

        user.setUpdatedBy(currentUser.getId());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user first
        User savedUser = userRepository.save(user);

        // Update UserProfile (create if not exists)
        UserProfile profile = savedUser.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(savedUser);
        }
        // apply profile fields from request
        if (!isBlank(request.getFirstname()))
            profile.setFirstname(request.getFirstname());
        if (!isBlank(request.getLastname()))
            profile.setLastname(request.getLastname());
        if (!isBlank(request.getProfileImageUrl()))
            profile.setProfileImageUrl(request.getProfileImageUrl());
        if (!isBlank(request.getAddressLine1()))
            profile.setAddressLine1(request.getAddressLine1());
        if (!isBlank(request.getAddressLine2()))
            profile.setAddressLine2(request.getAddressLine2());
        if (!isBlank(request.getCity()))
            profile.setCity(request.getCity());
        if (!isBlank(request.getState()))
            profile.setState(request.getState());
        if (!isBlank(request.getCountry()))
            profile.setCountry(request.getCountry());
        if (!isBlank(request.getPostalCode()))
            profile.setPostalCode(request.getPostalCode());
        if (!isBlank(request.getTimezone()))
            profile.setTimezone(request.getTimezone());
        if (!isBlank(request.getLanguage()))
            profile.setLanguage(request.getLanguage());
        if (!isBlank(request.getGender()))
            profile.setGender(request.getGender());
        if (!isBlank(request.getDepartment()))
            profile.setDepartment(request.getDepartment());
        if (!isBlank(request.getPositionTitle()))
            profile.setPositionTitle(request.getPositionTitle());

        userProfileRepository.save(profile);
        savedUser.setProfile(profile); // ensure in-memory consistency

        // Warehouse handling
        if (request.getWarehouseIds() != null) {
            // Remove warehouses not in the new list (safe delete)
            Set<Long> newWarehouseIds = new HashSet<>(request.getWarehouseIds());
            savedUser.getUserWarehouses().removeIf(uw -> !newWarehouseIds.contains(uw.getWarehouse().getId()));

            // Assign new warehouses safely
            for (Long whId : newWarehouseIds) {
                if (savedUser.getUserWarehouses().stream().noneMatch(uw -> uw.getWarehouse().getId().equals(whId))) {
                    Warehouse wh = warehouseRepository.findById(whId)
                            .orElseThrow(() -> new RuntimeException("Warehouse not found: " + whId));
                    UserWarehouse uw = new UserWarehouse();
                    uw.setUser(savedUser);
                    uw.setWarehouse(wh);
                    uw.setActive(true);
                    uw.setCreatedAt(LocalDateTime.now());
                    uw.setCreatedBy(currentUser.getId());
                    savedUser.addUserWarehouse(uw);
                }
            }
        }

        // Default warehouse handling
        Warehouse defaultWarehouse = null;

        if (request.getDefaultWarehouseId() != null) {
            Warehouse requestedWh = warehouseRepository.findById(request.getDefaultWarehouseId())
                    .orElseThrow(() -> new RuntimeException(
                            "Default warehouse not found: " + request.getDefaultWarehouseId()));

            boolean validDefault = savedUser.getUserWarehouses().stream()
                    .anyMatch(uw -> uw.getWarehouse().getId().equals(requestedWh.getId()));

            if (validDefault) {
                defaultWarehouse = requestedWh;
            }
        }

        // Fallback to first assigned warehouse if default invalid or not provided
        if (defaultWarehouse == null && !savedUser.getUserWarehouses().isEmpty()) {
            defaultWarehouse = savedUser.getUserWarehouses().iterator().next().getWarehouse();
        }

        savedUser.setDefaultWarehouse(defaultWarehouse);

        // Save final state
        savedUser = userRepository.save(savedUser);

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(savedUser.getId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image: {}", e.getMessage());
        }

        return UserResponse.fromEntity(savedUser, mediaResponse);
    }

    @Override
    @Transactional
    public void updatePassword(final UpdatePasswordRequest request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("User is not authenticated");
        }

        final String email = auth.getName();
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(user.getId());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(final Long userId) {
        // Get user to get companyId before deleting
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long companyId = user.getCompanyId();

        // Delete user's media
        try {
            mediaService.deleteMediaByEntity("USER", userId);
        } catch (Exception e) {
            log.error("Failed to delete media for user {}: {}", userId, e.getMessage());
        }

        // Delete warehouse assignments
        userWarehouseRepository.deleteByUserId(userId);

        // The profile will be deleted automatically due to cascade = ALL, orphanRemoval
        // = true
        userRepository.deleteById(userId);
    }

    @Override
    public JwtResponse authenticateAndGenerateToken(final LoginRequest loginRequest) {
        final String identifier = loginRequest.getIdentifier();
        final String password = loginRequest.getPassword();

        // Resolve identifier to email or phone
        Optional<User> opt = userRepository.findByEmail(identifier);
        if (opt.isEmpty())
            opt = userRepository.findByPhone(identifier);

        final User user = opt.orElseThrow(() -> new RuntimeException("Invalid login credentials"));
        final String emailForAuth = user.getEmail();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailForAuth, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtUtils.generateToken(emailForAuth);
        final Date expiration = jwtUtils.getExpirationFromToken(token);
        final long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / 1000;

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(user.getId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image: {}", e.getMessage());
        }

        return new JwtResponse(token, expiresIn, UserResponse.fromEntity(user, mediaResponse));
    }

    private String generateStrongPassword(final int length) {
        final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lower = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final String symbols = "!@#$%&*";
        final String all = upper + lower + digits + symbols;

        final SecureRandom random = new SecureRandom();
        final StringBuilder password = new StringBuilder();

        // Ensure at least one character from each group
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));

        for (int i = 4; i < length; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }

        final List<Character> chars = password.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(chars, random);
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private boolean isBlank(final String s) {
        return s == null || s.trim().isEmpty();
    }

    private String nv(final String s) {
        return s == null ? "" : s;
    }

    private MediaResponse getProfileImageFromMedia(Long userId) {
        // Get the map from the service
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities(
                "USER",
                Collections.singletonList(userId));

        // Check if the map itself is null (Defensive programming)
        if (mediaMap == null) {
            return null;
        }

        // Get the list for this specific user safely
        List<MediaResponse> mediaList = mediaMap.get(userId);

        // Return the latest media if it exists, otherwise return null
        if (mediaList != null && !mediaList.isEmpty()) {
            // Returns the last uploaded image (usually the most recent profile pic)
            return mediaList.get(mediaList.size() - 1);
        }

        return null;
    }
}
