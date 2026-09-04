package com.financeos.financeosbackend.investment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvestmentValuationHistoryResponse {

    private Long id;
    private Long investmentId;
    private BigDecimal investedAmount;
    private BigDecimal currentValue;
    private BigDecimal profitLoss;
    private LocalDate valuationDate;

    public InvestmentValuationHistoryResponse() {
    }

    public InvestmentValuationHistoryResponse(
            Long id,
            Long investmentId,
            BigDecimal investedAmount,
            BigDecimal currentValue,
            BigDecimal profitLoss,
            LocalDate valuationDate) {

        this.id = id;
        this.investmentId = investmentId;
        this.investedAmount = investedAmount;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
        this.valuationDate = valuationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(Long investmentId) {
        this.investmentId = investmentId;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(BigDecimal investedAmount) {
        this.investedAmount = investedAmount;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
    }

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }
}