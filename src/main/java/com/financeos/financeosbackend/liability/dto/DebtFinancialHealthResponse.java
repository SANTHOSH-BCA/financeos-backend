package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class DebtFinancialHealthResponse {

    private BigDecimal totalOutstandingDebt;
    private BigDecimal totalMonthlyDebtPayment;
    private BigDecimal debtPaymentRatio;
    private Integer activeLiabilityCount;
    private Integer overdueLiabilityCount;
    private BigDecimal totalPrincipalPaid;
    private BigDecimal totalInterestPaid;
    private BigDecimal totalRepaid;
    private String debtHealth;
    private String explanation;

    public DebtFinancialHealthResponse(
            BigDecimal totalOutstandingDebt,
            BigDecimal totalMonthlyDebtPayment,
            BigDecimal debtPaymentRatio,
            Integer activeLiabilityCount,
            Integer overdueLiabilityCount,
            BigDecimal totalPrincipalPaid,
            BigDecimal totalInterestPaid,
            BigDecimal totalRepaid,
            String debtHealth,
            String explanation
    ) {
        this.totalOutstandingDebt = totalOutstandingDebt;
        this.totalMonthlyDebtPayment = totalMonthlyDebtPayment;
        this.debtPaymentRatio = debtPaymentRatio;
        this.activeLiabilityCount = activeLiabilityCount;
        this.overdueLiabilityCount = overdueLiabilityCount;
        this.totalPrincipalPaid = totalPrincipalPaid;
        this.totalInterestPaid = totalInterestPaid;
        this.totalRepaid = totalRepaid;
        this.debtHealth = debtHealth;
        this.explanation = explanation;
    }

    public BigDecimal getTotalOutstandingDebt() {
        return totalOutstandingDebt;
    }

    public void setTotalOutstandingDebt(BigDecimal totalOutstandingDebt) {
        this.totalOutstandingDebt = totalOutstandingDebt;
    }

    public BigDecimal getTotalMonthlyDebtPayment() {
        return totalMonthlyDebtPayment;
    }

    public void setTotalMonthlyDebtPayment(BigDecimal totalMonthlyDebtPayment) {
        this.totalMonthlyDebtPayment = totalMonthlyDebtPayment;
    }

    public BigDecimal getDebtPaymentRatio() {
        return debtPaymentRatio;
    }

    public void setDebtPaymentRatio(BigDecimal debtPaymentRatio) {
        this.debtPaymentRatio = debtPaymentRatio;
    }

    public Integer getActiveLiabilityCount() {
        return activeLiabilityCount;
    }

    public void setActiveLiabilityCount(Integer activeLiabilityCount) {
        this.activeLiabilityCount = activeLiabilityCount;
    }

    public Integer getOverdueLiabilityCount() {
        return overdueLiabilityCount;
    }

    public void setOverdueLiabilityCount(Integer overdueLiabilityCount) {
        this.overdueLiabilityCount = overdueLiabilityCount;
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

    public BigDecimal getTotalRepaid() {
        return totalRepaid;
    }

    public void setTotalRepaid(BigDecimal totalRepaid) {
        this.totalRepaid = totalRepaid;
    }

    public String getDebtHealth() {
        return debtHealth;
    }

    public void setDebtHealth(String debtHealth) {
        this.debtHealth = debtHealth;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}