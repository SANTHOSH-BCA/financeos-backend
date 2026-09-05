package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthInvestmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class InvestmentHealthController {

    private final FinancialHealthInvestmentService financialHealthInvestmentService;

    public InvestmentHealthController(
            FinancialHealthInvestmentService financialHealthInvestmentService
    ) {
        this.financialHealthInvestmentService = financialHealthInvestmentService;
    }

    @GetMapping("/investment")
    public ResponseEntity<ApiResponse<InvestmentHealthResponse>> getInvestmentHealth() {

        InvestmentHealthResponse response =
                financialHealthInvestmentService.calculateInvestmentHealth();

        return ResponseBuilder.success(
                "Investment health retrieved successfully",
                response
        );
    }
}