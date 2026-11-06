package com.eventease.backend.service;

import com.eventease.backend.dto.RegisterRequestDTO;
import com.eventease.backend.dto.UserDTO;
import com.eventease.backend.entity.User;
import com.eventease.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(java.time.LocalDateTime.now());

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toDto(user);
    }

    public User findUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
