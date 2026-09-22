package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.util.Map;

public class ExpenseReportV2Response {

    private BigDecimal totalConfirmedExpenses = BigDecimal.ZERO;
    private Map<String, BigDecimal> categoryBreakdown;
    private BigDecimal helpAmounts = BigDecimal.ZERO;
    private BigDecimal convertedHelpExpenses = BigDecimal.ZERO;

    public ExpenseReportV2Response() {
    }

    public BigDecimal getTotalConfirmedExpenses() {
        return totalConfirmedExpenses;
    }

    public void setTotalConfirmedExpenses(BigDecimal totalConfirmedExpenses) {
        this.totalConfirmedExpenses = totalConfirmedExpenses;
    }

    public Map<String, BigDecimal> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public BigDecimal getHelpAmounts() {
        return helpAmounts;
    }

    public void setHelpAmounts(BigDecimal helpAmounts) {
        this.helpAmounts = helpAmounts;
    }

    public BigDecimal getConvertedHelpExpenses() {
        return convertedHelpExpenses;
    }

    public void setConvertedHelpExpenses(BigDecimal convertedHelpExpenses) {
        this.convertedHelpExpenses = convertedHelpExpenses;
    }

    private ReportSectionMetadata metadata;

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
    }
}