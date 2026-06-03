package com.powerpulse.domain;

public class NepaPower extends AbstractPowerSource {

    private boolean supplyOn;

    private static final double RATED_OUTPUT_KWH = 1.5; // standard residential

    public NepaPower(boolean supplyOn) {
        this.supplyOn = supplyOn;
    }

    // NEPA comes and goes
    public void updateSupply(boolean isOn) {
        this.supplyOn = isOn;
    }

    @Override
    public String getSourceName() {
        return "NEPA";
    }

    @Override
    public double getCurrentOutput() {
        return supplyOn ? RATED_OUTPUT_KWH: 0.0;
    }

    @Override
    public boolean isAvailable() {
        return supplyOn;
    }
}
