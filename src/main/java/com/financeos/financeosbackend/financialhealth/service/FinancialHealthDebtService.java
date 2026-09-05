package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialHealthDebtService {

    private final NetWorthService netWorthService;

    public FinancialHealthDebtService(
            NetWorthService netWorthService
    ) {
        this.netWorthService = netWorthService;
    }

    public DebtHealthResponse calculateDebtHealth() {

        BigDecimal recognizedLiabilities =
                netWorthService.calculateRecognizedLiabilities();

        String status;

        if (recognizedLiabilities.compareTo(BigDecimal.ZERO) == 0) {
            status = "NO_DEBT";
        } else {
            status = "DEBT_PRESENT";
        }

        return new DebtHealthResponse(
                recognizedLiabilities,
                status
        );
    }
}