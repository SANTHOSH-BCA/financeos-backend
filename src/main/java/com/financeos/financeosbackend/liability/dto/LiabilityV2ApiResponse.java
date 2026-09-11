package com.financeos.financeosbackend.liability.dto;

import java.math.BigDecimal;

public class LiabilityV2ApiResponse {

    private BigDecimal totalOutstandingDebt;
    private BigDecimal totalMonthlyDebtPayment;
    private BigDecimal totalInvestmentValue;
    private BigDecimal netWorth;
    private BigDecimal debtPaymentRatio;
    private BigDecimal debtToInvestmentRatio;
    private Integer activeLiabilityCount;
    private String debtBurdenLevel;
    private String debtHealth;
    private String projectionStatus;

    public LiabilityV2ApiResponse(
            BigDecimal totalOutstandingDebt,
            BigDecimal totalMonthlyDebtPayment,
            BigDecimal totalInvestmentValue,
            BigDecimal netWorth,
            BigDecimal debtPaymentRatio,
            BigDecimal debtToInvestmentRatio,
            Integer activeLiabilityCount,
            String debtBurdenLevel,
            String debtHealth,
            String projectionStatus
    ) {
        this.totalOutstandingDebt = totalOutstandingDebt;
        this.totalMonthlyDebtPayment = totalMonthlyDebtPayment;
        this.totalInvestmentValue = totalInvestmentValue;
        this.netWorth = netWorth;
        this.debtPaymentRatio = debtPaymentRatio;
        this.debtToInvestmentRatio = debtToInvestmentRatio;
        this.activeLiabilityCount = activeLiabilityCount;
        this.debtBurdenLevel = debtBurdenLevel;
        this.debtHealth = debtHealth;
        this.projectionStatus = projectionStatus;
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

    public BigDecimal getTotalInvestmentValue() {
        return totalInvestmentValue;
    }

    public void setTotalInvestmentValue(BigDecimal totalInvestmentValue) {
        this.totalInvestmentValue = totalInvestmentValue;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getDebtPaymentRatio() {
        return debtPaymentRatio;
    }

    public void setDebtPaymentRatio(BigDecimal debtPaymentRatio) {
        this.debtPaymentRatio = debtPaymentRatio;
    }

    public BigDecimal getDebtToInvestmentRatio() {
        return debtToInvestmentRatio;
    }

    public void setDebtToInvestmentRatio(BigDecimal debtToInvestmentRatio) {
        this.debtToInvestmentRatio = debtToInvestmentRatio;
    }

    public Integer getActiveLiabilityCount() {
        return activeLiabilityCount;
    }

    public void setActiveLiabilityCount(Integer activeLiabilityCount) {
        this.activeLiabilityCount = activeLiabilityCount;
    }

    public String getDebtBurdenLevel() {
        return debtBurdenLevel;
    }

    public void setDebtBurdenLevel(String debtBurdenLevel) {
        this.debtBurdenLevel = debtBurdenLevel;
    }

    public String getDebtHealth() {
        return debtHealth;
    }

    public void setDebtHealth(String debtHealth) {
        this.debtHealth = debtHealth;
    }

    public String getProjectionStatus() {
        return projectionStatus;
    }

    public void setProjectionStatus(String projectionStatus) {
        this.projectionStatus = projectionStatus;
    }
}