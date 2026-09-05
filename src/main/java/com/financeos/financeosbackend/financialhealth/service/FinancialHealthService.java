package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialHealthService {

    private final CashFlowService cashFlowService;

    public FinancialHealthService(
            CashFlowService cashFlowService
    ) {
        this.cashFlowService = cashFlowService;
    }

    public CashFlowHealthResponse calculateCashFlowHealth() {

        BigDecimal inflows =
                cashFlowService.calculateInflows();

        BigDecimal outflows =
                cashFlowService.calculateOutflows();

        BigDecimal netCashFlow =
                cashFlowService.calculateNetCashFlow();

        BigDecimal savingsRate =
                cashFlowService.calculateSavingsRate();

        String status;

        if (netCashFlow.compareTo(BigDecimal.ZERO) > 0) {
            status = "POSITIVE";
        } else if (netCashFlow.compareTo(BigDecimal.ZERO) == 0) {
            status = "NEUTRAL";
        } else {
            status = "NEGATIVE";
        }

        return new CashFlowHealthResponse(
                inflows,
                outflows,
                netCashFlow,
                savingsRate,
                status
        );
    }
}