package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.OverallFinancialHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import org.springframework.stereotype.Service;

@Service
public class OverallFinancialHealthService {

    private final FinancialHealthService financialHealthService;
    private final FinancialHealthDebtService financialHealthDebtService;
    private final FinancialHealthSavingsService financialHealthSavingsService;
    private final FinancialHealthInvestmentService financialHealthInvestmentService;
    private final FinancialHealthGoalService financialHealthGoalService;
    private final FinancialHealthWealthService financialHealthWealthService;

    public OverallFinancialHealthService(
            FinancialHealthService financialHealthService,
            FinancialHealthDebtService financialHealthDebtService,
            FinancialHealthSavingsService financialHealthSavingsService,
            FinancialHealthInvestmentService financialHealthInvestmentService,
            FinancialHealthGoalService financialHealthGoalService,
            FinancialHealthWealthService financialHealthWealthService
    ) {
        this.financialHealthService = financialHealthService;
        this.financialHealthDebtService = financialHealthDebtService;
        this.financialHealthSavingsService = financialHealthSavingsService;
        this.financialHealthInvestmentService = financialHealthInvestmentService;
        this.financialHealthGoalService = financialHealthGoalService;
        this.financialHealthWealthService = financialHealthWealthService;
    }

    public OverallFinancialHealthResponse calculateOverallFinancialHealth() {

        CashFlowHealthResponse cashFlowHealth =
                financialHealthService.calculateCashFlowHealth();

        DebtHealthResponse debtHealth =
                financialHealthDebtService.calculateDebtHealth();

        SavingsHealthResponse savingsHealth =
                financialHealthSavingsService.calculateSavingsHealth();

        InvestmentHealthResponse investmentHealth =
                financialHealthInvestmentService.calculateInvestmentHealth();

        GoalHealthResponse goalHealth =
                financialHealthGoalService.calculateGoalHealth();

        WealthHealthResponse wealthHealth =
                financialHealthWealthService.calculateWealthHealth();

        String overallStatus = determineOverallStatus(
                cashFlowHealth.getStatus(),
                debtHealth.getStatus(),
                savingsHealth.getStatus(),
                investmentHealth.getStatus(),
                goalHealth.getStatus(),
                wealthHealth.getStatus()
        );

        return new OverallFinancialHealthResponse(
                cashFlowHealth.getStatus(),
                debtHealth.getStatus(),
                savingsHealth.getStatus(),
                investmentHealth.getStatus(),
                goalHealth.getStatus(),
                wealthHealth.getStatus(),
                overallStatus
        );
    }

    private String determineOverallStatus(
            String cashFlowStatus,
            String debtStatus,
            String savingsStatus,
            String investmentStatus,
            String goalStatus,
            String wealthStatus
    ) {

        if ("NEGATIVE".equals(cashFlowStatus)
                || "NEGATIVE".equals(savingsStatus)
                || "NEGATIVE".equals(wealthStatus)
                || "AT_RISK".equals(goalStatus)) {
            return "NEEDS_ATTENTION";
        }

        if ("POSITIVE".equals(cashFlowStatus)
                && ("NO_DEBT".equals(debtStatus)
                || "DEBT_PRESENT".equals(debtStatus))
                && ("POSITIVE".equals(savingsStatus)
                || "NEUTRAL".equals(savingsStatus))
                && ("POSITIVE".equals(wealthStatus)
                || "NEUTRAL".equals(wealthStatus))) {
            return "HEALTHY";
        }

        return "STABLE";
    }
}