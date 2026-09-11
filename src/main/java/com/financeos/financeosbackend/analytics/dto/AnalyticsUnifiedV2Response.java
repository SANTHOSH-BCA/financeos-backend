package com.financeos.financeosbackend.analytics.dto;

public class AnalyticsUnifiedV2Response {

    private final AnalyticsFinancialPositionV2Response financialPosition;
    private final AnalyticsCashFlowV2Response cashFlow;
    private final AnalyticsNetWorthV2Response netWorth;
    private final AnalyticsInvestmentV2Response investments;
    private final AnalyticsGoalV2Response goals;
    private final AnalyticsFinancialHealthV2Response financialHealth;
    private final AnalyticsFinancialProfileV2Response financialProfile;

    public AnalyticsUnifiedV2Response(
            AnalyticsFinancialPositionV2Response financialPosition,
            AnalyticsCashFlowV2Response cashFlow,
            AnalyticsNetWorthV2Response netWorth,
            AnalyticsInvestmentV2Response investments,
            AnalyticsGoalV2Response goals,
            AnalyticsFinancialHealthV2Response financialHealth,
            AnalyticsFinancialProfileV2Response financialProfile
    ) {
        this.financialPosition = financialPosition;
        this.cashFlow = cashFlow;
        this.netWorth = netWorth;
        this.investments = investments;
        this.goals = goals;
        this.financialHealth = financialHealth;
        this.financialProfile = financialProfile;
    }

    public AnalyticsFinancialPositionV2Response getFinancialPosition() {
        return financialPosition;
    }

    public AnalyticsCashFlowV2Response getCashFlow() {
        return cashFlow;
    }

    public AnalyticsNetWorthV2Response getNetWorth() {
        return netWorth;
    }

    public AnalyticsInvestmentV2Response getInvestments() {
        return investments;
    }

    public AnalyticsGoalV2Response getGoals() {
        return goals;
    }

    public AnalyticsFinancialHealthV2Response getFinancialHealth() {
        return financialHealth;
    }

    public AnalyticsFinancialProfileV2Response getFinancialProfile() {
        return financialProfile;
    }
}