package com.financeos.financeosbackend.reporting.dto;

import java.math.BigDecimal;

public class FinancialReportResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalSavings;
    private BigDecimal netWorth;
    private String financialHealth;

    public FinancialReportResponse() {
    }

    public FinancialReportResponse(
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            BigDecimal totalSavings,
            BigDecimal netWorth,
            String financialHealth) {

        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.totalSavings = totalSavings;
        this.netWorth = netWorth;
        this.financialHealth = financialHealth;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(BigDecimal totalSavings) {
        this.totalSavings = totalSavings;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public String getFinancialHealth() {
        return financialHealth;
    }

    public void setFinancialHealth(String financialHealth) {
        this.financialHealth = financialHealth;
    }
}