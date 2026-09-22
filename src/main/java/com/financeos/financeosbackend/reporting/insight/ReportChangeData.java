package com.financeos.financeosbackend.reporting.insight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportChangeData {

    private String metric;
    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal previousValue = BigDecimal.ZERO;
    private BigDecimal absoluteChange = BigDecimal.ZERO;
    private BigDecimal percentageChange = BigDecimal.ZERO;

    private String direction;
    private boolean significant;

    private List<ReportChangeContributorData> contributors =
            new ArrayList<>();

    public ReportChangeData() {
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isSignificant() {
        return significant;
    }

    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

    public List<ReportChangeContributorData> getContributors() {
        return contributors;
    }

    public void setContributors(
            List<ReportChangeContributorData> contributors
    ) {
        this.contributors = contributors;
    }
}