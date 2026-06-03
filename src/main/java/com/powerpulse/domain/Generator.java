package com.powerpulse.domain;

public class Generator extends AbstractPowerSource implements Billable {

    private double fuelLevel; // litres remaining
    private final double fuelRatePerKwh;

    public Generator(double fuelLevel, double fuelRatePerKwh) {
        this.fuelLevel = fuelLevel;
        this.fuelRatePerKwh = fuelRatePerKwh;
    }

    @Override
    public double calculateCost(double kwh) {
        return kwh * fuelRatePerKwh;
    }

    // Generator needs to lose fuel over time
    public void reduceFuel(double litresUsed) {
        this.fuelLevel = Math.max(0, this.fuelLevel - litresUsed);
    }

    @Override
    public String getSourceName() {
        return "Generator";
    }

    @Override
    public double getCurrentOutput() {
        // Generator produces 2.5kWh when fuel is available, nothing when empty
        return fuelLevel > 0 ? 2.5 : 0.0;
    }

    @Override
    public boolean isAvailable() {
        return fuelLevel > 0;
    }
}
