package com.alerting.service;

import com.alerting.model.Team;
import com.alerting.model.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SeedDataService {
    private final List<User> seedUsers;
    public SeedDataService() {
        Team engineering = new Team(1, "Engineering");
        Team marketing = new Team(2, "Marketing");

        User alice = new User(1, "Alice", engineering.getId());
        User bob = new User(2, "Bob", marketing.getId());
        User charlie = new User(3, "Charlie", engineering.getId());

        this.seedUsers = Arrays.asList(alice, bob, charlie);
    }

     public List<User> getSeedUsers() {
        return seedUsers;
    }
}
