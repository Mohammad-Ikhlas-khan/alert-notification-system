package com.alerting.model;

import java.time.LocalDateTime;

public class NotificationDelivery {
    private final int alertId;
    private final int userId;
    private final LocalDateTime deliveredAt;

    public NotificationDelivery(int alertId, int userId, LocalDateTime deliveredAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.deliveredAt = deliveredAt;
    }

    public int getAlertId() { return alertId; }

    public int getUserId() { return userId; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
}
