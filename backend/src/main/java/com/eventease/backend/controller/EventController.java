package com.eventease.backend.controller;

import com.eventease.backend.dto.EventDTO;
import com.eventease.backend.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getPublishedEvents() {
        return ResponseEntity.ok(eventService.getPublishedEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> search(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(eventService.searchEvents(title));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<EventDTO>> filterByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(eventService.filterByCategory(categoryId));
    }
}
