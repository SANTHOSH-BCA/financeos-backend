package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class FinancialHealthController {

    private final FinancialHealthService financialHealthService;

    public FinancialHealthController(
            FinancialHealthService financialHealthService
    ) {
        this.financialHealthService = financialHealthService;
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<ApiResponse<CashFlowHealthResponse>> getCashFlowHealth() {

        CashFlowHealthResponse response =
                financialHealthService.calculateCashFlowHealth();

        return ResponseBuilder.success(
                "Cash-flow health retrieved successfully",
                response
        );
    }
}