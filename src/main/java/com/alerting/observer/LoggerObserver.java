package com.alerting.observer;

import com.alerting.model.Alert;
import com.alerting.model.User;

public class LoggerObserver implements NotificationObserver {
    @Override
    public void onNotificationSent(Alert alert, User user) {
        System.out.println("[LOGGER] Alert '" + alert.getTitle() + "' sent to user " + user.getName());
    }
}
