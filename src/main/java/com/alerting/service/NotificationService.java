package com.alerting.service;

import com.alerting.model.Alert;
import com.alerting.model.NotificationDelivery;
import com.alerting.model.User;
import com.alerting.observer.NotificationObserver;
import com.alerting.strategy.NotificationChannel;
import org.springframework.stereotype.Service;
import com.alerting.strategy.InAppChannel;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final Map<Integer, List<NotificationDelivery>> deliveryLog = new HashMap<>();
    private final List<NotificationObserver> observers = new ArrayList<>();
    private final Map<String, NotificationChannel> channelRegistry = new HashMap<>();
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    public void registerChannel(String channelName, NotificationChannel channel) {
        channelRegistry.put(channelName.toLowerCase(), channel);
    }

    public void sendNotification(Alert alert, User user) {
        System.out.println("Sent alert " + alert.getId() + " to user " + user.getId());
        for (String dt : alert.getDeliveryTypes().stream().distinct().toList()) {
            NotificationChannel channel = channelRegistry.get(dt.toLowerCase());
            if (channel != null) {
                channel.send(alert, user);
                logDelivery(alert.getId(), user.getId());
                notifyObservers(alert, user);
            }
        }
    }
    
    public void logDelivery(int alertId, int userId) {
        NotificationDelivery delivery = new NotificationDelivery(alertId, userId, LocalDateTime.now());
        deliveryLog.computeIfAbsent(alertId, k -> new ArrayList<>()).add(delivery);
    }

    private void notifyObservers(Alert alert, User user) {
        for (NotificationObserver obs : observers) {
            obs.onNotificationSent(alert, user);
        }
    }
    
    // Analytics
    public int getTotalAlertsSent() { 
        int total = deliveryLog.values().stream().mapToInt(List::size).sum(); 
        return total;
     }

    public int getAlertDeliveryCount(int alertId) {
        return deliveryLog.getOrDefault(alertId, Collections.emptyList()).size();
    }

}
