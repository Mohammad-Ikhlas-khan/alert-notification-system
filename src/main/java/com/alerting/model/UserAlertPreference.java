package com.alerting.model;

import java.time.LocalDateTime;

public class UserAlertPreference {
    public enum State { UNREAD, READ, SNOOZED }

    private final int userId;
    private final int alertId;
    private State state;
    private LocalDateTime snoozedUntil; // null if not snoozed

    public UserAlertPreference(int userId, int alertId) {
        this.userId = userId;
        this.alertId = alertId;
        this.state = State.UNREAD;
        this.snoozedUntil = null;
    }

    public int getUserId() { return userId; }

    public int getAlertId() { return alertId; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public LocalDateTime getSnoozedUntil() { return snoozedUntil; }

    public void setSnoozedUntil(LocalDateTime snoozedUntil) { this.snoozedUntil = snoozedUntil; }

    public boolean isSnoozed() {
        if (state == State.SNOOZED && snoozedUntil != null) {
            return snoozedUntil.isAfter(LocalDateTime.now());
        }
        return false;
    }
}
