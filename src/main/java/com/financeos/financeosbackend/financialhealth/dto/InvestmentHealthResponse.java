package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class InvestmentHealthResponse {

    private BigDecimal totalInvestedAmount;
    private BigDecimal currentPortfolioValue;
    private BigDecimal totalProfitLoss;
    private BigDecimal returnPercentage;
    private String status;

    public InvestmentHealthResponse() {
    }

    public InvestmentHealthResponse(
            BigDecimal totalInvestedAmount,
            BigDecimal currentPortfolioValue,
            BigDecimal totalProfitLoss,
            BigDecimal returnPercentage,
            String status
    ) {
        this.totalInvestedAmount = totalInvestedAmount;
        this.currentPortfolioValue = currentPortfolioValue;
        this.totalProfitLoss = totalProfitLoss;
        this.returnPercentage = returnPercentage;
        this.status = status;
    }

    public BigDecimal getTotalInvestedAmount() {
        return totalInvestedAmount;
    }

    public void setTotalInvestedAmount(BigDecimal totalInvestedAmount) {
        this.totalInvestedAmount = totalInvestedAmount;
    }

    public BigDecimal getCurrentPortfolioValue() {
        return currentPortfolioValue;
    }

    public void setCurrentPortfolioValue(BigDecimal currentPortfolioValue) {
        this.currentPortfolioValue = currentPortfolioValue;
    }

    public BigDecimal getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public void setTotalProfitLoss(BigDecimal totalProfitLoss) {
        this.totalProfitLoss = totalProfitLoss;
    }

    public BigDecimal getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(BigDecimal returnPercentage) {
        this.returnPercentage = returnPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}