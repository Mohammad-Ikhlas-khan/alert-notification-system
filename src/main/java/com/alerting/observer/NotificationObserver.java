package com.alerting.observer;

import com.alerting.model.Alert;
import com.alerting.model.User;

public interface NotificationObserver {
    void onNotificationSent(Alert alert, User user);
}
