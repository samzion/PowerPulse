package com.powerpulse.repositories;

import com.powerpulse.models.entities.BusinessProfile;
import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessProfileRepository
        extends JpaRepository<BusinessProfile, UUID> {
    Optional<BusinessProfile> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByBusinessNameAndIsDeletedFalse(String businessName);

    @Query("""
            SELECT b FROM BusinessProfile b
            WHERE b.isDeleted = false
            AND (:type IS NULL OR b.businessType = :type)
            AND (:location IS NULL OR b.location = :location)
            AND (:name IS NULL OR LOWER(b.businessName) LIKE LOWER(CONCAT('%', :name, '%')))
            ORDER BY b.createdAt DESC
            """)
    List<BusinessProfile> findAllWithFilters(
            @Param("type") BusinessType type,
            @Param("location") LagosZone location,
            @Param("name") String name
    );
}