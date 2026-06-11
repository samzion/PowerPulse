package com.powerpulse.repositories;

import com.powerpulse.models.entities.BusinessProfile;
import com.powerpulse.models.entities.EnergyUsageRecord;
import com.powerpulse.models.enums.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EnergyUsageRepository
        extends JpaRepository<EnergyUsageRecord, UUID> {

    List<EnergyUsageRecord> findByBusinessProfileAndIsDeletedFalse(
            BusinessProfile businessProfile);

    List<EnergyUsageRecord> findByBusinessProfileAndSourceTypeAndIsDeletedFalse(
            BusinessProfile businessProfile, SourceType sourceType);

    List<EnergyUsageRecord> findByBusinessProfileAndTimestampBetweenAndIsDeletedFalse(
            BusinessProfile businessProfile,
            LocalDateTime start,
            LocalDateTime end);

    @Query("""
            SELECT COALESCE(SUM(e.costNaira), 0)
            FROM EnergyUsageRecord e
            WHERE e.businessProfile = :business
            AND e.timestamp BETWEEN :start AND :end
            AND e.isDeleted = false
            """)
    BigDecimal getTotalCostBetween(
            @Param("business") BusinessProfile business,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
            SELECT COALESCE(SUM(e.costNaira), 0)
            FROM EnergyUsageRecord e
            WHERE e.businessProfile = :business
            AND e.sourceType = :sourceType
            AND e.timestamp BETWEEN :start AND :end
            AND e.isDeleted = false
            """)
    BigDecimal getTotalCostBySourceBetween(
            @Param("business") BusinessProfile business,
            @Param("sourceType") SourceType sourceType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
            SELECT COALESCE(SUM(e.hoursUsed), 0)
            FROM EnergyUsageRecord e
            WHERE e.businessProfile = :business
            AND e.sourceType = :sourceType
            AND e.timestamp BETWEEN :start AND :end
            AND e.isDeleted = false
            """)
    BigDecimal getTotalHoursBySourceBetween(
            @Param("business") BusinessProfile business,
            @Param("sourceType") SourceType sourceType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}