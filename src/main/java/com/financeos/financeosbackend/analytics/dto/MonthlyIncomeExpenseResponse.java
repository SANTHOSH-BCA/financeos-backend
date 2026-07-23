package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class MonthlyIncomeExpenseResponse {

    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    public MonthlyIncomeExpenseResponse() {
    }

}