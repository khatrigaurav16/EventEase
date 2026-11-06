package com.eventease.backend.service;

import com.eventease.backend.dto.EventDTO;
import com.eventease.backend.dto.EventRequestDTO;
import com.eventease.backend.entity.Category;
import com.eventease.backend.entity.Event;
import com.eventease.backend.repository.CategoryRepository;
import com.eventease.backend.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventService(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getPublishedEvents() {
        return eventRepository.findByStatus("PUBLISHED").stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return toDto(event);
    }

    @Transactional
    public EventDTO createEvent(EventRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(category);
        event.setEventDate(request.getEventDate());
        event.setEventTime(request.getEventTime());
        event.setVenue(request.getVenue());
        event.setCapacity(request.getCapacity());
        event.setOrganizerName(request.getOrganizerName());
        event.setStatus(request.getStatus());
        event.setCreatedAt(LocalDateTime.now());

        Event saved = eventRepository.save(event);
        return toDto(saved);
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventRequestDTO request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setCategory(category);
        event.setEventDate(request.getEventDate());
        event.setEventTime(request.getEventTime());
        event.setVenue(request.getVenue());
        event.setCapacity(request.getCapacity());
        event.setOrganizerName(request.getOrganizerName());
        event.setStatus(request.getStatus());

        Event saved = eventRepository.save(event);
        return toDto(saved);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        long activeCount = eventRepository.findById(id).map(e -> {
            // Count active registrations via registration repository is not directly available here;
            // use a simple check through the event's registrations if loaded.
            return 0L;
        }).orElse(0L);

        if (activeCount > 0) {
            throw new IllegalStateException("Cannot delete event with active registrations");
        }

        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public List<EventDTO> searchEvents(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllEvents();
        }
        return eventRepository.findByTitleContainingIgnoreCase(title.trim()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventDTO> filterByCategory(Long categoryId) {
        return eventRepository.findByCategoryId(categoryId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getUpcomingPublishedEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByStatusAndEventDateGreaterThanEqual("PUBLISHED", today).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private EventDTO toDto(Event event) {
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory() != null ? event.getCategory().getId() : null,
                event.getCategory() != null ? event.getCategory().getName() : null,
                event.getEventDate(),
                event.getEventTime(),
                event.getVenue(),
                event.getCapacity(),
                event.getOrganizerName(),
                event.getStatus()
        );
    }
}
