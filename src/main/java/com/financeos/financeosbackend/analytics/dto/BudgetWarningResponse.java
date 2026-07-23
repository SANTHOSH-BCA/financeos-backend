package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class BudgetWarningResponse {

    private String category;
    private BigDecimal spentAmount;
    private BigDecimal income;
    private BigDecimal percentage;
    private String warning;

    public BudgetWarningResponse() {
    }

    public BudgetWarningResponse(String category,
                                 BigDecimal spentAmount,
                                 BigDecimal income,
                                 BigDecimal percentage,
                                 String warning) {
        this.category = category;
        this.spentAmount = spentAmount;
        this.income = income;
        this.percentage = percentage;
        this.warning = warning;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}