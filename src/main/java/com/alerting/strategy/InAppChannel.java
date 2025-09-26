package com.alerting.strategy;

import com.alerting.model.Alert;
import com.alerting.model.User;

public class InAppChannel implements NotificationChannel {
    @Override
    public void send(Alert alert, User user) {
        System.out.println("[IN-APP] To User " + user.getName() + ": " + alert.getTitle() + " - " + alert.getMessage());
    }
}
