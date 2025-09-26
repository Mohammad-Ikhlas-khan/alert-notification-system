package com.alerting.service;

import com.alerting.model.Alert;
import com.alerting.model.Alert.Severity;
import com.alerting.model.UserAlertPreference;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    

    private final AlertService alertService;
    private final NotificationService notificationService;
    private final UserPreferenceService preferenceService;
    
    public AnalyticsService(AlertService alertService,
                            NotificationService notificationService,
                            UserPreferenceService preferenceService) {
        this.alertService = alertService;
        this.notificationService = notificationService;
        this.preferenceService = preferenceService;
    }
     
   
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        List<Alert> alerts = alertService.listAlerts(null, null);
        metrics.put("totalAlerts", alerts.size());

        int totalDelivered = notificationService.getTotalAlertsSent();
        metrics.put("totalDelivered", totalDelivered);

        long totalRead = preferenceService.getAllPreferences().values().stream()
                .filter(p -> p.getState() == UserAlertPreference.State.READ).count();
        metrics.put("totalRead", totalRead);

        Map<String, Integer> snoozedPerAlert = new HashMap<>();
        for (Alert alert : alerts) {
                int snoozedCount = preferenceService.countSnoozedForAlert(alert.getId());
                snoozedPerAlert.put("Alert " + alert.getId(), snoozedCount);
        }
        
        metrics.put("totalSnoozed", snoozedPerAlert);
        Map<Severity, Long> severityCount = alerts.stream()
                .collect(Collectors.groupingBy(Alert::getSeverity, Collectors.counting()));

        metrics.put("severityBreakdown", severityCount);

        return metrics;
    }
}
