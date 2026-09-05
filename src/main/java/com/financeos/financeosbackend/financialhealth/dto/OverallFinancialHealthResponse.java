package com.financeos.financeosbackend.financialhealth.dto;

public class OverallFinancialHealthResponse {

    private String cashFlowStatus;
    private String debtStatus;
    private String savingsStatus;
    private String investmentStatus;
    private String goalStatus;
    private String wealthStatus;
    private String overallStatus;

    public OverallFinancialHealthResponse() {
    }

    public OverallFinancialHealthResponse(
            String cashFlowStatus,
            String debtStatus,
            String savingsStatus,
            String investmentStatus,
            String goalStatus,
            String wealthStatus,
            String overallStatus
    ) {
        this.cashFlowStatus = cashFlowStatus;
        this.debtStatus = debtStatus;
        this.savingsStatus = savingsStatus;
        this.investmentStatus = investmentStatus;
        this.goalStatus = goalStatus;
        this.wealthStatus = wealthStatus;
        this.overallStatus = overallStatus;
    }

    public String getCashFlowStatus() {
        return cashFlowStatus;
    }

    public void setCashFlowStatus(String cashFlowStatus) {
        this.cashFlowStatus = cashFlowStatus;
    }

    public String getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(String debtStatus) {
        this.debtStatus = debtStatus;
    }

    public String getSavingsStatus() {
        return savingsStatus;
    }

    public void setSavingsStatus(String savingsStatus) {
        this.savingsStatus = savingsStatus;
    }

    public String getInvestmentStatus() {
        return investmentStatus;
    }

    public void setInvestmentStatus(String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }

    public String getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(String goalStatus) {
        this.goalStatus = goalStatus;
    }

    public String getWealthStatus() {
        return wealthStatus;
    }

    public void setWealthStatus(String wealthStatus) {
        this.wealthStatus = wealthStatus;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }
}