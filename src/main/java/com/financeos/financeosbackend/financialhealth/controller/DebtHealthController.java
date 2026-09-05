package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthDebtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class DebtHealthController {

    private final FinancialHealthDebtService financialHealthDebtService;

    public DebtHealthController(
            FinancialHealthDebtService financialHealthDebtService
    ) {
        this.financialHealthDebtService = financialHealthDebtService;
    }

    @GetMapping("/debt")
    public ResponseEntity<ApiResponse<DebtHealthResponse>> getDebtHealth() {

        DebtHealthResponse response =
                financialHealthDebtService.calculateDebtHealth();

        return ResponseBuilder.success(
                "Debt health retrieved successfully",
                response
        );
    }
}