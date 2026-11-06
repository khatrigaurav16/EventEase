package com.eventease.backend.service;

import com.eventease.backend.dto.RegistrationDTO;
import com.eventease.backend.dto.UserDashboardDTO;
import com.eventease.backend.entity.Event;
import com.eventease.backend.entity.Registration;
import com.eventease.backend.entity.User;
import com.eventease.backend.repository.EventRepository;
import com.eventease.backend.repository.RegistrationRepository;
import com.eventease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private static final String ACTIVE = "ACTIVE";
    private static final String CANCELLED = "CANCELLED";
    private static final String PUBLISHED = "PUBLISHED";

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               EventRepository eventRepository,
                               UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegistrationDTO registerForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Pessimistic lock on the event to prevent concurrent over-booking
        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (!PUBLISHED.equals(event.getStatus())) {
            throw new IllegalStateException("Cannot register for a cancelled or unpublished event");
        }

        LocalDate today = LocalDate.now();
        if (event.getEventDate().isBefore(today)) {
            throw new IllegalStateException("Cannot register for a past event");
        }

        if (registrationRepository.existsByUserIdAndEventIdAndStatus(userId, eventId, ACTIVE)) {
            throw new IllegalStateException("You are already registered for this event");
        }

        long activeCount = registrationRepository.countByEventIdAndStatus(eventId, ACTIVE);
        if (activeCount >= event.getCapacity()) {
            throw new IllegalStateException("Event is full");
        }

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus(ACTIVE);

        Registration saved = registrationRepository.save(registration);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RegistrationDTO> getMyRegistrations(Long userId) {
        return registrationRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelRegistration(Long registrationId, Long userId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

        if (!registration.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You can only cancel your own registration");
        }

        if (!ACTIVE.equals(registration.getStatus())) {
            throw new IllegalStateException("Registration is already cancelled");
        }

        registration.setStatus(CANCELLED);
        registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public UserDashboardDTO getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Registration> all = registrationRepository.findByUserAndStatus(user, ACTIVE);
        LocalDate today = LocalDate.now();

        List<RegistrationDTO> upcoming = all.stream()
                .filter(r -> r.getEvent().getEventDate().isAfter(today)
                        || r.getEvent().getEventDate().isEqual(today))
                .map(this::toDto)
                .collect(Collectors.toList());

        List<RegistrationDTO> history = registrationRepository.findByUserId(userId).stream()
                .filter(r -> !ACTIVE.equals(r.getStatus())
                        || r.getEvent().getEventDate().isBefore(today))
                .map(this::toDto)
                .collect(Collectors.toList());

        return new UserDashboardDTO(upcoming, history);
    }

    private RegistrationDTO toDto(Registration registration) {
        return new RegistrationDTO(
                registration.getId(),
                registration.getUser().getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getRegistrationDate(),
                registration.getStatus()
        );
    }
}
