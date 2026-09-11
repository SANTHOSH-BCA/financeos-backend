package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class DebtFinancialFutureResponse {

    private BigDecimal totalOutstandingDebt;
    private BigDecimal monthlyDebtPayment;
    private Integer estimatedRemainingMonths;
    private BigDecimal estimatedRemainingPayments;
    private String projectionStatus;
    private String explanation;

    public DebtFinancialFutureResponse(
            BigDecimal totalOutstandingDebt,
            BigDecimal monthlyDebtPayment,
            Integer estimatedRemainingMonths,
            BigDecimal estimatedRemainingPayments,
            String projectionStatus,
            String explanation
    ) {
        this.totalOutstandingDebt = totalOutstandingDebt;
        this.monthlyDebtPayment = monthlyDebtPayment;
        this.estimatedRemainingMonths = estimatedRemainingMonths;
        this.estimatedRemainingPayments = estimatedRemainingPayments;
        this.projectionStatus = projectionStatus;
        this.explanation = explanation;
    }

    public BigDecimal getTotalOutstandingDebt() {
        return totalOutstandingDebt;
    }

    public void setTotalOutstandingDebt(BigDecimal totalOutstandingDebt) {
        this.totalOutstandingDebt = totalOutstandingDebt;
    }

    public BigDecimal getMonthlyDebtPayment() {
        return monthlyDebtPayment;
    }

    public void setMonthlyDebtPayment(BigDecimal monthlyDebtPayment) {
        this.monthlyDebtPayment = monthlyDebtPayment;
    }

    public Integer getEstimatedRemainingMonths() {
        return estimatedRemainingMonths;
    }

    public void setEstimatedRemainingMonths(Integer estimatedRemainingMonths) {
        this.estimatedRemainingMonths = estimatedRemainingMonths;
    }

    public BigDecimal getEstimatedRemainingPayments() {
        return estimatedRemainingPayments;
    }

    public void setEstimatedRemainingPayments(
            BigDecimal estimatedRemainingPayments
    ) {
        this.estimatedRemainingPayments = estimatedRemainingPayments;
    }

    public String getProjectionStatus() {
        return projectionStatus;
    }

    public void setProjectionStatus(String projectionStatus) {
        this.projectionStatus = projectionStatus;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}