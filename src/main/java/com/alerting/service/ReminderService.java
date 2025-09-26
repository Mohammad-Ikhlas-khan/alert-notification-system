package com.alerting.service;

import com.alerting.model.Alert;
import com.alerting.model.User;
import com.alerting.model.Team;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    private final AlertService alertService;
    private final UserPreferenceService preferenceService;
    private final NotificationService notificationService;
    private final SeedDataService seedDataService;
    
    public ReminderService(AlertService alertService,
                           UserPreferenceService preferenceService,
                           NotificationService notificationService,SeedDataService seedDataService) {
        this.alertService = alertService;
        this.preferenceService = preferenceService;
        this.notificationService = notificationService;
        this.seedDataService = seedDataService;
    }

    public void triggerReminders() {

        LocalDateTime now = LocalDateTime.now();
        List<Alert> activeAlerts = alertService.listAlerts(null, true);
        for (Alert alert : activeAlerts) {
            if (!alert.isRemindersEnabled()) continue;

            List<User> recipients = getRecipients(alert);

            for (User user : recipients) {
                // Check if user snoozed this alert
                if (preferenceService.isSnoozed(user.getId(), alert.getId())){

                    continue;
                }
                // Optionally check for read/unread status here

                // Send notification
                notificationService.sendNotification(alert, user);
            }
        }
    }

    private List<User> getRecipients(Alert alert) {
         List<User> allUsers = seedDataService.getSeedUsers();

        if (alert.isOrgWide()) {
            return allUsers;
        } else {
            return allUsers.stream()
                    .filter(u -> (alert.getTeamIds().contains(u.getTeamId()) || alert.getUserIds().contains(u.getId())))
                    .collect(Collectors.toList());
        }
    }
    
    public List<Alert> getRecipientsForUser(int userId) {
        List<User> allUsers = seedDataService.getSeedUsers();
        User user = allUsers.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElse(null);
        if (user == null) return new ArrayList<>();
        List<Alert> activeAlerts = alertService.listAlerts(null, true);

        return activeAlerts.stream()
                .filter(alert -> (alert.isOrgWide()
                        || (alert.getTeamIds() != null && alert.getTeamIds().contains(user.getTeamId()))
                        || (alert.getUserIds() != null && alert.getUserIds().contains(userId)))
                )
                   .filter(alert -> !preferenceService.isSnoozed(userId, alert.getId()))   
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 7200000) // every 60 seconds
    public void scheduledTrigger() {
        System.out.println("Running triggerReminders at " + LocalDateTime.now());
        triggerReminders();
    }

}
