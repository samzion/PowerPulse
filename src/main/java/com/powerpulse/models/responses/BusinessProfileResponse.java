package com.powerpulse.models.responses;

import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;

import java.time.LocalDateTime;
import java.util.UUID;

public record BusinessProfileResponse(
        UUID id,
        String businessName,
        BusinessType businessType,
        LagosZone location,
        LocalDateTime createdAt
) {}