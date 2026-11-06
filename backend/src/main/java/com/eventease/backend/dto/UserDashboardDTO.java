package com.eventease.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDashboardDTO {

    private List<RegistrationDTO> upcomingRegistrations;
    private List<RegistrationDTO> registrationHistory;

    public UserDashboardDTO() {}

    public UserDashboardDTO(List<RegistrationDTO> upcomingRegistrations,
                            List<RegistrationDTO> registrationHistory) {
        this.upcomingRegistrations = upcomingRegistrations;
        this.registrationHistory = registrationHistory;
    }

    public List<RegistrationDTO> getUpcomingRegistrations() { return upcomingRegistrations; }
    public void setUpcomingRegistrations(List<RegistrationDTO> upcomingRegistrations) {
        this.upcomingRegistrations = upcomingRegistrations;
    }
    public List<RegistrationDTO> getRegistrationHistory() { return registrationHistory; }
    public void setRegistrationHistory(List<RegistrationDTO> registrationHistory) {
        this.registrationHistory = registrationHistory;
    }
}
