package com.eventease.backend.controller;

import com.eventease.backend.dto.UserDashboardDTO;
import com.eventease.backend.entity.User;
import com.eventease.backend.service.RegistrationService;
import com.eventease.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/dashboard")
public class UserDashboardController {

    private final RegistrationService registrationService;
    private final UserService userService;

    public UserDashboardController(RegistrationService registrationService,
                                   UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDashboardDTO> getDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserEntity(userDetails.getUsername());
        return ResponseEntity.ok(registrationService.getUserDashboard(user.getId()));
    }
}
