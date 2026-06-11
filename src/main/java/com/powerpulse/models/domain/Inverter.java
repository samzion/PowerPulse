package com.powerpulse.models.domain;

public class Inverter extends AbstractPowerSource implements Billable{

    private static final double MINIMUM_BATTERY_THRESHOLD = 20.0;
    private static final double RATED_OUTPUT_KWH = 0.8;

    private double batteryPercent; // 0.0 to 100.0

    private  final double batteryCostPerKwh; // amortised battery cost

    public Inverter(double batteryPercent, double batteryCostPerKwh) {

        this.batteryPercent = batteryPercent;
        this.batteryCostPerKwh = batteryCostPerKwh;
    }

    @Override
    public double calculateCost(double kwh) {
        return kwh * batteryCostPerKwh;
    }

    // Battery drains over time
    public void discharge(double percentUsed) {
        this.batteryPercent = Math.max(0, this.batteryPercent - percentUsed);
    }
    
    @Override
    public String getSourceName() {
        return "Inverter";
    }

    @Override
    public double getCurrentOutput() {
        // Inverter produces rated output when battery is above minimum threshold
        return isAvailable() ? RATED_OUTPUT_KWH : 0.0;
    }

    @Override
    public boolean isAvailable() {
        // Cuts off below 20% to protect battery lifespan — standard inverter behaviour
        return batteryPercent > MINIMUM_BATTERY_THRESHOLD;
    }
}
