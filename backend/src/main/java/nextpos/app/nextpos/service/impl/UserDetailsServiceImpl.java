package nextpos.app.nextpos.service.impl;

import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
            user.getRole().getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getName().name())));
        }

        Set<Long> warehouseIds = user.getUserWarehouses().stream()
                .filter(mapping -> Boolean.TRUE.equals(mapping.getActive()))
                .filter(mapping -> mapping.getWarehouse() != null
                        && user.getCompanyId().equals(mapping.getWarehouse().getCompanyId()))
                .map(mapping -> mapping.getWarehouse().getId())
                .collect(Collectors.toUnmodifiableSet());

        Long defaultWarehouseId = user.getDefaultWarehouse() != null
                && user.getCompanyId().equals(user.getDefaultWarehouse().getCompanyId())
                ? user.getDefaultWarehouse().getId()
                : null;

        return new AuthenticatedUser(
                user.getId(),
                user.getCompanyId(),
                defaultWarehouseId,
                identifier,
                user.getPassword(),
                Boolean.TRUE.equals(user.getStatus()),
                warehouseIds,
                Set.copyOf(authorities));
    }
}
