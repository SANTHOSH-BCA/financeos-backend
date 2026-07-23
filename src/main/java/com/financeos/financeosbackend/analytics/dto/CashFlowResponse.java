package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class CashFlowResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netCashFlow;

    public CashFlowResponse() {
    }

    public CashFlowResponse(
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            BigDecimal netCashFlow) {

        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netCashFlow = netCashFlow;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }
}