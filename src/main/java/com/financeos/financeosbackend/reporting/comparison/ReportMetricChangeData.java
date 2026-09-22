package com.financeos.financeosbackend.reporting.comparison;

import java.math.BigDecimal;

public class ReportMetricChangeData {

    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal previousValue = BigDecimal.ZERO;
    private BigDecimal absoluteChange = BigDecimal.ZERO;
    private BigDecimal percentageChange = BigDecimal.ZERO;

    public ReportMetricChangeData() {
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    public BigDecimal getAbsoluteChange() {
        return absoluteChange;
    }

    public void setAbsoluteChange(BigDecimal absoluteChange) {
        this.absoluteChange = absoluteChange;
    }

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(BigDecimal percentageChange) {
        this.percentageChange = percentageChange;
    }
}