package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class InvestmentSummaryResponse {

    private BigDecimal totalInvestment;

    public InvestmentSummaryResponse() {
    }

    public InvestmentSummaryResponse(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}