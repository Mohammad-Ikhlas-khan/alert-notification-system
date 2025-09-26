package com.alerting.service;

import com.alerting.model.UserAlertPreference;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserPreferenceService {
    // Key: userId-alertId
    private final Map<String, UserAlertPreference> preferences = new HashMap<>();

    private String key(int userId, int alertId) {
        return userId + "-" + alertId;
    }

    public Map<String, UserAlertPreference> getAllPreferences() {
    return preferences;
    }


    public UserAlertPreference getOrCreate(int userId, int alertId) {
        String k = key(userId, alertId);
        preferences.putIfAbsent(k, new UserAlertPreference(userId, alertId));
        return preferences.get(k);
    }

    public void markRead(int userId, int alertId) {
        getOrCreate(userId, alertId).setState(UserAlertPreference.State.READ);
    }

    public void markUnread(int userId, int alertId) {
        getOrCreate(userId, alertId).setState(UserAlertPreference.State.UNREAD);
    }

    public void snooze(int userId, int alertId) {
        UserAlertPreference pref = getOrCreate(userId, alertId);
        pref.setState(UserAlertPreference.State.SNOOZED);
        pref.setSnoozedUntil(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)); // Snoozed until midnight next day
    }

    public boolean isSnoozed(int userId, int alertId) {
        UserAlertPreference pref = preferences.get(key(userId, alertId));
        if(pref == null) return false;
        if(pref.getState() != UserAlertPreference.State.SNOOZED) return false;
        return pref.getSnoozedUntil() != null && LocalDateTime.now().isBefore(pref.getSnoozedUntil());
    }

    public int countSnoozedForAlert(int alertId) {
        int count = 0;
        for (UserAlertPreference pref : preferences.values()) {
            if (pref.getAlertId() == alertId && pref.getState() == UserAlertPreference.State.SNOOZED) {
                count++;
            }
        }
        return count;
    }


    public List<UserAlertPreference> getSnoozedForUser(int userId) {
        List<UserAlertPreference> snoozed = new ArrayList<>();
        for (UserAlertPreference pref : preferences.values()) {
            if (pref.getUserId() == userId && pref.getState() == UserAlertPreference.State.SNOOZED) {
                snoozed.add(pref);
            }
        }
        return snoozed;
    }
}
