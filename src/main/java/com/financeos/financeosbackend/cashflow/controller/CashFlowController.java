package com.financeos.financeosbackend.cashflow.controller;

import com.financeos.financeosbackend.cashflow.dto.CashFlowResponse;
import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v2/cash-flow")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CashFlowResponse>> getCashFlow() {

        BigDecimal inflows =
                cashFlowService.calculateIncludedInflows();

        BigDecimal outflows =
                cashFlowService.calculateIncludedOutflows();

        BigDecimal netCashFlow =
                cashFlowService.calculateNetCashFlow();

        CashFlowResponse response =
                new CashFlowResponse(
                        inflows,
                        outflows,
                        netCashFlow
                );

        return ResponseBuilder.success(
                "Cash flow retrieved successfully",
                response
        );
    }
}