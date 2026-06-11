package com.powerpulse.services;

import com.powerpulse.models.entities.BusinessProfile;
import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import com.powerpulse.models.requests.BusinessProfileRequest;
import com.powerpulse.models.responses.BusinessProfileResponse;
import com.powerpulse.repositories.BusinessProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessProfileService {

    private final BusinessProfileRepository repository;

    @Transactional
    public BusinessProfileResponse create(BusinessProfileRequest request) {
        log.info("Creating business profile: {}", request.businessName());

        if (repository.existsByBusinessNameAndIsDeletedFalse(request.businessName())) {
            throw new IllegalArgumentException(
                    "Business with name '" + request.businessName() + "' already exists"
            );
        }

        BusinessProfile profile = new BusinessProfile();
        profile.setBusinessName(request.businessName());
        profile.setBusinessType(request.businessType());
        profile.setLocation(request.location());

        BusinessProfile saved = repository.save(profile);
        log.info("Business profile created with id: {}", saved.getId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BusinessProfileResponse> getAll() {
        log.info("Fetching all business profiles");
        return repository.findByIsDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessProfileResponse getById(UUID id) {
        log.info("Fetching business profile with id: {}", id);
        return repository.findByIdAndIsDeletedFalse(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Business profile not found with id: " + id
                ));
    }

    @Transactional(readOnly = true)
    public List<BusinessProfileResponse> getByType(BusinessType type) {
        log.info("Fetching business profiles by type: {}", type);
        return repository.findByBusinessTypeAndIsDeletedFalse(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BusinessProfileResponse> getByLocation(LagosZone location) {
        log.info("Fetching business profiles by location: {}", location);
        return repository.findByLocationAndIsDeletedFalse(location)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Soft deleting business profile with id: {}", id);
        BusinessProfile profile = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Business profile not found with id: " + id
                ));
        profile.setDeleted(true);
        profile.setDeletedAt(java.time.LocalDateTime.now());
        repository.save(profile);
        log.info("Business profile soft deleted: {}", id);
    }

    private BusinessProfileResponse toResponse(BusinessProfile profile) {
        return new BusinessProfileResponse(
                profile.getId(),
                profile.getBusinessName(),
                profile.getBusinessType(),
                profile.getLocation(),
                profile.getCreatedAt()
        );
    }
}
