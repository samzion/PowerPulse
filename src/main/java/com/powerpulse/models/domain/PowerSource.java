package com.powerpulse.models.domain;

public interface PowerSource {
    String getSourceName();
    double getCurrentOutput();
    boolean isAvailable();
}
