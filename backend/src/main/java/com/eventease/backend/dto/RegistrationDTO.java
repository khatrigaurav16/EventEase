package com.eventease.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RegistrationDTO {

    private Long id;
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime registrationDate;
    private String status;

    public RegistrationDTO() {}

    public RegistrationDTO(Long id, Long userId, Long eventId, String eventTitle,
                           LocalDateTime registrationDate, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
