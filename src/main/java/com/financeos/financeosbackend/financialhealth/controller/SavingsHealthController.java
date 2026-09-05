package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthSavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class SavingsHealthController {

    private final FinancialHealthSavingsService financialHealthSavingsService;

    public SavingsHealthController(
            FinancialHealthSavingsService financialHealthSavingsService
    ) {
        this.financialHealthSavingsService = financialHealthSavingsService;
    }

    @GetMapping("/savings")
    public ResponseEntity<ApiResponse<SavingsHealthResponse>> getSavingsHealth() {

        SavingsHealthResponse response =
                financialHealthSavingsService.calculateSavingsHealth();

        return ResponseBuilder.success(
                "Savings health retrieved successfully",
                response
        );
    }
}