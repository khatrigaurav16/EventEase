package com.eventease.backend.service;

import com.eventease.backend.dto.AdminStatsDTO;
import com.eventease.backend.repository.EventRepository;
import com.eventease.backend.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminStatsService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public AdminStatsService(EventRepository eventRepository,
                             RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional(readOnly = true)
    public AdminStatsDTO getStats() {
        long totalEvents = eventRepository.count();
        long published = eventRepository.countByStatus("PUBLISHED");
        long cancelled = eventRepository.countByStatus("CANCELLED");
        long completed = eventRepository.countByStatus("COMPLETED");

        long totalRegistrations = registrationRepository.count();
        long activeRegistrations = registrationRepository.countByStatus("ACTIVE");

        return new AdminStatsDTO(
                totalEvents, totalRegistrations, published, cancelled, completed, activeRegistrations
        );
    }
}
