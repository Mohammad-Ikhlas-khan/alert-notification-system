package com.alerting.config;

import com.alerting.service.NotificationService;
import com.alerting.strategy.InAppChannel;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationChannelConfig {

    public NotificationChannelConfig(NotificationService notificationService) {
        notificationService.registerChannel("inapp", new InAppChannel());
        // You can add more channels here:
        // notificationService.registerChannel("email", new EmailNotificationChannel());
    }
}
