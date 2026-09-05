package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.OverallFinancialHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.OverallFinancialHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class OverallFinancialHealthController {

    private final OverallFinancialHealthService overallFinancialHealthService;

    public OverallFinancialHealthController(
            OverallFinancialHealthService overallFinancialHealthService
    ) {
        this.overallFinancialHealthService = overallFinancialHealthService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OverallFinancialHealthResponse>> getOverallFinancialHealth() {

        OverallFinancialHealthResponse response =
                overallFinancialHealthService.calculateOverallFinancialHealth();

        return ResponseBuilder.success(
                "Overall financial health retrieved successfully",
                response
        );
    }
}