package com.eventease.backend.service;

import com.eventease.backend.dto.RegisterRequestDTO;
import com.eventease.backend.dto.UserDTO;
import com.eventease.backend.entity.User;
import com.eventease.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequestDTO("john_doe", "john@example.com", "securePassword123");
    }

    @Test
    void registerUser_shouldCreateUserWithHashedPassword() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("securePassword123")).thenReturn("hashedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john_doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("hashedPassword");
        savedUser.setRole("USER");
        savedUser.setCreatedAt(java.time.LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("USER", result.getRole());

        verify(passwordEncoder).encode("securePassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowWhenUsernameTaken() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );
        assertEquals("Username is already taken", ex.getMessage());
    }

    @Test
    void registerUser_shouldThrowWhenEmailTaken() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );
        assertEquals("Email is already registered", ex.getMessage());
    }
}
