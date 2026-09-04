package com.financeos.financeosbackend.income.dto;

import java.math.BigDecimal;

public class IncomeCrossModuleResponse {

    private BigDecimal currentMonthIncome;
    private BigDecimal currentMonthExpense;

    private BigDecimal incomeChangePercentage;
    private BigDecimal expenseChangePercentage;

    private BigDecimal currentMonthSurplus;

    private String relationship;
    private String insight;

    public BigDecimal getCurrentMonthIncome() {
        return currentMonthIncome;
    }

    public void setCurrentMonthIncome(BigDecimal currentMonthIncome) {
        this.currentMonthIncome = currentMonthIncome;
    }

    public BigDecimal getCurrentMonthExpense() {
        return currentMonthExpense;
    }

    public void setCurrentMonthExpense(BigDecimal currentMonthExpense) {
        this.currentMonthExpense = currentMonthExpense;
    }

    public BigDecimal getIncomeChangePercentage() {
        return incomeChangePercentage;
    }

    public void setIncomeChangePercentage(BigDecimal incomeChangePercentage) {
        this.incomeChangePercentage = incomeChangePercentage;
    }

    public BigDecimal getExpenseChangePercentage() {
        return expenseChangePercentage;
    }

    public void setExpenseChangePercentage(BigDecimal expenseChangePercentage) {
        this.expenseChangePercentage = expenseChangePercentage;
    }

    public BigDecimal getCurrentMonthSurplus() {
        return currentMonthSurplus;
    }

    public void setCurrentMonthSurplus(BigDecimal currentMonthSurplus) {
        this.currentMonthSurplus = currentMonthSurplus;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }
}