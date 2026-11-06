package com.eventease.backend.dto;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequestDTO {

    @NotNull
    private Long eventId;

    public RegistrationRequestDTO() {}

    public RegistrationRequestDTO(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
