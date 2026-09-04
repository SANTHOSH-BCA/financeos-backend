package com.financeos.financeosbackend.investment.dto;

import java.math.BigDecimal;

public class InvestmentPerformanceResponse {

    private BigDecimal totalInvestedAmount;

    private BigDecimal currentPortfolioValue;

    private BigDecimal totalProfitLoss;

    private BigDecimal returnPercentage;

    private Long investmentCount;

    public InvestmentPerformanceResponse() {
    }

    public InvestmentPerformanceResponse(
            BigDecimal totalInvestedAmount,
            BigDecimal currentPortfolioValue,
            BigDecimal totalProfitLoss,
            BigDecimal returnPercentage,
            Long investmentCount) {

        this.totalInvestedAmount = totalInvestedAmount;
        this.currentPortfolioValue = currentPortfolioValue;
        this.totalProfitLoss = totalProfitLoss;
        this.returnPercentage = returnPercentage;
        this.investmentCount = investmentCount;
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

    public Long getInvestmentCount() {
        return investmentCount;
    }

    public void setInvestmentCount(Long investmentCount) {
        this.investmentCount = investmentCount;
    }
}