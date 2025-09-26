package com.alerting.strategy;

import com.alerting.model.Alert;
import com.alerting.model.User;

public interface NotificationChannel {
    void send(Alert alert, User user);
}
