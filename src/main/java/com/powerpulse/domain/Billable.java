package com.powerpulse.domain;

public interface Billable {
    double calculateCost(double kwh);

    default String getCostSummary(double kwh) {
        return String.format("Cost for %.2f kWh: ₦%.2f", kwh, calculateCost(kwh));
    }
}