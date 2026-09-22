package com.financeos.financeosbackend.reporting.dto.v2;

public class FinancialHealthReportV2Response {

    private ReportSectionMetadata metadata;

    private String cashFlowHealth;
    private String debtHealth;
    private String savingsHealth;
    private String investmentHealth;
    private String goalHealth;
    private String wealthHealth;
    private String overallStatus;

    public FinancialHealthReportV2Response() {
    }

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
    }

    public String getCashFlowHealth() {
        return cashFlowHealth;
    }

    public void setCashFlowHealth(String cashFlowHealth) {
        this.cashFlowHealth = cashFlowHealth;
    }

    public String getDebtHealth() {
        return debtHealth;
    }

    public void setDebtHealth(String debtHealth) {
        this.debtHealth = debtHealth;
    }

    public String getSavingsHealth() {
        return savingsHealth;
    }

    public void setSavingsHealth(String savingsHealth) {
        this.savingsHealth = savingsHealth;
    }

    public String getInvestmentHealth() {
        return investmentHealth;
    }

    public void setInvestmentHealth(String investmentHealth) {
        this.investmentHealth = investmentHealth;
    }

    public String getGoalHealth() {
        return goalHealth;
    }

    public void setGoalHealth(String goalHealth) {
        this.goalHealth = goalHealth;
    }

    public String getWealthHealth() {
        return wealthHealth;
    }

    public void setWealthHealth(String wealthHealth) {
        this.wealthHealth = wealthHealth;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }
}