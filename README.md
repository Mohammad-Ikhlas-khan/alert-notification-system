# 🚨 Alert Notification System

A Spring Boot–based alerting and notification system that manages alerts, reminders, and user preferences. It supports in-app notification channels (extensible to email, SMS, etc.) and includes scheduled reminders with snoozing functionality.

## 📖 Summary

This project provides:

- **Alerts Management**: Create, list, and manage alerts with severity, reminders, and delivery channels.

- **Notifications**: Sends alerts to users via registered channels (currently in-app).

- **Reminders**: Automatically triggers every 2 hours for active alerts.

- **User Preferences**: Supports snoozing alerts and read/unread alerts.

- **Extensible Channels**: Add new notification channels (e.g., Email, Slack) easily via `NotificationChannel` strategy.


## Setup Instructions

### Prerequisites

- *Java 17+*

- *Maven 3.8+*

- *Spring Boot Basic* (already included in dependencies)

## Installation and Running

### Clone the repository:

```bash
git clone https://github.com/Mohammad-Ikhlas-khan/alert-notification-system.git
```

### Change directory to project folder
```bash
cd alert-notification-system
```


### Build the project:


  ```bash
  mvn clean install
  ```

### Run the Spring Boot application:

  ```bash
  mvn spring-boot:run
  ```

### Access the API:
 Open [http://127.0.0.1:8080/](http://127.0.0.1:8080/) in your browser.

## 🌐 API Endpoints

### 1️⃣ Admin APIs (`/admin/alerts`)

#### Create Alert
```http
POST /admin/alerts
```

**Example Request Body:**
```json
{
  "title": "Server Down",
  "message": "Production server unreachable",
  "severity": "CRITICAL",
  "startTime": "2025-09-26T12:00:00",
  "expiryTime": "2025-09-27T12:00:00",
  "remindersEnabled": true,
  "deliveryTypes": ["inapp"],
  "orgWide": true,
  "teamIds": [],
  "userIds": []
}
```

#### Update Alert
```http
PATCH /admin/alerts/{alertId}
```

#### List Alerts
```http
GET /admin/alerts
```

#### Alerts by Severity
```http
GET /admin/alerts/severity/{severity}
```

#### Alerts by Status
```http
GET /admin/alerts/status/{status}
```
- **Status:** `active` or `expired`

#### Snoozed Statistics
```http
GET /admin/alerts/snoozedstats
```
Returns snoozed percentage per alert.

### 2️⃣ User APIs (`/user`)

#### Get User Alerts
```http
GET /user/{userId}/alerts
```

#### Mark Alert as Read
```http
POST /user/{userId}/alerts/{alertId}/read
```

#### Mark Alert as Unread
```http
POST /user/{userId}/alerts/{alertId}/unread
```

#### Snooze Alert
```http
POST /user/{userId}/alerts/{alertId}/snooze
```

#### Get Snoozed Alerts
```http
GET /user/{userId}/alerts/snoozed
```

### 3️⃣ Analytics API

#### Get Dashboard Metrics
```http
GET /analytics
```

**Response Example:**
```json
{
  "severityBreakdown": {
    "INFO": 2,
    "WARNING": 1
  },
  "totalAlerts": 3,
  "totalDelivered": 17,
  "totalRead": 0,
  "totalSnoozed": {
    "Alert 1": 0,
    "Alert 3": 0,
    "Alert 2": 0
  }
}
```

## Seed Data

The application uses the following seed data for testing and development:

### Teams
- **Engineering Team** (ID: 1)
- **Marketing Team** (ID: 2)

### Users
- **Alice** (ID: 1) - Member of Engineering Team
- **Bob** (ID: 2) - Member of Marketing Team  
- **Charlie** (ID: 3) - Member of Engineering Team

### Code Reference
```java
Team engineering = new Team(1, "Engineering");
Team marketing = new Team(2, "Marketing");

User alice = new User(1, "Alice", engineering.getId());
User bob = new User(2, "Bob", marketing.getId());
User charlie = new User(3, "Charlie", engineering.getId());

this.seedUsers = Arrays.asList(alice, bob, charlie);
```

## 🚀 Extending

- Add new notification channels (EmailChannel, SlackChannel, etc.).

- Hook into real message queues or databases.

- Replace seed data with persistent storage.


