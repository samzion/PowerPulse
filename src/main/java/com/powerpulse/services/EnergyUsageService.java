package com.powerpulse.services;

import com.powerpulse.models.entities.BusinessProfile;
import com.powerpulse.models.entities.EnergyUsageRecord;
import com.powerpulse.models.requests.EnergyUsageRequest;
import com.powerpulse.models.responses.EnergyUsageResponse;
import com.powerpulse.repositories.BusinessProfileRepository;
import com.powerpulse.repositories.EnergyUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyUsageService {

    private final EnergyUsageRepository usageRepository;
    private final BusinessProfileRepository businessRepository;

    @Transactional
    public EnergyUsageResponse logUsage(EnergyUsageRequest request) {
        log.info("Logging usage for business: {} source: {}",
                request.businessId(), request.sourceType());

        BusinessProfile business = businessRepository
                .findByIdAndIsDeletedFalse(request.businessId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Business not found with id: " + request.businessId()
                ));

        EnergyUsageRecord record = new EnergyUsageRecord();
        record.setBusinessProfile(business);
        record.setSourceType(request.sourceType());
        record.setHoursUsed(request.hoursUsed());
        record.setEstimatedKwh(request.estimatedKwh());
        record.setCostNaira(request.costNaira());
        record.setTimestamp(request.timestamp());

        EnergyUsageRecord saved = usageRepository.save(record);
        log.info("Usage record saved with id: {}", saved.getId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<EnergyUsageResponse> getAll() {
        log.info("Fetching all usage records");
        return usageRepository.findAll()
                .stream()
                .filter(r -> !r.isDeleted())
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnergyUsageResponse> getByBusiness(UUID businessId) {
        log.info("Fetching usage records for business: {}", businessId);

        BusinessProfile business = businessRepository
                .findByIdAndIsDeletedFalse(businessId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Business not found with id: " + businessId
                ));

        return usageRepository
                .findByBusinessProfileAndIsDeletedFalse(business)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EnergyUsageResponse toResponse(EnergyUsageRecord record) {
        return new EnergyUsageResponse(
                record.getId(),
                record.getBusinessProfile().getId(),
                record.getBusinessProfile().getBusinessName(),
                record.getSourceType(),
                record.getHoursUsed(),
                record.getEstimatedKwh(),
                record.getCostNaira(),
                record.getTimestamp(),
                record.getCreatedAt()
        );
    }
}