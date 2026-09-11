package com.financeos.financeosbackend.analytics.dto;

import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.OverallFinancialHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;

public class AnalyticsFinancialHealthV2Response {

    private CashFlowHealthResponse cashFlow;
    private DebtHealthResponse debt;
    private SavingsHealthResponse savings;
    private InvestmentHealthResponse investments;
    private GoalHealthResponse goals;
    private WealthHealthResponse wealth;
    private OverallFinancialHealthResponse overall;

    public AnalyticsFinancialHealthV2Response() {
    }

    public AnalyticsFinancialHealthV2Response(
            CashFlowHealthResponse cashFlow,
            DebtHealthResponse debt,
            SavingsHealthResponse savings,
            InvestmentHealthResponse investments,
            GoalHealthResponse goals,
            WealthHealthResponse wealth,
            OverallFinancialHealthResponse overall
    ) {
        this.cashFlow = cashFlow;
        this.debt = debt;
        this.savings = savings;
        this.investments = investments;
        this.goals = goals;
        this.wealth = wealth;
        this.overall = overall;
    }

    public CashFlowHealthResponse getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(CashFlowHealthResponse cashFlow) {
        this.cashFlow = cashFlow;
    }

    public DebtHealthResponse getDebt() {
        return debt;
    }

    public void setDebt(DebtHealthResponse debt) {
        this.debt = debt;
    }

    public SavingsHealthResponse getSavings() {
        return savings;
    }

    public void setSavings(SavingsHealthResponse savings) {
        this.savings = savings;
    }

    public InvestmentHealthResponse getInvestments() {
        return investments;
    }

    public void setInvestments(InvestmentHealthResponse investments) {
        this.investments = investments;
    }

    public GoalHealthResponse getGoals() {
        return goals;
    }

    public void setGoals(GoalHealthResponse goals) {
        this.goals = goals;
    }

    public WealthHealthResponse getWealth() {
        return wealth;
    }

    public void setWealth(WealthHealthResponse wealth) {
        this.wealth = wealth;
    }

    public OverallFinancialHealthResponse getOverall() {
        return overall;
    }

    public void setOverall(OverallFinancialHealthResponse overall) {
        this.overall = overall;
    }
}