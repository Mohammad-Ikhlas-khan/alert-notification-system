package com.alerting.observer;

import com.alerting.model.Alert;
import com.alerting.model.User;

public class AnalyticsObserver implements NotificationObserver {
    @Override
    public void onNotificationSent(Alert alert, User user) {
        // Here you can put logic to track metrics, increment counters, etc.
        System.out.println("[ANALYTICS] Tracked delivery of alert '" + alert.getTitle() + "' to user " + user.getName());
    }
}
