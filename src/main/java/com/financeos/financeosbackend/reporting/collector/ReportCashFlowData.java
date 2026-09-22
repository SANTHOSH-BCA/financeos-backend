package com.financeos.financeosbackend.reporting.collector;

import java.math.BigDecimal;

public class ReportCashFlowData {

    private BigDecimal inflows = BigDecimal.ZERO;
    private BigDecimal outflows = BigDecimal.ZERO;
    private BigDecimal debtPaymentOutflows = BigDecimal.ZERO;
    private BigDecimal netCashFlow = BigDecimal.ZERO;
    private BigDecimal savings = BigDecimal.ZERO;
    private BigDecimal savingsRate = BigDecimal.ZERO;

    public ReportCashFlowData() {
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

    public BigDecimal getDebtPaymentOutflows() {
        return debtPaymentOutflows;
    }

    public void setDebtPaymentOutflows(BigDecimal debtPaymentOutflows) {
        this.debtPaymentOutflows = debtPaymentOutflows;
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