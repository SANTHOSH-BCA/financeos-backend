package com.financeos.financeosbackend.financialprofile.controller;

import com.financeos.financeosbackend.financialprofile.dto.CreateFinancialProfileRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialProfileResponse;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;import com.financeos.financeosbackend.financialprofile.dto.PlanningHorizonRequest;
import com.financeos.financeosbackend.financialprofile.dto.PlanningHorizonResponse;import com.financeos.financeosbackend.financialprofile.dto.UpdateFinancialProfileRequest;

@RestController
@RequestMapping("/api/financial-profiles")
public class FinancialProfileController {

    private final FinancialProfileService financialProfileService;

    public FinancialProfileController(
            FinancialProfileService financialProfileService
    ) {
        this.financialProfileService = financialProfileService;
    }

    @PostMapping("/user/{userId}")
    public FinancialProfileResponse createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody CreateFinancialProfileRequest request
    ) {
        return financialProfileService.createProfile(userId, request);
    }

    @GetMapping("/user/{userId}")
    public FinancialProfileResponse getProfile(
            @PathVariable Long userId
    ) {
        return financialProfileService.getProfile(userId);
    }

    @GetMapping("/me")
    public FinancialProfileResponse getMyProfile() {
        return financialProfileService.getMyProfile();
    }

    @PutMapping("/planning-horizon")
    public PlanningHorizonResponse updatePlanningHorizon(
            @Valid @RequestBody PlanningHorizonRequest request
    ) {
        return financialProfileService.updatePlanningHorizon(request);
    }

    @PutMapping("/me")
    public FinancialProfileResponse updateMyProfile(
            @Valid @RequestBody UpdateFinancialProfileRequest request
    ) {
        return financialProfileService.updateMyProfile(request);
    }
}