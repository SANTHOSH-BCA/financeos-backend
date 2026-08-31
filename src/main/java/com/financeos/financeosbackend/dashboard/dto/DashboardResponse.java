package com.financeos.financeosbackend.dashboard.dto;

import java.math.BigDecimal;

public class DashboardResponse {

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal netSavings;

    private BigDecimal totalNetWorth;

    private BigDecimal totalInvestments;

    private Long totalTransactions;

    private Long goalCount;

    private Long expenseCount;

    private Long incomeCount;

    private Long investmentCount;

    public DashboardResponse() {
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

    public BigDecimal getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(BigDecimal netSavings) {
        this.netSavings = netSavings;
    }

    public BigDecimal getTotalNetWorth() {
        return totalNetWorth;
    }

    public void setTotalNetWorth(BigDecimal totalNetWorth) {
        this.totalNetWorth = totalNetWorth;
    }

    public BigDecimal getTotalInvestments() {
        return totalInvestments;
    }

    public void setTotalInvestments(BigDecimal totalInvestments) {
        this.totalInvestments = totalInvestments;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(Long goalCount) {
        this.goalCount = goalCount;
    }

    public Long getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Long expenseCount) {
        this.expenseCount = expenseCount;
    }

    public Long getIncomeCount() {
        return incomeCount;
    }

    public void setIncomeCount(Long incomeCount) {
        this.incomeCount = incomeCount;
    }

    public Long getInvestmentCount() {
        return investmentCount;
    }

    public void setInvestmentCount(Long investmentCount) {
        this.investmentCount = investmentCount;
    }
}