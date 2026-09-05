package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class SavingsHealthResponse {

    private BigDecimal savings;
    private BigDecimal savingsRate;
    private String status;

    public SavingsHealthResponse() {
    }

    public SavingsHealthResponse(
            BigDecimal savings,
            BigDecimal savingsRate,
            String status
    ) {
        this.savings = savings;
        this.savingsRate = savingsRate;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}