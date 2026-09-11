package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class DebtNetWorthImpactResponse {

    private BigDecimal recognizedAssets;
    private BigDecimal investmentValue;
    private BigDecimal recognizedLiabilities;
    private BigDecimal netWorth;
    private BigDecimal liabilityImpactPercentage;
    private String explanation;

    public DebtNetWorthImpactResponse(
            BigDecimal recognizedAssets,
            BigDecimal investmentValue,
            BigDecimal recognizedLiabilities,
            BigDecimal netWorth,
            BigDecimal liabilityImpactPercentage,
            String explanation
    ) {
        this.recognizedAssets = recognizedAssets;
        this.investmentValue = investmentValue;
        this.recognizedLiabilities = recognizedLiabilities;
        this.netWorth = netWorth;
        this.liabilityImpactPercentage = liabilityImpactPercentage;
        this.explanation = explanation;
    }

    public BigDecimal getRecognizedAssets() {
        return recognizedAssets;
    }

    public void setRecognizedAssets(BigDecimal recognizedAssets) {
        this.recognizedAssets = recognizedAssets;
    }

    public BigDecimal getInvestmentValue() {
        return investmentValue;
    }

    public void setInvestmentValue(BigDecimal investmentValue) {
        this.investmentValue = investmentValue;
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(BigDecimal recognizedLiabilities) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getLiabilityImpactPercentage() {
        return liabilityImpactPercentage;
    }

    public void setLiabilityImpactPercentage(
            BigDecimal liabilityImpactPercentage
    ) {
        this.liabilityImpactPercentage = liabilityImpactPercentage;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}