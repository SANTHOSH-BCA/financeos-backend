package com.financeos.financeosbackend.reporting.dto.v2;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class FinancialReportV2Response {

    private Long reportId;
    private ReportPeriodResponse reportPeriod;
    private LocalDateTime generatedAt;
    private String reportStatus;

    private IncomeReportV2Response income;
    private ExpenseReportV2Response expenses;
    private CashFlowReportV2Response cashFlow;
    private InvestmentReportV2Response investments;
    private GoalReportV2Response goals;
    private AssetReportV2Response assets;
    private LiabilityReportV2Response liabilities;
    private NetWorthReportV2Response netWorth;
    private FinancialHealthReportV2Response financialHealth;
    private ReportComparisonV2Response comparison;

    public FinancialReportV2Response() {
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public ReportPeriodResponse getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(ReportPeriodResponse reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public IncomeReportV2Response getIncome() {
        return income;
    }

    public void setIncome(IncomeReportV2Response income) {
        this.income = income;
    }

    public ExpenseReportV2Response getExpenses() {
        return expenses;
    }

    public void setExpenses(ExpenseReportV2Response expenses) {
        this.expenses = expenses;
    }

    public CashFlowReportV2Response getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(CashFlowReportV2Response cashFlow) {
        this.cashFlow = cashFlow;
    }

    public InvestmentReportV2Response getInvestments() {
        return investments;
    }

    public void setInvestments(InvestmentReportV2Response investments) {
        this.investments = investments;
    }

    public GoalReportV2Response getGoals() {
        return goals;
    }

    public void setGoals(GoalReportV2Response goals) {
        this.goals = goals;
    }

    public AssetReportV2Response getAssets() {
        return assets;
    }

    public void setAssets(AssetReportV2Response assets) {
        this.assets = assets;
    }

    public LiabilityReportV2Response getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(LiabilityReportV2Response liabilities) {
        this.liabilities = liabilities;
    }

    public NetWorthReportV2Response getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(NetWorthReportV2Response netWorth) {
        this.netWorth = netWorth;
    }

    public FinancialHealthReportV2Response getFinancialHealth() {
        return financialHealth;
    }

    public void setFinancialHealth(
            FinancialHealthReportV2Response financialHealth
    ) {
        this.financialHealth = financialHealth;
    }

    public ReportComparisonV2Response getComparison() {
        return comparison;
    }

    public void setComparison(
            ReportComparisonV2Response comparison
    ) {
        this.comparison = comparison;
    }

    private List<ReportChangeInsightV2Response> changes =
            new ArrayList<>();

    public List<ReportChangeInsightV2Response> getChanges() {
        return changes;
    }

    public void setChanges(
            List<ReportChangeInsightV2Response> changes
    ) {
        this.changes = changes;
    }
}