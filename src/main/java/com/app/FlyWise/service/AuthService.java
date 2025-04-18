package com.app.FlyWise.service;

import com.app.FlyWise.model.Role;
import com.app.FlyWise.model.User;
import com.app.FlyWise.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Builder
    public User register(String username, String email, String password) {
        var user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password)) // Hash the password
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
