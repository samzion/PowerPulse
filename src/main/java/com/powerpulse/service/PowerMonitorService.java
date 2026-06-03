package com.powerpulse.service;

import com.powerpulse.domain.PowerSource;

import java.util.List;

public class PowerMonitorService {

    private final List<? extends PowerSource> sources;

    public PowerMonitorService(List<? extends PowerSource> sources) {
        this.sources = sources;
    }

    // Return only available sources
    public List<? extends PowerSource> getActiveSources() {
        // hint: stream + filter
        return sources.stream().filter(PowerSource::isAvailable).toList();
    }

    // Return total kWh output across all active sources
    public double getTotalOutput() {
        // hint: stream + mapToDouble + sum
        return sources.stream()
                .filter(PowerSource::isAvailable)
                .mapToDouble(PowerSource::getCurrentOutput)
                .sum();
    }

    // Print each source name, availability, and output
    public void logAllSources() {
        // hint: forEach
        System.out.println("=============================================");
        System.out.printf("%-15s %-15s %-10s%n", "Source", "Available", "Output (kWh)");
        System.out.println("---------------------------------------------");
       sources.forEach(source ->
               System.out.printf("%-15s %-15s %-10s%n",
                       source.getSourceName(),
                       source.isAvailable(),
                       source.getCurrentOutput()
               ));
        System.out.println("=============================================");
    }
}
