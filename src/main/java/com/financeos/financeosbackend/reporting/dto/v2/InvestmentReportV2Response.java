package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.util.List;

public class InvestmentReportV2Response {

    private ReportSectionMetadata metadata;

    private BigDecimal investedAmount;
    private BigDecimal portfolioValue;
    private BigDecimal profitLoss;
    private BigDecimal returnPercentage;

    private List<InvestmentAllocationV2Response> assetAllocation;

    private List<ReportInvestmentHoldingV2Response> holdings;

    private List<ReportInvestmentHoldingV2Response> bestPerformers;
    private List<ReportInvestmentHoldingV2Response> worstPerformers;

    private List<InvestmentHistoricalPerformanceV2Response> historicalPerformance;

    private String valuationDate;

    public InvestmentReportV2Response() {
    }

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
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

    public List<InvestmentAllocationV2Response> getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(
            List<InvestmentAllocationV2Response> assetAllocation) {
        this.assetAllocation = assetAllocation;
    }

    public List<ReportInvestmentHoldingV2Response> getHoldings() {
        return holdings;
    }

    public void setHoldings(
            List<ReportInvestmentHoldingV2Response> holdings) {
        this.holdings = holdings;
    }

    public List<ReportInvestmentHoldingV2Response> getBestPerformers() {
        return bestPerformers;
    }

    public void setBestPerformers(
            List<ReportInvestmentHoldingV2Response> bestPerformers) {
        this.bestPerformers = bestPerformers;
    }

    public List<ReportInvestmentHoldingV2Response> getWorstPerformers() {
        return worstPerformers;
    }

    public void setWorstPerformers(
            List<ReportInvestmentHoldingV2Response> worstPerformers) {
        this.worstPerformers = worstPerformers;
    }

    public List<InvestmentHistoricalPerformanceV2Response>
    getHistoricalPerformance() {
        return historicalPerformance;
    }

    public void setHistoricalPerformance(
            List<InvestmentHistoricalPerformanceV2Response>
                    historicalPerformance) {
        this.historicalPerformance = historicalPerformance;
    }

    public String getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(String valuationDate) {
        this.valuationDate = valuationDate;
    }
}