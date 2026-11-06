package com.eventease.backend.service;

import com.eventease.backend.dto.RegistrationDTO;
import com.eventease.backend.entity.Event;
import com.eventease.backend.entity.Registration;
import com.eventease.backend.entity.User;
import com.eventease.backend.repository.EventRepository;
import com.eventease.backend.repository.RegistrationRepository;
import com.eventease.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("student");
        user.setRole("USER");

        event = new Event();
        event.setId(10L);
        event.setTitle("Tech Talk");
        event.setStatus("PUBLISHED");
        event.setEventDate(LocalDate.of(2026, 12, 1));
        event.setCapacity(50);
    }

    @Test
    void registerForEvent_shouldSucceedWhenAllConditionsMet() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventIdAndStatus(1L, 10L, "ACTIVE"))
                .thenReturn(false);
        when(registrationRepository.countByEventIdAndStatus(10L, "ACTIVE")).thenReturn(5L);

        Registration saved = new Registration();
        saved.setId(100L);
        saved.setUser(user);
        saved.setEvent(event);
        saved.setStatus("ACTIVE");
        saved.setRegistrationDate(LocalDateTime.now());
        when(registrationRepository.save(any(Registration.class))).thenReturn(saved);

        RegistrationDTO result = registrationService.registerForEvent(1L, 10L);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(10L, result.getEventId());
    }

    @Test
    void registerForEvent_shouldThrowForPastEvent() {
        Event pastEvent = new Event();
        pastEvent.setId(10L);
        pastEvent.setTitle("Old Event");
        pastEvent.setStatus("PUBLISHED");
        pastEvent.setEventDate(LocalDate.of(2020, 1, 1));
        pastEvent.setCapacity(50);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(pastEvent));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(1L, 10L)
        );
        assertTrue(ex.getMessage().contains("past event"));
    }

    @Test
    void registerForEvent_shouldThrowForCancelledEvent() {
        Event cancelledEvent = new Event();
        cancelledEvent.setId(10L);
        cancelledEvent.setTitle("Cancelled Event");
        cancelledEvent.setStatus("CANCELLED");
        cancelledEvent.setEventDate(LocalDate.of(2026, 12, 1));
        cancelledEvent.setCapacity(50);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(cancelledEvent));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(1L, 10L)
        );
        assertTrue(ex.getMessage().contains("cancelled or unpublished"));
    }

    @Test
    void registerForEvent_shouldThrowForDuplicateRegistration() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventIdAndStatus(1L, 10L, "ACTIVE"))
                .thenReturn(true);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(1L, 10L)
        );
        assertTrue(ex.getMessage().contains("already registered"));
    }

    @Test
    void registerForEvent_shouldThrowWhenEventFull() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventIdAndStatus(1L, 10L, "ACTIVE"))
                .thenReturn(false);
        when(registrationRepository.countByEventIdAndStatus(10L, "ACTIVE")).thenReturn(50L);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> registrationService.registerForEvent(1L, 10L)
        );
        assertTrue(ex.getMessage().contains("full"));
    }
}
