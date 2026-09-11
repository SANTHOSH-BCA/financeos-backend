package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class DebtBurdenResponse {

    private BigDecimal totalOutstanding;
    private BigDecimal totalMonthlyPayment;
    private BigDecimal totalPrincipalPaid;
    private BigDecimal totalInterestPaid;
    private BigDecimal totalPaid;
    private int activeLiabilityCount;

    private BigDecimal monthlyIncome;
    private BigDecimal debtPaymentRatio;

    private String burdenLevel;
    private String explanation;

    public DebtBurdenResponse() {
    }

    public BigDecimal getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(BigDecimal totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public BigDecimal getTotalMonthlyPayment() {
        return totalMonthlyPayment;
    }

    public void setTotalMonthlyPayment(BigDecimal totalMonthlyPayment) {
        this.totalMonthlyPayment = totalMonthlyPayment;
    }

    public BigDecimal getTotalPrincipalPaid() {
        return totalPrincipalPaid;
    }

    public void setTotalPrincipalPaid(BigDecimal totalPrincipalPaid) {
        this.totalPrincipalPaid = totalPrincipalPaid;
    }

    public BigDecimal getTotalInterestPaid() {
        return totalInterestPaid;
    }

    public void setTotalInterestPaid(BigDecimal totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getActiveLiabilityCount() {
        return activeLiabilityCount;
    }

    public void setActiveLiabilityCount(int activeLiabilityCount) {
        this.activeLiabilityCount = activeLiabilityCount;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getDebtPaymentRatio() {
        return debtPaymentRatio;
    }

    public void setDebtPaymentRatio(BigDecimal debtPaymentRatio) {
        this.debtPaymentRatio = debtPaymentRatio;
    }

    public String getBurdenLevel() {
        return burdenLevel;
    }

    public void setBurdenLevel(String burdenLevel) {
        this.burdenLevel = burdenLevel;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}