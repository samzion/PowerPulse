package com.powerpulse.models.entities;

import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BusinessProfile extends BaseEntity {

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private LagosZone location;
}