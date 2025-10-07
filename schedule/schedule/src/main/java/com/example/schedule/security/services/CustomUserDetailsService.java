package com.example.schedule.security.services;

import com.example.schedule.entity.User;
import com.example.schedule.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * CustomUserDetailsService is a service that implements the UserDetailsService interface.
 * It provides custom user details for Spring Security authentication.
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // Logger instance for logging purposes
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // User repository to interact with the data source
    private final UserRepository userRepository;

    /**
     * Loads user details by the given username or email.
     *
     * @param usernameOrEmail The username or email to look up the user.
     * @return UserDetails containing user information and granted authorities.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Find the user by username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist by username or email"));

        // Retrieve the user's roles and convert them to granted authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        // Create and return a CustomUserDetails instance using the user information
        return new CustomUserDetails.Builder()
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withAuthorities(authorities)
                .withAccountNonExpired(true)
                .withAccountNonLocked(true)
                .withCredentialsNonExpired(true)
                .withEnabled(true)
                .build();

        // Alternate return format (commented out) that uses the default User implementation:
        /*return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.getPassword(),
                authorities
        );*/
    }
}