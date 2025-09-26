package com.alerting.model;

public class User {
    private final int id;
    private final String name;
    private final int teamId;

    public User(int id, String name, int teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public int getTeamId() { return teamId; }
}
