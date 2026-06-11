package com.powerpulse.models.entities;

import com.powerpulse.models.enums.SourceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "energy_usage_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EnergyUsageRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_profile_id", nullable = false)
    private BusinessProfile businessProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Column(name = "hours_used", nullable = false)
    private BigDecimal hoursUsed;

    @Column(name = "estimated_kwh", nullable = false)
    private BigDecimal estimatedKwh;

    @Column(name = "cost_naira", nullable = false)
    private BigDecimal costNaira;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}