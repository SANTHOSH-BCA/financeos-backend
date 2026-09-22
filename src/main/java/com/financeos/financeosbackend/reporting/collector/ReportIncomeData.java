package com.financeos.financeosbackend.reporting.collector;

import java.math.BigDecimal;
import java.util.Map;

public class ReportIncomeData {

    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal recurringIncome = BigDecimal.ZERO;
    private BigDecimal irregularIncome = BigDecimal.ZERO;
    private Map<String, BigDecimal> sourceBreakdown;

    public ReportIncomeData() {
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getRecurringIncome() {
        return recurringIncome;
    }

    public void setRecurringIncome(BigDecimal recurringIncome) {
        this.recurringIncome = recurringIncome;
    }

    public BigDecimal getIrregularIncome() {
        return irregularIncome;
    }

    public void setIrregularIncome(BigDecimal irregularIncome) {
        this.irregularIncome = irregularIncome;
    }

    public Map<String, BigDecimal> getSourceBreakdown() {
        return sourceBreakdown;
    }

    public void setSourceBreakdown(Map<String, BigDecimal> sourceBreakdown) {
        this.sourceBreakdown = sourceBreakdown;
    }
}