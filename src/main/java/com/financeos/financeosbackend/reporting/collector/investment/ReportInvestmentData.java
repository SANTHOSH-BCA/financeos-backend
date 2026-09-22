package com.financeos.financeosbackend.reporting.collector.investment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportInvestmentData {

    private BigDecimal investedAmount = BigDecimal.ZERO;
    private BigDecimal portfolioValue = BigDecimal.ZERO;
    private BigDecimal profitLoss = BigDecimal.ZERO;
    private BigDecimal returnPercentage = BigDecimal.ZERO;

    private Map<String, BigDecimal> assetAllocation;

    private List<ReportInvestmentHoldingData> holdings = new ArrayList<>();
    private List<ReportInvestmentHistoricalData> historicalPerformance = new ArrayList<>();

    private LocalDate valuationDate;

    private boolean historicalDataAvailable;

    public ReportInvestmentData() {
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(BigDecimal portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
    }

    public BigDecimal getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(BigDecimal returnPercentage) {
        this.returnPercentage = returnPercentage;
    }

    public Map<String, BigDecimal> getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(Map<String, BigDecimal> assetAllocation) {
        this.assetAllocation = assetAllocation;
    }

    public List<ReportInvestmentHoldingData> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<ReportInvestmentHoldingData> holdings) {
        this.holdings = holdings;
    }

    public List<ReportInvestmentHistoricalData> getHistoricalPerformance() {
        return historicalPerformance;
    }

    public void setHistoricalPerformance(
            List<ReportInvestmentHistoricalData> historicalPerformance) {
        this.historicalPerformance = historicalPerformance;
    }

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }

    public boolean isHistoricalDataAvailable() {
        return historicalDataAvailable;
    }

    public void setHistoricalDataAvailable(boolean historicalDataAvailable) {
        this.historicalDataAvailable = historicalDataAvailable;
    }
}