package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class InvestmentInsightResponse {

    private String investmentType;
    private BigDecimal amount;
    private BigDecimal percentage;
    private String status;
    private String recommendation;

    public InvestmentInsightResponse() {
    }

    public InvestmentInsightResponse(
            String investmentType,
            BigDecimal amount,
            BigDecimal percentage,
            String status,
            String recommendation) {

        this.investmentType = investmentType;
        this.amount = amount;
        this.percentage = percentage;
        this.status = status;
        this.recommendation = recommendation;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}