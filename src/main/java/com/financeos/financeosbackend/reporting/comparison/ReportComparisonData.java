package com.financeos.financeosbackend.reporting.comparison;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;

public class ReportComparisonData {

    private boolean comparisonAvailable;

    private ReportPeriodResponse currentPeriod;
    private ReportPeriodResponse previousPeriod;

    private ReportMetricChangeData income;
    private ReportMetricChangeData expenses;
    private ReportMetricChangeData savings;
    private ReportMetricChangeData investments;
    private ReportMetricChangeData netWorth;

    public ReportComparisonData() {
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

    public ReportMetricChangeData getIncome() {
        return income;
    }

    public void setIncome(ReportMetricChangeData income) {
        this.income = income;
    }

    public ReportMetricChangeData getExpenses() {
        return expenses;
    }

    public void setExpenses(ReportMetricChangeData expenses) {
        this.expenses = expenses;
    }

    public ReportMetricChangeData getSavings() {
        return savings;
    }

    public void setSavings(ReportMetricChangeData savings) {
        this.savings = savings;
    }

    public ReportMetricChangeData getInvestments() {
        return investments;
    }

    public void setInvestments(ReportMetricChangeData investments) {
        this.investments = investments;
    }

    public ReportMetricChangeData getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(ReportMetricChangeData netWorth) {
        this.netWorth = netWorth;
    }
}