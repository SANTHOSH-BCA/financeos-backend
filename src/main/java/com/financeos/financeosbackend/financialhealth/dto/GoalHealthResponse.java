package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class GoalHealthResponse {

    private Long totalGoals;
    private Long completedGoals;
    private Long atRiskGoals;
    private Long onTrackGoals;
    private BigDecimal totalTargetAmount;
    private BigDecimal totalCurrentAmount;
    private String status;

    public GoalHealthResponse() {
    }

    public GoalHealthResponse(
            Long totalGoals,
            Long completedGoals,
            Long atRiskGoals,
            Long onTrackGoals,
            BigDecimal totalTargetAmount,
            BigDecimal totalCurrentAmount,
            String status
    ) {
        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.atRiskGoals = atRiskGoals;
        this.onTrackGoals = onTrackGoals;
        this.totalTargetAmount = totalTargetAmount;
        this.totalCurrentAmount = totalCurrentAmount;
        this.status = status;
    }

    public Long getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Long totalGoals) {
        this.totalGoals = totalGoals;
    }

    public Long getCompletedGoals() {
        return completedGoals;
    }

    public void setCompletedGoals(Long completedGoals) {
        this.completedGoals = completedGoals;
    }

    public Long getAtRiskGoals() {
        return atRiskGoals;
    }

    public void setAtRiskGoals(Long atRiskGoals) {
        this.atRiskGoals = atRiskGoals;
    }

    public Long getOnTrackGoals() {
        return onTrackGoals;
    }

    public void setOnTrackGoals(Long onTrackGoals) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}