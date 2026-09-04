package com.financeos.financeosbackend.financialposition.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialposition.dto.FinancialPositionResponse;
import com.financeos.financeosbackend.financialposition.service.FinancialPositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-position")
public class FinancialPositionController {

    private final FinancialPositionService financialPositionService;

    public FinancialPositionController(
            FinancialPositionService financialPositionService
    ) {
        this.financialPositionService = financialPositionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FinancialPositionResponse>>
    getFinancialPosition() {

        FinancialPositionResponse response =
                new FinancialPositionResponse(
                        financialPositionService.calculateNetWorth(),
                        financialPositionService.calculateNetCashFlow(),
                        financialPositionService.calculateInvestmentValue(),
                        financialPositionService.calculateDebtValue(),
                        financialPositionService.calculateLiquidAssets(),
                        financialPositionService.calculateAssetAllocation(),
                        financialPositionService.calculateSavings(),
                        financialPositionService.calculateSavingsRate()
                );

        return ResponseBuilder.success(
                "Financial position retrieved successfully",
                response
        );
    }
}