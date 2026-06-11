package com.powerpulse.repositories;

import com.powerpulse.models.entities.BusinessProfile;
import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessProfileRepository
        extends JpaRepository<BusinessProfile, UUID> {

    List<BusinessProfile> findByIsDeletedFalse();

    Optional<BusinessProfile> findByIdAndIsDeletedFalse(UUID id);

    List<BusinessProfile> findByBusinessTypeAndIsDeletedFalse(
            BusinessType businessType);

    List<BusinessProfile> findByLocationAndIsDeletedFalse(
            LagosZone location);

    List<BusinessProfile> findByBusinessTypeAndLocationAndIsDeletedFalse(
            BusinessType businessType, LagosZone location);

    boolean existsByBusinessNameAndIsDeletedFalse(String businessName);
}