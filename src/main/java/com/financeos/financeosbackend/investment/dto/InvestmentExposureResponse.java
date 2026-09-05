package com.financeos.financeosbackend.investment.dto;

import java.math.BigDecimal;

public class InvestmentExposureResponse {

    private String investmentType;
    private BigDecimal currentValue;
    private BigDecimal exposurePercentage;

    public InvestmentExposureResponse() {
    }

    public InvestmentExposureResponse(
            String investmentType,
            BigDecimal currentValue,
            BigDecimal exposurePercentage) {

        this.investmentType = investmentType;
        this.currentValue = currentValue;
        this.exposurePercentage = exposurePercentage;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getExposurePercentage() {
        return exposurePercentage;
    }

    public void setExposurePercentage(BigDecimal exposurePercentage) {
        this.exposurePercentage = exposurePercentage;
    }
}