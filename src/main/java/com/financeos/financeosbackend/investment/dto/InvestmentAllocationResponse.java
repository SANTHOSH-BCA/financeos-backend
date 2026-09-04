package com.financeos.financeosbackend.investment.dto;

import java.math.BigDecimal;

public class InvestmentAllocationResponse {

    private String investmentType;
    private BigDecimal investedAmount;
    private BigDecimal allocationPercentage;

    public InvestmentAllocationResponse() {
    }

    public InvestmentAllocationResponse(
            String investmentType,
            BigDecimal investedAmount,
            BigDecimal allocationPercentage) {

        this.investmentType = investmentType;
        this.investedAmount = investedAmount;
        this.allocationPercentage = allocationPercentage;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public BigDecimal getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(BigDecimal allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }
}