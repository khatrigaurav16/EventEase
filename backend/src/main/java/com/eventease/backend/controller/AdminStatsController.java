package com.eventease.backend.controller;

import com.eventease.backend.dto.AdminStatsDTO;
import com.eventease.backend.service.AdminStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminStatsService.getStats());
    }
}
