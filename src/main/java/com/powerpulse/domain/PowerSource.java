package com.powerpulse.domain;

public interface PowerSource {
    String getSourceName();
    double getCurrentOutput();
    boolean isAvailable();
}
