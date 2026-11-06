package com.eventease.backend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Long categoryId;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private LocalTime eventTime;

    @NotBlank
    @Size(max = 100)
    private String venue;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotBlank
    @Size(max = 100)
    private String organizerName;

    @NotBlank
    @Size(max = 20)
    private String status;

    public EventRequestDTO() {}

    public EventRequestDTO(String title, String description, Long categoryId,
                           LocalDate eventDate, LocalTime eventTime, String venue,
                           Integer capacity, String organizerName, String status) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.capacity = capacity;
        this.organizerName = organizerName;
        this.status = status;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
