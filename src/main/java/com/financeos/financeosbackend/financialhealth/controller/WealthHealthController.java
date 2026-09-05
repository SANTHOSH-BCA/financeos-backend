package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthWealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class WealthHealthController {

    private final FinancialHealthWealthService financialHealthWealthService;

    public WealthHealthController(
            FinancialHealthWealthService financialHealthWealthService
    ) {
        this.financialHealthWealthService = financialHealthWealthService;
    }

    @GetMapping("/wealth")
    public ResponseEntity<ApiResponse<WealthHealthResponse>> getWealthHealth() {

        WealthHealthResponse response =
                financialHealthWealthService.calculateWealthHealth();

        return ResponseBuilder.success(
                "Wealth health retrieved successfully",
                response
        );
    }
}