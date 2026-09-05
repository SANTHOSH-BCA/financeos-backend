package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class CashFlowHealthResponse {

    private BigDecimal inflows;
    private BigDecimal outflows;
    private BigDecimal netCashFlow;
    private BigDecimal savingsRate;
    private String status;

    public CashFlowHealthResponse() {
    }

    public CashFlowHealthResponse(
            BigDecimal inflows,
            BigDecimal outflows,
            BigDecimal netCashFlow,
            BigDecimal savingsRate,
            String status
    ) {
        this.inflows = inflows;
        this.outflows = outflows;
        this.netCashFlow = netCashFlow;
        this.savingsRate = savingsRate;
        this.status = status;
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

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}