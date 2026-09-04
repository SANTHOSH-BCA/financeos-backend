package com.financeos.financeosbackend.income.dto;

import java.math.BigDecimal;

public class MonthlyIncomeResponse {

    private String month;

    private BigDecimal totalIncome;

    public MonthlyIncomeResponse() {
    }

    public MonthlyIncomeResponse(
            String month,
            BigDecimal totalIncome) {

        this.month = month;
        this.totalIncome = totalIncome;
    }

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
}