package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class AnalyticsGoalV2Response {

    private int totalGoals;
    private int completedGoals;
    private int atRiskGoals;
    private int onTrackGoals;

    private BigDecimal totalTargetAmount;
    private BigDecimal totalCurrentAmount;
    private BigDecimal totalRemainingAmount;

    public AnalyticsGoalV2Response() {
    }

    public AnalyticsGoalV2Response(
            int totalGoals,
            int completedGoals,
            int atRiskGoals,
            int onTrackGoals,
            BigDecimal totalTargetAmount,
            BigDecimal totalCurrentAmount,
            BigDecimal totalRemainingAmount
    ) {
        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.atRiskGoals = atRiskGoals;
        this.onTrackGoals = onTrackGoals;
        this.totalTargetAmount = totalTargetAmount;
        this.totalCurrentAmount = totalCurrentAmount;
        this.totalRemainingAmount = totalRemainingAmount;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }

    public int getCompletedGoals() {
        return completedGoals;
    }

    public void setCompletedGoals(int completedGoals) {
        this.completedGoals = completedGoals;
    }

    public int getAtRiskGoals() {
        return atRiskGoals;
    }

    public void setAtRiskGoals(int atRiskGoals) {
        this.atRiskGoals = atRiskGoals;
    }

    public int getOnTrackGoals() {
        return onTrackGoals;
    }

    public void setOnTrackGoals(int onTrackGoals) {
        this.onTrackGoals = onTrackGoals;
    }

    public BigDecimal getTotalTargetAmount() {
        return totalTargetAmount;
    }

    public void setTotalTargetAmount(BigDecimal totalTargetAmount) {
        this.totalTargetAmount = totalTargetAmount;
    }

    public BigDecimal getTotalCurrentAmount() {
        return totalCurrentAmount;
    }

    public void setTotalCurrentAmount(BigDecimal totalCurrentAmount) {
        this.totalCurrentAmount = totalCurrentAmount;
    }

    public BigDecimal getTotalRemainingAmount() {
        return totalRemainingAmount;
    }

    public void setTotalRemainingAmount(BigDecimal totalRemainingAmount) {
        this.totalRemainingAmount = totalRemainingAmount;
    }
}