package com.financeos.financeosbackend.goal.dto;

import java.math.BigDecimal;

public class GoalIntelligenceResponse {

    private Long totalGoals;
    private Long completedGoals;
    private Long onTrackGoals;
    private Long atRiskGoals;
    private BigDecimal overallProgressPercentage;
    private String strongestGoal;
    private String weakestGoal;
    private String overallObservation;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal financialCapacity;
    private Long financiallyAchievableGoals;

    public GoalIntelligenceResponse() {
    }

    public GoalIntelligenceResponse(
            Long totalGoals,
            Long completedGoals,
            Long onTrackGoals,
            Long atRiskGoals,
            BigDecimal overallProgressPercentage,
            String strongestGoal,
            String weakestGoal,
            String overallObservation,
            BigDecimal monthlyIncome,
            BigDecimal monthlyExpenses,
            BigDecimal financialCapacity,
            Long financiallyAchievableGoals) {

        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.onTrackGoals = onTrackGoals;
        this.atRiskGoals = atRiskGoals;
        this.overallProgressPercentage = overallProgressPercentage;
        this.strongestGoal = strongestGoal;
        this.weakestGoal = weakestGoal;
        this.overallObservation = overallObservation;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.financialCapacity = financialCapacity;
        this.financiallyAchievableGoals = financiallyAchievableGoals;
    }

    public Long getTotalGoals() {
        return totalGoals;
    }

    public Long getCompletedGoals() {
        return completedGoals;
    }

    public Long getOnTrackGoals() {
        return onTrackGoals;
    }

    public Long getAtRiskGoals() {
        return atRiskGoals;
    }

    public BigDecimal getOverallProgressPercentage() {
        return overallProgressPercentage;
    }

    public String getStrongestGoal() {
        return strongestGoal;
    }

    public String getWeakestGoal() {
        return weakestGoal;
    }

    public String getOverallObservation() {
        return overallObservation;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public BigDecimal getFinancialCapacity() {
        return financialCapacity;
    }

    public void setFinancialCapacity(BigDecimal financialCapacity) {
        this.financialCapacity = financialCapacity;
    }

    public Long getFinanciallyAchievableGoals() {
        return financiallyAchievableGoals;
    }

    public void setFinanciallyAchievableGoals(Long financiallyAchievableGoals) {
        this.financiallyAchievableGoals = financiallyAchievableGoals;
    }
}