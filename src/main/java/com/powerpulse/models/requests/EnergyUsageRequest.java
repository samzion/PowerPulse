package com.powerpulse.models.requests;

import com.powerpulse.models.enums.SourceType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EnergyUsageRequest(

        @NotNull(message = "Business ID is required")
        UUID businessId,

        @NotNull(message = "Source type is required")
        SourceType sourceType,

        @NotNull(message = "Hours used is required")
        @Positive(message = "Hours used must be greater than zero")
        @DecimalMax(value = "24.0", message = "Hours used cannot exceed 24")
        BigDecimal hoursUsed,

        @NotNull(message = "Estimated kWh is required")
        @Positive(message = "Estimated kWh must be greater than zero")
        BigDecimal estimatedKwh,

        @NotNull(message = "Cost in Naira is required")
        @Positive(message = "Cost must be greater than zero")
        BigDecimal costNaira,

        @NotNull(message = "Timestamp is required")
        @PastOrPresent(message = "Timestamp cannot be in the future")
        LocalDateTime timestamp
) {}