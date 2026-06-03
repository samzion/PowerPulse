package com.powerpulse.domain;

import java.time.LocalDateTime;

public abstract class AbstractPowerSource implements PowerSource {

    // Shared method — written once, inherited by all three sources
    public void logUsage(double kwh) {
        System.out.printf("[%s] %s logged %.2f kWh%n",
                LocalDateTime.now(),
                getSourceName(),  // polymorphism — each subclass returns its own name
                kwh
        );
    }

    // Shared method — total cost tracking
    public void logCost(double kwh, double ratePerKwh) {
        double cost = kwh * ratePerKwh;
        System.out.printf("[%s] %s cost: ₦%.2f%n",
                LocalDateTime.now(),
                getSourceName(),
                cost
        );
    }
}
