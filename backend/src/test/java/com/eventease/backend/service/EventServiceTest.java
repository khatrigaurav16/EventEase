package com.eventease.backend.service;

import com.eventease.backend.dto.EventRequestDTO;
import com.eventease.backend.dto.EventDTO;
import com.eventease.backend.entity.Category;
import com.eventease.backend.entity.Event;
import com.eventease.backend.repository.CategoryRepository;
import com.eventease.backend.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private EventService eventService;

    private Category category;
    private EventRequestDTO request;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Music");

        request = new EventRequestDTO();
        request.setTitle("Jazz Night");
        request.setDescription("Live jazz performance");
        request.setCategoryId(1L);
        request.setEventDate(LocalDate.of(2026, 11, 15));
        request.setEventTime(LocalTime.of(19, 0));
        request.setVenue("Main Hall");
        request.setCapacity(100);
        request.setOrganizerName("Music Club");
        request.setStatus("PUBLISHED");
    }

    @Test
    void createEvent_shouldSaveEventWithCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Event savedEvent = new Event();
        savedEvent.setId(10L);
        savedEvent.setTitle("Jazz Night");
        savedEvent.setCategory(category);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        EventDTO result = eventService.createEvent(request);

        assertNotNull(result);
        assertEquals("Jazz Night", result.getTitle());
        assertEquals(1L, result.getCategoryId());
        assertEquals("Music", result.getCategoryName());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_shouldThrowWhenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(request)
        );
        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void searchEvents_shouldReturnMatchingEvents() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Jazz Night");
        when(eventRepository.findByTitleContainingIgnoreCase("Jazz"))
                .thenReturn(List.of(event));

        List<EventDTO> results = eventService.searchEvents("Jazz");

        assertEquals(1, results.size());
        assertEquals("Jazz Night", results.get(0).getTitle());
    }
}
