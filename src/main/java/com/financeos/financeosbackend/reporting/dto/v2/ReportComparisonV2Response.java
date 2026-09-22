package com.financeos.financeosbackend.reporting.dto.v2;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;

public class ReportComparisonV2Response {

    private boolean comparisonAvailable;

    private ReportPeriodResponse currentPeriod;
    private ReportPeriodResponse previousPeriod;

    private ReportMetricChangeV2Response income;
    private ReportMetricChangeV2Response expenses;
    private ReportMetricChangeV2Response savings;
    private ReportMetricChangeV2Response investments;
    private ReportMetricChangeV2Response netWorth;

    public ReportComparisonV2Response() {
    }

    public boolean isComparisonAvailable() {
        return comparisonAvailable;
    }

    public void setComparisonAvailable(boolean comparisonAvailable) {
        this.comparisonAvailable = comparisonAvailable;
    }

    public ReportPeriodResponse getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(ReportPeriodResponse currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public ReportPeriodResponse getPreviousPeriod() {
        return previousPeriod;
    }

    public void setPreviousPeriod(ReportPeriodResponse previousPeriod) {
        this.previousPeriod = previousPeriod;
    }

    public ReportMetricChangeV2Response getIncome() {
        return income;
    }

    public void setIncome(ReportMetricChangeV2Response income) {
        this.income = income;
    }

    public ReportMetricChangeV2Response getExpenses() {
        return expenses;
    }

    public void setExpenses(ReportMetricChangeV2Response expenses) {
        this.expenses = expenses;
    }

    public ReportMetricChangeV2Response getSavings() {
        return savings;
    }

    public void setSavings(ReportMetricChangeV2Response savings) {
        this.savings = savings;
    }

    public ReportMetricChangeV2Response getInvestments() {
        return investments;
    }

    public void setInvestments(
            ReportMetricChangeV2Response investments
    ) {
        this.investments = investments;
    }

    public ReportMetricChangeV2Response getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(
            ReportMetricChangeV2Response netWorth
    ) {
        this.netWorth = netWorth;
    }
}