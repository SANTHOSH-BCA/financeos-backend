package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class InvestmentDistributionResponse {

    private String investmentType;
    private BigDecimal amount;
    private BigDecimal percentage;

    public InvestmentDistributionResponse() {
    }

    public InvestmentDistributionResponse(
            String investmentType,
            BigDecimal amount,
            BigDecimal percentage) {

        this.investmentType = investmentType;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}