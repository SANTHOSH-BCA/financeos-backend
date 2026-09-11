package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.OverallFinancialHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goalintelligence.service.GoalIntelligenceService;
import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FinancialHealthService {

    private final CashFlowService cashFlowService;
    private final NetWorthService netWorthService;
    private final InvestmentService investmentService;
    private final GoalIntelligenceService goalIntelligenceService;

    public FinancialHealthService(
            CashFlowService cashFlowService,
            NetWorthService netWorthService,
            InvestmentService investmentService,
            GoalIntelligenceService goalIntelligenceService
    ) {
        this.cashFlowService = cashFlowService;
        this.netWorthService = netWorthService;
        this.investmentService = investmentService;
        this.goalIntelligenceService = goalIntelligenceService;
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

    public DebtHealthResponse calculateDebtHealth() {

        BigDecimal recognizedLiabilities =
                netWorthService.calculateRecognizedLiabilities();

        String status =
                recognizedLiabilities.compareTo(BigDecimal.ZERO) == 0
                        ? "NO_DEBT"
                        : "DEBT_PRESENT";

        return new DebtHealthResponse(
                recognizedLiabilities,
                status
        );
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

    public GoalHealthResponse calculateGoalHealth() {

        List<Goal> goals =
                goalIntelligenceService.getMyGoals();

        long totalGoals = goals.size();
        long completedGoals = 0;
        long atRiskGoals = 0;
        long onTrackGoals = 0;

        BigDecimal totalTargetAmount = BigDecimal.ZERO;
        BigDecimal totalCurrentAmount = BigDecimal.ZERO;

        for (Goal goal : goals) {

            GoalStatus status =
                    goalIntelligenceService.calculateGoalStatus(goal);

            if (status == GoalStatus.COMPLETED) {
                completedGoals++;
            } else if (status == GoalStatus.AT_RISK) {
                atRiskGoals++;
            } else if (status == GoalStatus.ON_TRACK) {
                onTrackGoals++;
            }

            totalTargetAmount =
                    totalTargetAmount.add(goal.getTargetAmount());

            totalCurrentAmount =
                    totalCurrentAmount.add(goal.getCurrentAmount());
        }

        String status;

        if (totalGoals == 0) {
            status = "NO_GOALS";
        } else if (atRiskGoals > 0) {
            status = "AT_RISK";
        } else if (completedGoals == totalGoals) {
            status = "COMPLETED";
        } else {
            status = "ON_TRACK";
        }

        return new GoalHealthResponse(
                totalGoals,
                completedGoals,
                atRiskGoals,
                onTrackGoals,
                totalTargetAmount,
                totalCurrentAmount,
                status
        );
    }

    public WealthHealthResponse calculateWealthHealth() {

        BigDecimal recognizedAssets =
                netWorthService.calculateRecognizedAssets();

        BigDecimal recognizedLiabilities =
                netWorthService.calculateRecognizedLiabilities();

        BigDecimal netWorth =
                recognizedAssets.subtract(recognizedLiabilities);

        String status;

        if (netWorth.compareTo(BigDecimal.ZERO) > 0) {
            status = "POSITIVE";
        } else if (netWorth.compareTo(BigDecimal.ZERO) == 0) {
            status = "NEUTRAL";
        } else {
            status = "NEGATIVE";
        }

        return new WealthHealthResponse(
                recognizedAssets,
                recognizedLiabilities,
                netWorth,
                status
        );
    }

    public OverallFinancialHealthResponse calculateOverallFinancialHealth() {

        CashFlowHealthResponse cashFlowHealth =
                calculateCashFlowHealth();

        DebtHealthResponse debtHealth =
                calculateDebtHealth();

        SavingsHealthResponse savingsHealth =
                calculateSavingsHealth();

        InvestmentHealthResponse investmentHealth =
                calculateInvestmentHealth();

        GoalHealthResponse goalHealth =
                calculateGoalHealth();

        WealthHealthResponse wealthHealth =
                calculateWealthHealth();

        String cashFlowStatus =
                cashFlowHealth.getStatus();

        String debtStatus =
                debtHealth.getStatus();

        String savingsStatus =
                savingsHealth.getStatus();

        String investmentStatus =
                investmentHealth.getStatus();

        String goalStatus =
                goalHealth.getStatus();

        String wealthStatus =
                wealthHealth.getStatus();

        String overallStatus;

        if ("NEGATIVE".equals(cashFlowStatus)
                || "NEGATIVE".equals(savingsStatus)
                || "NEGATIVE".equals(wealthStatus)
                || "AT_RISK".equals(goalStatus)) {

            overallStatus = "NEEDS_ATTENTION";

        } else if ("POSITIVE".equals(cashFlowStatus)
                && ("NO_DEBT".equals(debtStatus)
                || "DEBT_PRESENT".equals(debtStatus))
                && ("POSITIVE".equals(savingsStatus)
                || "NEUTRAL".equals(savingsStatus))
                && ("POSITIVE".equals(wealthStatus)
                || "NEUTRAL".equals(wealthStatus))) {

            overallStatus = "HEALTHY";

        } else {
            overallStatus = "STABLE";
        }

        return new OverallFinancialHealthResponse(
                cashFlowStatus,
                debtStatus,
                savingsStatus,
                investmentStatus,
                goalStatus,
                wealthStatus,
                overallStatus
        );
    }
}