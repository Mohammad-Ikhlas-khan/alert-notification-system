package com.alerting;

import com.alerting.service.SeedDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SeedDataService seedDataService;

    public DataInitializer(SeedDataService seedDataService) {
        this.seedDataService = seedDataService;
    }

    @Override
    public void run(String... args) {
        // Just access seed data once to trigger initialization
        System.out.println("Seeding users: " + seedDataService.getSeedUsers().size());
    }
}
