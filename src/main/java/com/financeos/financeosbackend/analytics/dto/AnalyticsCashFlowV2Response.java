package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class AnalyticsCashFlowV2Response {

    private BigDecimal inflows;
    private BigDecimal outflows;
    private BigDecimal netCashFlow;
    private BigDecimal savings;
    private BigDecimal savingsRate;

    public AnalyticsCashFlowV2Response() {
    }

    public AnalyticsCashFlowV2Response(
            BigDecimal inflows,
            BigDecimal outflows,
            BigDecimal netCashFlow,
            BigDecimal savings,
            BigDecimal savingsRate
    ) {
        this.inflows = inflows;
        this.outflows = outflows;
        this.netCashFlow = netCashFlow;
        this.savings = savings;
        this.savingsRate = savingsRate;
    }

    public BigDecimal getInflows() {
        return inflows;
    }

    public void setInflows(BigDecimal inflows) {
        this.inflows = inflows;
    }

    public BigDecimal getOutflows() {
        return outflows;
    }

    public void setOutflows(BigDecimal outflows) {
        this.outflows = outflows;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }
}