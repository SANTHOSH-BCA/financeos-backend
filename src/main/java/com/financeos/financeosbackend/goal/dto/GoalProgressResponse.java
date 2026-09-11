package com.financeos.financeosbackend.goal.dto;

import java.math.BigDecimal;

public class GoalProgressResponse {

    private Long goalId;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private BigDecimal remainingAmount;
    private BigDecimal progressPercentage;
    private long daysRemaining;
    private BigDecimal requiredMonthlyContribution;
    private String progressStatus;

    private BigDecimal monthlyDebtPayment;
    private BigDecimal availableAfterDebt;
    private BigDecimal goalContributionGap;
    private boolean debtImpactDetected;

    public GoalProgressResponse() {
    }

    public GoalProgressResponse(
            Long goalId,
            String goalName,
            BigDecimal targetAmount,
            BigDecimal currentAmount,
            BigDecimal remainingAmount,
            BigDecimal progressPercentage,
            long daysRemaining,
            BigDecimal requiredMonthlyContribution,
            String progressStatus) {

        this.goalId = goalId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.remainingAmount = remainingAmount;
        this.progressPercentage = progressPercentage;
        this.daysRemaining = daysRemaining;
        this.requiredMonthlyContribution = requiredMonthlyContribution;
        this.progressStatus = progressStatus;
    }

    public Long getGoalId() {
        return goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public BigDecimal getProgressPercentage() {
        return progressPercentage;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public BigDecimal getRequiredMonthlyContribution() {
        return requiredMonthlyContribution;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public BigDecimal getMonthlyDebtPayment() {
        return monthlyDebtPayment;
    }

    public void setMonthlyDebtPayment(
            BigDecimal monthlyDebtPayment
    ) {
        this.monthlyDebtPayment = monthlyDebtPayment;
    }

    public BigDecimal getAvailableAfterDebt() {
        return availableAfterDebt;
    }

    public void setAvailableAfterDebt(
            BigDecimal availableAfterDebt
    ) {
        this.availableAfterDebt = availableAfterDebt;
    }

    public BigDecimal getGoalContributionGap() {
        return goalContributionGap;
    }

    public void setGoalContributionGap(
            BigDecimal goalContributionGap
    ) {
        this.goalContributionGap = goalContributionGap;
    }

    public boolean isDebtImpactDetected() {
        return debtImpactDetected;
    }

    public void setDebtImpactDetected(
            boolean debtImpactDetected
    ) {
        this.debtImpactDetected = debtImpactDetected;
    }
}