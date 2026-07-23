package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class MonthlyFinancialSummaryResponse {

    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal savings;
    private BigDecimal savingsRate;
    private String financialHealth;
    private String summary;

    public MonthlyFinancialSummaryResponse() {
    }

    public MonthlyFinancialSummaryResponse(
            BigDecimal income,
            BigDecimal expense,
            BigDecimal savings,
            BigDecimal savingsRate,
            String financialHealth,
            String summary) {

        this.income = income;
        this.expense = expense;
        this.savings = savings;
        this.savingsRate = savingsRate;
        this.financialHealth = financialHealth;
        this.summary = summary;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }

    public String getFinancialHealth() {
        return financialHealth;
    }

    public void setFinancialHealth(String financialHealth) {
        this.financialHealth = financialHealth;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}