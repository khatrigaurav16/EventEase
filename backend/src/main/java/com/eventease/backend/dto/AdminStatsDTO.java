package com.eventease.backend.dto;

public class AdminStatsDTO {

    private Long totalEvents;
    private Long totalRegistrations;
    private Long publishedEvents;
    private Long cancelledEvents;
    private Long completedEvents;
    private Long activeRegistrations;

    public AdminStatsDTO() {}

    public AdminStatsDTO(Long totalEvents, Long totalRegistrations, Long publishedEvents,
                         Long cancelledEvents, Long completedEvents, Long activeRegistrations) {
        this.totalEvents = totalEvents;
        this.totalRegistrations = totalRegistrations;
        this.publishedEvents = publishedEvents;
        this.cancelledEvents = cancelledEvents;
        this.completedEvents = completedEvents;
        this.activeRegistrations = activeRegistrations;
    }

    public Long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(Long totalEvents) { this.totalEvents = totalEvents; }
    public Long getTotalRegistrations() { return totalRegistrations; }
    public void setTotalRegistrations(Long totalRegistrations) { this.totalRegistrations = totalRegistrations; }
    public Long getPublishedEvents() { return publishedEvents; }
    public void setPublishedEvents(Long publishedEvents) { this.publishedEvents = publishedEvents; }
    public Long getCancelledEvents() { return cancelledEvents; }
    public void setCancelledEvents(Long cancelledEvents) { this.cancelledEvents = cancelledEvents; }
    public Long getCompletedEvents() { return completedEvents; }
    public void setCompletedEvents(Long completedEvents) { this.completedEvents = completedEvents; }
    public Long getActiveRegistrations() { return activeRegistrations; }
    public void setActiveRegistrations(Long activeRegistrations) { this.activeRegistrations = activeRegistrations; }
}
