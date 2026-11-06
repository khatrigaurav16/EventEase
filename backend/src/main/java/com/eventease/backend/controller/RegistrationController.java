package com.eventease.backend.controller;

import com.eventease.backend.dto.RegistrationDTO;
import com.eventease.backend.dto.RegistrationRequestDTO;
import com.eventease.backend.entity.User;
import com.eventease.backend.service.RegistrationService;
import com.eventease.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    public RegistrationController(RegistrationService registrationService,
                                  UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userService.findUserEntity(userDetails.getUsername());
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<RegistrationDTO> registerForEvent(@RequestBody RegistrationRequestDTO request,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        RegistrationDTO registration = registrationService.registerForEvent(userId, request.getEventId());
        return ResponseEntity.ok(registration);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RegistrationDTO>> getMyRegistrations(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(registrationService.getMyRegistrations(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        registrationService.cancelRegistration(id, userId);
        return ResponseEntity.noContent().build();
    }
}
