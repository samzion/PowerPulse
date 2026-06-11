package com.powerpulse.controllers;

import com.powerpulse.models.enums.BusinessType;
import com.powerpulse.models.enums.LagosZone;
import com.powerpulse.models.requests.BusinessProfileRequest;
import com.powerpulse.models.responses.BusinessProfileResponse;
import com.powerpulse.services.BusinessProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessProfileController {

    private final BusinessProfileService service;

    @PostMapping
    public ResponseEntity<BusinessProfileResponse> create(
            @Valid @RequestBody BusinessProfileRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BusinessProfileResponse>> getAll(
            @RequestParam(required = false) BusinessType type,
            @RequestParam(required = false) LagosZone location,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.getAll(type, location, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessProfileResponse> getById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}