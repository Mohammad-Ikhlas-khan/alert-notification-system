package com.alerting.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Alert {
    public enum Severity { INFO, WARNING, CRITICAL }

    private final int id;
    private String title;
    private String message;
    private Severity severity;
    private LocalDateTime startTime;
    private LocalDateTime expiryTime;
    private boolean remindersEnabled;
    private final List<String> deliveryTypes; // e.g., "inapp"
    private boolean archived;

    // Visibility: true if org-wide, else specify teams/user IDs
    private boolean orgWide;
    private List<Integer> teamIds = new ArrayList<>();
    private List<Integer> userIds = new ArrayList<>();

    public Alert(int id, String title, String message, Severity severity,
                 LocalDateTime startTime, LocalDateTime expiryTime,
                 boolean remindersEnabled, List<String> deliveryTypes,
                 boolean orgWide, List<Integer> teamIds, List<Integer> userIds) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.severity = severity;
        this.startTime = startTime;
        this.expiryTime = expiryTime;
        this.remindersEnabled = remindersEnabled;
        this.deliveryTypes = deliveryTypes;
        this.orgWide = orgWide;
        if (teamIds != null) this.teamIds = teamIds;
        if (userIds != null) this.userIds = userIds;
        this.archived = false;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Severity getSeverity() { return severity; }

    public void setSeverity(Severity severity) { this.severity = severity; }

    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getExpiryTime() { return expiryTime; }

    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }

    public boolean isRemindersEnabled() { return remindersEnabled; }

    public void setRemindersEnabled(boolean remindersEnabled) { this.remindersEnabled = remindersEnabled; }

    public List<String> getDeliveryTypes() { return deliveryTypes; }

    public boolean isOrgWide() { return orgWide; }

    public void setOrgWide(boolean orgWide) { this.orgWide = orgWide; }

    public List<Integer> getTeamIds() { return teamIds; }

    public void setTeamIds(List<Integer> teamIds) { this.teamIds = teamIds; }

    public List<Integer> getUserIds() { return userIds; }

    public void setUserIds(List<Integer> userIds) { this.userIds = userIds; }

    public boolean isArchived() { return archived; }

    public void setArchived(boolean archived) { this.archived = archived; }
}
