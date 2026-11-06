package com.eventease.backend.service;

import com.eventease.backend.dto.AdminStatsDTO;
import com.eventease.backend.repository.EventRepository;
import com.eventease.backend.repository.RegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminStatsServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private AdminStatsService adminStatsService;

    @Test
    void getStats_shouldReturnCorrectCounts() {
        when(eventRepository.count()).thenReturn(3L);
        when(eventRepository.countByStatus("PUBLISHED")).thenReturn(1L);
        when(eventRepository.countByStatus("CANCELLED")).thenReturn(1L);
        when(eventRepository.countByStatus("COMPLETED")).thenReturn(1L);
        when(registrationRepository.count()).thenReturn(10L);
        when(registrationRepository.countByStatus("ACTIVE")).thenReturn(7L);

        AdminStatsDTO stats = adminStatsService.getStats();

        assertEquals(3L, stats.getTotalEvents());
        assertEquals(1L, stats.getPublishedEvents());
        assertEquals(1L, stats.getCancelledEvents());
        assertEquals(1L, stats.getCompletedEvents());
        assertEquals(10L, stats.getTotalRegistrations());
        assertEquals(7L, stats.getActiveRegistrations());
    }
}
