package com.powerpulse.models.responses;

import com.powerpulse.models.enums.SourceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EnergyUsageResponse(
        UUID id,
        UUID businessId,
        String businessName,
        SourceType sourceType,
        BigDecimal hoursUsed,
        BigDecimal estimatedKwh,
        BigDecimal costNaira,
        LocalDateTime timestamp,
        LocalDateTime createdAt
) {}