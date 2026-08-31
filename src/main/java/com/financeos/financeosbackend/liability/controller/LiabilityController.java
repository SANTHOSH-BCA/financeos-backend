package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.service.LiabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/liabilities")
public class LiabilityController {

    private final LiabilityService liabilityService;

    public LiabilityController(LiabilityService liabilityService) {
        this.liabilityService = liabilityService;
    }

    @PostMapping
    public ResponseEntity<LiabilityResponse> createLiability(
            @Valid @RequestBody CreateLiabilityRequest request
    ) {

        LiabilityResponse response =
                liabilityService.createLiability(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<LiabilityResponse>> getMyLiabilities() {

        return ResponseEntity.ok(
                liabilityService.getMyLiabilities()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiabilityResponse> getMyLiability(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                liabilityService.getMyLiability(id)
        );
    }
}