package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportChangeInsightV2Response {

    private String metric;
    private BigDecimal currentValue;
    private BigDecimal previousValue;
    private BigDecimal absoluteChange;
    private BigDecimal percentageChange;
    private String direction;
    private boolean significant;

    private String whatChanged;
    private String whyDidItChange;

    private List<ReportChangeContributorV2Response> contributors =
            new ArrayList<>();

    public ReportChangeInsightV2Response() {
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

    public String getWhatChanged() {
        return whatChanged;
    }

    public void setWhatChanged(String whatChanged) {
        this.whatChanged = whatChanged;
    }

    public String getWhyDidItChange() {
        return whyDidItChange;
    }

    public void setWhyDidItChange(String whyDidItChange) {
        this.whyDidItChange = whyDidItChange;
    }

    public List<ReportChangeContributorV2Response> getContributors() {
        return contributors;
    }

    public void setContributors(
            List<ReportChangeContributorV2Response> contributors
    ) {
        this.contributors = contributors;
    }
}