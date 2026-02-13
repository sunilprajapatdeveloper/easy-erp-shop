package nextpos.app.nextpos.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import nextpos.app.nextpos.model.entity.Company;
import nextpos.app.nextpos.model.entity.Role;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.UserWarehouse;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.UserRole;
import nextpos.app.nextpos.repository.CompanyRepository;
import nextpos.app.nextpos.repository.RoleRepository;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.repository.UserWarehouseRepository;
import nextpos.app.nextpos.repository.WarehouseRepository;
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

        if (isBlank(request.getEmail()) && isBlank(request.getPhone()) && isBlank(request.getUsername())) {
            throw new RuntimeException("At least one of email, phone, or username must be provided.");
        }

        if (!isBlank(request.getEmail()) && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (!isBlank(request.getPhone()) && userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        if (!isBlank(request.getUsername()) && userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthenticated creation is not allowed.");
        }

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create user entity
        User user = new User();
        applyCreateRequest(user, request);

        String rawPassword = generateStrongPassword(12);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(Boolean.TRUE);
        user.setRole(role);
        user.setCompanyId(currentUser.getCompanyId());
        user.setCreatedBy(currentUser.getId());

        // Save user first to generate ID (needed for assignments)
        User savedUser = userRepository.save(user);

        // Assign warehouses efficiently and avoid duplicates
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

        // Determine default warehouse
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

        // Save final state
        savedUser = userRepository.save(savedUser);

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(savedUser.getId(), savedUser.getCompanyId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return UserResponse.fromEntity(savedUser, mediaResponse);
    }

    @Override
    @Transactional
    public UserResponse signup(final UserRegisterRequest request) {
        // Require at least one identifier (email or username)
        if (isBlank(request.getEmail()) && isBlank(request.getUsername())) {
            throw new RuntimeException("At least one of email or username must be provided.");
        }

        // Check uniqueness
        if (!isBlank(request.getEmail()) && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (!isBlank(request.getUsername()) && userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use");
        }
        if (!isBlank(request.getPhone()) && userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        // Create a new company for this user
        Company newCompany = Company.builder()
                .companyName("Company for " + nv(request.getFirstname()) + " " + nv(request.getLastname()))
                .phone(request.getPhone())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        Company savedCompany = companyRepository.save(newCompany);

        // Assign default role: COMPANY_OWNER
        Role defaultRole = roleRepository.findByName(UserRole.COMPANY_OWNER.name())
                .orElseThrow(() -> new RuntimeException("Default COMPANY_OWNER role not found"));

        // Build user entity
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Password encryption
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // System fields
        user.setStatus(Boolean.TRUE);
        user.setRole(defaultRole);
        user.setCompanyId(savedCompany.getId());
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(saved.getId(), saved.getCompanyId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return UserResponse.fromEntity(saved, mediaResponse);
    }

    @Override
    public UserResponse getUserById(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(userId, user.getCompanyId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return UserResponse.fromEntity(user, mediaResponse);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Unauthorized");
        }
        final String username = auth.getName();
        final User currentUser = userRepository.findByUsername(username)
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
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities(companyId, "USER", userIds);
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

        User currentUser = userRepository.findByUsername(authentication.getName())
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
        if (!isBlank(request.getUsername())) {
            userRepository.findByUsername(request.getUsername()).ifPresent(existing -> {
                if (!existing.getId().equals(userId)) {
                    throw new RuntimeException("Username already in use");
                }
            });
        }

        // Role update
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        // Apply other updates
        applyUpdateRequest(user, request);
        user.setUpdatedBy(currentUser.getId());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user first
        User savedUser = userRepository.save(user);

        // Update warehouses
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
            mediaResponse = getProfileImageFromMedia(savedUser.getId(), savedUser.getCompanyId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
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

        final String username = auth.getName();
        final User user = userRepository.findByUsername(username)
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

        // Delete user's media first
        try {
            mediaService.deleteMediaByEntity(companyId, "USER", userId, userId);
        } catch (Exception e) {
            log.error("Failed to delete media for user {}: {}", userId, e.getMessage());
            // Continue with user deletion even if media deletion fails
        }

        // Delete warehouse assignments
        userWarehouseRepository.deleteByUserId(userId);

        // Delete user
        userRepository.deleteById(userId);
    }

    @Override
    public JwtResponse authenticateAndGenerateToken(final LoginRequest loginRequest) {
        final String identifier = loginRequest.getIdentifier();
        final String password = loginRequest.getPassword();

        // Resolve identifier to username for authentication
        Optional<User> opt = userRepository.findByUsername(identifier);
        if (opt.isEmpty())
            opt = userRepository.findByEmail(identifier);
        if (opt.isEmpty())
            opt = userRepository.findByPhone(identifier);

        final User user = opt.orElseThrow(() -> new RuntimeException("Invalid login credentials"));
        final String usernameForAuth = user.getUsername();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameForAuth, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtUtils.generateToken(usernameForAuth);
        final Date expiration = jwtUtils.getExpirationFromToken(token);
        final long expiresIn = (expiration.getTime() - System.currentTimeMillis()) / 1000;

        // Get full media response
        MediaResponse mediaResponse = null;
        try {
            mediaResponse = getProfileImageFromMedia(user.getId(), user.getCompanyId());
        } catch (Exception e) {
            log.warn("Could not retrieve profile image for new user: {}", e.getMessage());
        }

        return new JwtResponse(token, expiresIn, UserResponse.fromEntity(user, mediaResponse));
    }

    // private void assignWarehouses(User user, Set<Long> warehouseIds, Long
    // createdBy) {
    // Set<Long> existingIds = user.getUserWarehouses().stream()
    // .map(uw -> uw.getWarehouse().getId())
    // .collect(Collectors.toSet());

    // List<UserWarehouse> newAssignments = warehouseIds.stream()
    // .filter(id -> !existingIds.contains(id))
    // .map(id -> {
    // Warehouse warehouse = warehouseRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Warehouse not found: " + id));
    // UserWarehouse uw = new UserWarehouse();
    // uw.setUser(user);
    // uw.setWarehouse(warehouse);
    // uw.setActive(true);
    // uw.setCreatedAt(LocalDateTime.now());
    // uw.setCreatedBy(createdBy);
    // return uw;
    // })
    // .toList();

    // newAssignments.forEach(user::addUserWarehouse);
    // }

    private void applyCreateRequest(final User user, final CreateUserRequest r) {
        // Identity
        if (!isBlank(r.getFirstname()))
            user.setFirstname(r.getFirstname());
        if (!isBlank(r.getMiddleName()))
            user.setMiddleName(r.getMiddleName());
        if (!isBlank(r.getLastname()))
            user.setLastname(r.getLastname());
        if (!isBlank(r.getUsername()))
            user.setUsername(r.getUsername());
        if (!isBlank(r.getEmail()))
            user.setEmail(r.getEmail());
        if (!isBlank(r.getPhone()))
            user.setPhone(r.getPhone());

        // Profile
        if (!isBlank(r.getProfileImageUrl()))
            user.setProfileImageUrl(r.getProfileImageUrl());

        // Contact / Locale
        if (!isBlank(r.getAddressLine1()))
            user.setAddressLine1(r.getAddressLine1());
        if (!isBlank(r.getAddressLine2()))
            user.setAddressLine2(r.getAddressLine2());
        if (!isBlank(r.getCity()))
            user.setCity(r.getCity());
        if (!isBlank(r.getState()))
            user.setState(r.getState());
        if (!isBlank(r.getCountry()))
            user.setCountry(r.getCountry());
        if (!isBlank(r.getPostalCode()))
            user.setPostalCode(r.getPostalCode());
        if (!isBlank(r.getTimezone()))
            user.setTimezone(r.getTimezone());
        if (!isBlank(r.getLanguage()))
            user.setLanguage(r.getLanguage());
        if (!isBlank(r.getGender()))
            user.setGender(r.getGender());

        // Org
        if (!isBlank(r.getDepartment()))
            user.setDepartment(r.getDepartment());
        if (!isBlank(r.getPositionTitle()))
            user.setPositionTitle(r.getPositionTitle());

        // Company
        if (r.getCompanyId() != null)
            user.setCompanyId(r.getCompanyId());

        // Security
        if (r.getMfaEnabled() != null)
            user.setMfaEnabled(r.getMfaEnabled());
    }

    private void applyUpdateRequest(final User user, final UpdateUserRequest r) {
        // Identity
        if (!isBlank(r.getFirstname()))
            user.setFirstname(r.getFirstname());
        if (!isBlank(r.getMiddleName()))
            user.setMiddleName(r.getMiddleName());
        if (!isBlank(r.getLastname()))
            user.setLastname(r.getLastname());
        if (!isBlank(r.getUsername()))
            user.setUsername(r.getUsername());
        if (!isBlank(r.getEmail()))
            user.setEmail(r.getEmail());
        if (!isBlank(r.getPhone()))
            user.setPhone(r.getPhone());

        // Profile
        if (!isBlank(r.getProfileImageUrl()))
            user.setProfileImageUrl(r.getProfileImageUrl());

        // Contact / Locale
        if (!isBlank(r.getAddressLine1()))
            user.setAddressLine1(r.getAddressLine1());
        if (!isBlank(r.getAddressLine2()))
            user.setAddressLine2(r.getAddressLine2());
        if (!isBlank(r.getCity()))
            user.setCity(r.getCity());
        if (!isBlank(r.getState()))
            user.setState(r.getState());
        if (!isBlank(r.getCountry()))
            user.setCountry(r.getCountry());
        if (!isBlank(r.getPostalCode()))
            user.setPostalCode(r.getPostalCode());
        if (!isBlank(r.getTimezone()))
            user.setTimezone(r.getTimezone());
        if (!isBlank(r.getLanguage()))
            user.setLanguage(r.getLanguage());
        if (!isBlank(r.getGender()))
            user.setGender(r.getGender());

        // Org
        if (!isBlank(r.getDepartment()))
            user.setDepartment(r.getDepartment());
        if (!isBlank(r.getPositionTitle()))
            user.setPositionTitle(r.getPositionTitle());

        // Company
        if (r.getCompanyId() != null)
            user.setCompanyId(r.getCompanyId());

        // Security
        if (r.getMfaEnabled() != null)
            user.setMfaEnabled(r.getMfaEnabled());
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

    private MediaResponse getProfileImageFromMedia(Long userId, Long companyId) {
        // Get the map from the service
        Map<Long, List<MediaResponse>> mediaMap = mediaService.getMediaForEntities(
                companyId,
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
