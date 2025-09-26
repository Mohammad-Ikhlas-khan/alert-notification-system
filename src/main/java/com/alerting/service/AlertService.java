package com.alerting.service;

import com.alerting.model.Alert;
import com.alerting.model.Alert.Severity;
import org.springframework.stereotype.Service;
import com.alerting.model.User;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AlertService {
    private final SeedDataService seedDataService;
    private final Map<Integer, Alert> alerts = new HashMap<>();
    private int alertIdCounter = 1;

     public AlertService(SeedDataService seedDataService) {
        this.seedDataService = seedDataService;
    }

    public Alert createAlert(String title, String message, Severity severity,
                             LocalDateTime start, LocalDateTime expiry,
                             boolean remindersEnabled, List<String> deliveryTypes,
                             boolean orgWide, List<Integer> teamIds, List<Integer> userIds) {
        Alert alert = new Alert(alertIdCounter++, title, message, severity,
                start, expiry, remindersEnabled, deliveryTypes, orgWide, teamIds, userIds);
        alerts.put(alert.getId(), alert);
        return alert;
    }

    public Alert getAlert(int id) {
        return alerts.get(id);
    }

    public List<Alert> listAlerts(Severity severity, Boolean activeStatus) {
        LocalDateTime now = LocalDateTime.now();
        return alerts.values().stream()
                .filter(a -> (severity == null || a.getSeverity() == severity)
                        && (activeStatus == null ||
                        (activeStatus ? (a.getStartTime().isBefore(now) || a.getStartTime().isEqual(now)) &&
                                a.getExpiryTime().isAfter(now) && !a.isArchived()
                                : a.isArchived() || a.getExpiryTime().isBefore(now))))
                .collect(Collectors.toList());
    }

    public void updateAlert(int alertId, String title, String message, Severity severity,
                            LocalDateTime start, LocalDateTime expiry, Boolean remindersEnabled,
                            Boolean archived, List<String> deliveryTypes,
                            Boolean orgWide, List<Integer> teamIds, List<Integer> userIds) {
        Alert alert = alerts.get(alertId);
        if (alert == null) return;
        if (title != null) alert.setTitle(title);
        if (message != null) alert.setMessage(message);
        if (severity != null) alert.setSeverity(severity);
        if (start != null) alert.setStartTime(start);
        if (expiry != null) alert.setExpiryTime(expiry);
        if (remindersEnabled != null) alert.setRemindersEnabled(remindersEnabled);
        if (archived != null) alert.setArchived(archived);
        if (deliveryTypes != null) {
            alert.getDeliveryTypes().clear();
            alert.getDeliveryTypes().addAll(deliveryTypes);
        }
        if (orgWide != null) alert.setOrgWide(orgWide);
        if (teamIds != null) alert.setTeamIds(teamIds);
        if (userIds != null) alert.setUserIds(userIds);
    }
    

    public int getTotalUsersForAlert(Alert alert) {
        List<User> allUsers = seedDataService.getSeedUsers();
        
        if (alert.isOrgWide()) {
            // orgWide - all users count
            return allUsers.size();
        }

        Set<Integer> userSet = new HashSet<>();

        // Add users explicitly listed in alert's userIds
        if (alert.getUserIds() != null) {
            userSet.addAll(alert.getUserIds());
        }

        // Add users from teams in alert's teamIds
        if (alert.getTeamIds() != null) {
            Set<Integer> teamIds = new HashSet<>(alert.getTeamIds());
            List<Integer> teamUsers = allUsers.stream()
                .filter(user -> teamIds.contains(user.getTeamId()))
                .map(User::getId)
                .collect(Collectors.toList());
            userSet.addAll(teamUsers);
        }

        return userSet.size();
    }
}
