package com.alerting.api;

import com.alerting.model.Alert;
import com.alerting.model.UserAlertPreference;
import com.alerting.service.AlertService;
import com.alerting.service.UserPreferenceService;
import com.alerting.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserAlertController {

    private final AlertService alertService;
    private final UserPreferenceService preferenceService;
    private final ReminderService reminderService;

    public UserAlertController(AlertService alertService,
                               UserPreferenceService preferenceService,
                               ReminderService reminderService) {
        this.alertService = alertService;
        this.preferenceService = preferenceService;
        this.reminderService = reminderService;
    }

    @GetMapping("/{userId}/alerts")
    public ResponseEntity<List<UserAlertSummary>> getUserAlerts(@PathVariable int userId) {
        List<Alert> userAlerts = reminderService.getRecipientsForUser(userId);

        List<UserAlertSummary> summaries = userAlerts.stream()
                                                    .map(alert -> new UserAlertSummary(alert.getId(),alert.getTitle(), alert.getMessage()))
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @PostMapping("/{userId}/alerts/{alertId}/read")
    public ResponseEntity<Void> markRead(@PathVariable int userId, @PathVariable int alertId) {
        preferenceService.markRead(userId, alertId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/alerts/{alertId}/unread")
    public ResponseEntity<Void> markUnread(@PathVariable int userId, @PathVariable int alertId) {
        preferenceService.markUnread(userId, alertId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/alerts/{alertId}/snooze")
    public ResponseEntity<Void> snooze(@PathVariable int userId, @PathVariable int alertId) {
        preferenceService.snooze(userId, alertId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/alerts/snoozed")
    public ResponseEntity<List<UserAlertSummary>> getSnoozedAlerts(@PathVariable int userId) {
    List<UserAlertPreference> snoozedPrefs = preferenceService.getSnoozedForUser(userId);

    // Fetch Alert objects for these preferences
    List<UserAlertSummary> snoozedAlerts = new ArrayList<>();
    for (UserAlertPreference pref : snoozedPrefs) {
        Alert alert = alertService.getAlert(pref.getAlertId());
        if (alert != null) {
            snoozedAlerts.add(new UserAlertSummary(alert.getId(),alert.getTitle(), alert.getMessage()));
        }
    }
    return ResponseEntity.ok(snoozedAlerts);
}
    public static class UserAlertSummary {
        public int id;
        public String title;
        public String message;

        public UserAlertSummary(int id,String title, String message) {
            this.id=id;
            this.title = title;
            this.message = message;
        }
    }
}
