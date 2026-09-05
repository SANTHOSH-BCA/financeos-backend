package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialHealthSavingsService {

    private final CashFlowService cashFlowService;

    public FinancialHealthSavingsService(
            CashFlowService cashFlowService
    ) {
        this.cashFlowService = cashFlowService;
    }

    public SavingsHealthResponse calculateSavingsHealth() {

        BigDecimal savings =
                cashFlowService.calculateSavings();

        BigDecimal savingsRate =
                cashFlowService.calculateSavingsRate();

        String status;

        if (savings.compareTo(BigDecimal.ZERO) > 0) {
            status = "POSITIVE";
        } else if (savings.compareTo(BigDecimal.ZERO) == 0) {
            status = "NEUTRAL";
        } else {
            status = "NEGATIVE";
        }

        return new SavingsHealthResponse(
                savings,
                savingsRate,
                status
        );
    }
}