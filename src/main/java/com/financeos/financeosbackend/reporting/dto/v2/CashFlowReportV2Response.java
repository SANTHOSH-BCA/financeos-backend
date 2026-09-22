package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;

public class CashFlowReportV2Response {

    private BigDecimal inflows = BigDecimal.ZERO;
    private BigDecimal outflows = BigDecimal.ZERO;
    private BigDecimal debtPaymentOutflows = BigDecimal.ZERO;
    private BigDecimal netCashFlow = BigDecimal.ZERO;
    private BigDecimal savings = BigDecimal.ZERO;
    private BigDecimal savingsRate = BigDecimal.ZERO;

    public CashFlowReportV2Response() {
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

    private ReportSectionMetadata metadata;

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
    }
}