package com.powerpulse.models.requests;

import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BusinessProfileRequest(

        @NotBlank(message = "Business name is required")
        @Size(max = 200, message = "Business name cannot exceed 200 characters")
        String businessName,

        @NotNull(message = "Business type is required")
        BusinessType businessType,

        @NotNull(message = "Location is required")
        LagosZone location
) {}