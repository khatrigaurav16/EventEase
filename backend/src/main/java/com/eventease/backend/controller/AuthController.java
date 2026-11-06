package com.eventease.backend.controller;

import com.eventease.backend.dto.AuthRequestDTO;
import com.eventease.backend.dto.AuthResponseDTO;
import com.eventease.backend.dto.RegisterRequestDTO;
import com.eventease.backend.dto.UserDTO;
import com.eventease.backend.config.JwtUtil;
import com.eventease.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserDTO created = userService.registerUser(request);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        com.eventease.backend.entity.User user = userService.findUserEntity(request.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), user.getRole()));
    }
}
