package com.powerpulse.controllers;

import com.powerpulse.models.requests.EnergyUsageRequest;
import com.powerpulse.models.responses.EnergyUsageResponse;
import com.powerpulse.services.EnergyUsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/usage")
@RequiredArgsConstructor
public class EnergyUsageController {

    private final EnergyUsageService service;

    @PostMapping
    public ResponseEntity<EnergyUsageResponse> logUsage(
            @Valid @RequestBody EnergyUsageRequest request) {
        return ResponseEntity.status(201).body(service.logUsage(request));
    }

    @GetMapping
    public ResponseEntity<List<EnergyUsageResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<EnergyUsageResponse>> getByBusiness(
            @PathVariable UUID businessId) {
        return ResponseEntity.ok(service.getByBusiness(businessId));
    }
}