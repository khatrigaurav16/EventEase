package com.eventease.backend.controller;

import com.eventease.backend.dto.EventDTO;
import com.eventease.backend.dto.EventRequestDTO;
import com.eventease.backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventRequestDTO request) {
        EventDTO created = eventService.createEvent(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id,
                                                @Valid @RequestBody EventRequestDTO request) {
        EventDTO updated = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> search(@RequestParam String title) {
        return ResponseEntity.ok(eventService.searchEvents(title));
    }
}
