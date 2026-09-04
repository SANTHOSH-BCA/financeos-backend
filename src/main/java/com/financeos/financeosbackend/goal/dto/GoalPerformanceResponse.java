package com.financeos.financeosbackend.goal.dto;

import java.math.BigDecimal;

public class GoalPerformanceResponse {

    private Long goalCount;
    private BigDecimal totalTargetAmount;
    private BigDecimal totalCurrentAmount;
    private BigDecimal totalRemainingAmount;
    private BigDecimal overallProgressPercentage;
    private Long completedGoalCount;
    private Long onTrackGoalCount;
    private Long atRiskGoalCount;

    public GoalPerformanceResponse() {
    }

    public GoalPerformanceResponse(
            Long goalCount,
            BigDecimal totalTargetAmount,
            BigDecimal totalCurrentAmount,
            BigDecimal totalRemainingAmount,
            BigDecimal overallProgressPercentage,
            Long completedGoalCount,
            Long onTrackGoalCount,
            Long atRiskGoalCount) {

        this.goalCount = goalCount;
        this.totalTargetAmount = totalTargetAmount;
        this.totalCurrentAmount = totalCurrentAmount;
        this.totalRemainingAmount = totalRemainingAmount;
        this.overallProgressPercentage = overallProgressPercentage;
        this.completedGoalCount = completedGoalCount;
        this.onTrackGoalCount = onTrackGoalCount;
        this.atRiskGoalCount = atRiskGoalCount;
    }

    public Long getGoalCount() {
        return goalCount;
    }

    public BigDecimal getTotalTargetAmount() {
        return totalTargetAmount;
    }

    public BigDecimal getTotalCurrentAmount() {
        return totalCurrentAmount;
    }

    public BigDecimal getTotalRemainingAmount() {
        return totalRemainingAmount;
    }

    public BigDecimal getOverallProgressPercentage() {
        return overallProgressPercentage;
    }

    public Long getCompletedGoalCount() {
        return completedGoalCount;
    }

    public Long getOnTrackGoalCount() {
        return onTrackGoalCount;
    }

    public Long getAtRiskGoalCount() {
        return atRiskGoalCount;
    }
}