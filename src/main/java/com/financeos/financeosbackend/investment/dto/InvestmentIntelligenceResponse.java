package com.financeos.financeosbackend.investment.dto;

import java.math.BigDecimal;

public class InvestmentIntelligenceResponse {

    private BigDecimal totalInvestedAmount;
    private BigDecimal currentPortfolioValue;
    private BigDecimal totalProfitLoss;
    private BigDecimal returnPercentage;

    private String bestPerformer;
    private String worstPerformer;
    private String dominantInvestmentType;

    private String contributionBehaviour;
    private String portfolioObservation;

    public InvestmentIntelligenceResponse() {
    }

    public InvestmentIntelligenceResponse(
            BigDecimal totalInvestedAmount,
            BigDecimal currentPortfolioValue,
            BigDecimal totalProfitLoss,
            BigDecimal returnPercentage,
            String bestPerformer,
            String worstPerformer,
            String dominantInvestmentType,
            String contributionBehaviour,
            String portfolioObservation) {

        this.totalInvestedAmount = totalInvestedAmount;
        this.currentPortfolioValue = currentPortfolioValue;
        this.totalProfitLoss = totalProfitLoss;
        this.returnPercentage = returnPercentage;
        this.bestPerformer = bestPerformer;
        this.worstPerformer = worstPerformer;
        this.dominantInvestmentType = dominantInvestmentType;
        this.contributionBehaviour = contributionBehaviour;
        this.portfolioObservation = portfolioObservation;
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

    public String getBestPerformer() {
        return bestPerformer;
    }

    public void setBestPerformer(String bestPerformer) {
        this.bestPerformer = bestPerformer;
    }

    public String getWorstPerformer() {
        return worstPerformer;
    }

    public void setWorstPerformer(String worstPerformer) {
        this.worstPerformer = worstPerformer;
    }

    public String getDominantInvestmentType() {
        return dominantInvestmentType;
    }

    public void setDominantInvestmentType(String dominantInvestmentType) {
        this.dominantInvestmentType = dominantInvestmentType;
    }

    public String getContributionBehaviour() {
        return contributionBehaviour;
    }

    public void setContributionBehaviour(String contributionBehaviour) {
        this.contributionBehaviour = contributionBehaviour;
    }

    public String getPortfolioObservation() {
        return portfolioObservation;
    }

    public void setPortfolioObservation(String portfolioObservation) {
        this.portfolioObservation = portfolioObservation;
    }
}