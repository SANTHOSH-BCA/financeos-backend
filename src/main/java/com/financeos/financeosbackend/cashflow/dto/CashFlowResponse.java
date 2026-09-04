package com.financeos.financeosbackend.cashflow.dto;

import java.math.BigDecimal;

public class CashFlowResponse {

    private BigDecimal inflows;
    private BigDecimal outflows;
    private BigDecimal netCashFlow;

    public CashFlowResponse() {
    }

    public CashFlowResponse(
            BigDecimal inflows,
            BigDecimal outflows,
            BigDecimal netCashFlow
    ) {
        this.inflows = inflows;
        this.outflows = outflows;
        this.netCashFlow = netCashFlow;
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
}