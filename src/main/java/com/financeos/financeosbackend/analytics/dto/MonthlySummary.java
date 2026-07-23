package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class MonthlySummary {

    private String month;
    private BigDecimal totalAmount;

    public MonthlySummary() {
    }

    public MonthlySummary(String month, BigDecimal totalAmount) {
        this.month = month;
        this.totalAmount = totalAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}