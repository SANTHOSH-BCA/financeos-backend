package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialHealthInvestmentService {

    private final InvestmentService investmentService;

    public FinancialHealthInvestmentService(
            InvestmentService investmentService
    ) {
        this.investmentService = investmentService;
    }

    public InvestmentHealthResponse calculateInvestmentHealth() {

        InvestmentPerformanceResponse performance =
                investmentService.getPortfolioPerformance();

        BigDecimal totalInvestedAmount =
                performance.getTotalInvestedAmount();

        BigDecimal currentPortfolioValue =
                performance.getCurrentPortfolioValue();

        BigDecimal totalProfitLoss =
                performance.getTotalProfitLoss();

        BigDecimal returnPercentage =
                performance.getReturnPercentage();

        String status;

        if (totalInvestedAmount.compareTo(BigDecimal.ZERO) == 0) {
            status = "NO_INVESTMENTS";
        } else if (totalProfitLoss.compareTo(BigDecimal.ZERO) > 0) {
            status = "POSITIVE";
        } else if (totalProfitLoss.compareTo(BigDecimal.ZERO) == 0) {
            status = "NEUTRAL";
        } else {
            status = "NEGATIVE";
        }

        return new InvestmentHealthResponse(
                totalInvestedAmount,
                currentPortfolioValue,
                totalProfitLoss,
                returnPercentage,
                status
        );
    }
}