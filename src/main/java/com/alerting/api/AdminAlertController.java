package com.alerting.api;

import com.alerting.model.Alert;
import com.alerting.model.Alert.Severity;
import com.alerting.service.AlertService;
import com.alerting.service.UserPreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/admin/alerts")
public class AdminAlertController {

    private final AlertService alertService;
    private final UserPreferenceService preferenceService;
    public AdminAlertController(AlertService alertService,UserPreferenceService preferenceService) {
        this.alertService = alertService;
        this.preferenceService=preferenceService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody AlertDTO dto) {
        Alert alert = alertService.createAlert(
                dto.title, dto.message, dto.severity,
                dto.startTime, dto.expiryTime,
                dto.remindersEnabled, dto.deliveryTypes,
                dto.orgWide, dto.teamIds, dto.userIds);
        return ResponseEntity.ok(alert);
    }

    @PatchMapping("/{alertId}")
    public ResponseEntity<Alert> updateAlert(@PathVariable int alertId, @RequestBody AlertDTO dto) {
        alertService.updateAlert(alertId, dto.title, dto.message, dto.severity,
                dto.startTime, dto.expiryTime, dto.remindersEnabled,
                dto.archived, dto.deliveryTypes, dto.orgWide, dto.teamIds, dto.userIds);
        Alert updated = alertService.getAlert(alertId);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<AlertDTO>> listAlerts(
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) Boolean active) {
        //return ResponseEntity.ok(alertService.listAlerts(severity, active));
        List<Alert> alerts = alertService.listAlerts(severity, active);

        // Map to DTO with severity included
        List<AlertDTO> dtoList = alerts.stream()
                .map(AlertDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/snoozedstats")
    public ResponseEntity<Map<String, Double>> getSnoozeMetrics() {
        List<Alert> alerts = alertService.listAlerts(null, null);
        Map<String, Double> snoozedPerUser = new HashMap<>();

        for (Alert alert : alerts) {
            int snoozedCount = preferenceService.countSnoozedForAlert(alert.getId());
            int totalUser = alertService.getTotalUsersForAlert(alert);

            double snoozePercentage = 0.0;
            if (totalUser > 0) {
                snoozePercentage = snoozedCount * 100.0 / totalUser;
            }

            snoozedPerUser.put("Alert" + alert.getId(), snoozePercentage);
        }

         return ResponseEntity.ok(snoozedPerUser);
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Alert>> getAlertsBySeverity(@PathVariable Severity severity) {
        List<Alert> alerts = alertService.listAlerts(severity, null);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Alert>> getAlertsByStatus(@PathVariable String status) {
            Boolean activeStatus;

            if ("active".equalsIgnoreCase(status)) {
                activeStatus = true;
            } else if ("expired".equalsIgnoreCase(status)) {
                activeStatus = false;
            } else {
                return ResponseEntity.badRequest()
                        .body(Collections.emptyList());
            }

            List<Alert> alerts = alertService.listAlerts(null, activeStatus);
            return ResponseEntity.ok(alerts);
        }


    public static class AlertDTO {
        public int id;
        public String title;
        public String message;
        public Severity severity;
        public LocalDateTime startTime;
        public LocalDateTime expiryTime;
        public Boolean remindersEnabled;
        public Boolean archived;
        public List<String> deliveryTypes;
        public Boolean orgWide;
        public List<Integer> teamIds;
        public List<Integer> userIds;

        public AlertDTO() {}

        // Constructor from Alert entity
        public AlertDTO(Alert alert) {
            this.id=alert.getId();
            this.title = alert.getTitle();
            this.message = alert.getMessage();
            this.severity = alert.getSeverity();
            this.startTime = alert.getStartTime();
            this.expiryTime = alert.getExpiryTime();
            this.remindersEnabled = alert.isRemindersEnabled();
            this.archived = alert.isArchived();
            this.deliveryTypes = alert.getDeliveryTypes();
            this.orgWide = alert.isOrgWide();
            this.teamIds = alert.getTeamIds();
            this.userIds = alert.getUserIds();
        }
    }
}
