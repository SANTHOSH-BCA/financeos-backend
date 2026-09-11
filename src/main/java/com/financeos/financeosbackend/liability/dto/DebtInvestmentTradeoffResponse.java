package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class DebtInvestmentTradeoffResponse {

    private BigDecimal totalOutstandingDebt;
    private BigDecimal totalInvestmentValue;
    private BigDecimal debtToInvestmentRatio;
    private String tradeoffLevel;
    private String explanation;

    public DebtInvestmentTradeoffResponse() {
    }

    public DebtInvestmentTradeoffResponse(
            BigDecimal totalOutstandingDebt,
            BigDecimal totalInvestmentValue,
            BigDecimal debtToInvestmentRatio,
            String tradeoffLevel,
            String explanation) {

        this.totalOutstandingDebt = totalOutstandingDebt;
        this.totalInvestmentValue = totalInvestmentValue;
        this.debtToInvestmentRatio = debtToInvestmentRatio;
        this.tradeoffLevel = tradeoffLevel;
        this.explanation = explanation;
    }

    public BigDecimal getTotalOutstandingDebt() {
        return totalOutstandingDebt;
    }

    public void setTotalOutstandingDebt(
            BigDecimal totalOutstandingDebt
    ) {
        this.totalOutstandingDebt = totalOutstandingDebt;
    }

    public BigDecimal getTotalInvestmentValue() {
        return totalInvestmentValue;
    }

    public void setTotalInvestmentValue(
            BigDecimal totalInvestmentValue
    ) {
        this.totalInvestmentValue = totalInvestmentValue;
    }

    public BigDecimal getDebtToInvestmentRatio() {
        return debtToInvestmentRatio;
    }

    public void setDebtToInvestmentRatio(
            BigDecimal debtToInvestmentRatio
    ) {
        this.debtToInvestmentRatio = debtToInvestmentRatio;
    }

    public String getTradeoffLevel() {
        return tradeoffLevel;
    }

    public void setTradeoffLevel(
            String tradeoffLevel
    ) {
        this.tradeoffLevel = tradeoffLevel;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(
            String explanation
    ) {
        this.explanation = explanation;
    }
}