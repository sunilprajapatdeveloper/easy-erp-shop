package nextpos.app.nextpos.service.impl;

import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        String roleName = (user.getRole() != null) ? user.getRole().getName() : "GUEST";

        return org.springframework.security.core.userdetails.User.builder()
                .username(identifier) // Use the login identifier as the principal
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }
}