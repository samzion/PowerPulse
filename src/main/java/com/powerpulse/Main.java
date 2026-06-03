package com.powerpulse;

import com.powerpulse.domain.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<AbstractPowerSource> sources = List.of(
                new Generator(10.0, 1350),
                new NepaPower(true),
                new Inverter(85.0, 150)
        );

// Cast to AbstractPowerSource to access logUsage
        sources.forEach(source -> {
            source.logUsage(2.5);

            // only calculate cost if this source is Billable
            if (source instanceof Billable billable) {
                System.out.println(billable.getCostSummary(2.5));
            }
        });
    }
}
