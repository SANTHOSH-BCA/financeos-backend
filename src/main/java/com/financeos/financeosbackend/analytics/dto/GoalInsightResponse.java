package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class GoalInsightResponse {

    private String goalName;
    private BigDecimal completionPercentage;
    private String status;
    private String recommendation;

    public GoalInsightResponse() {
    }

    public GoalInsightResponse(
            String goalName,
            BigDecimal completionPercentage,
            String status,
            String recommendation) {

        this.goalName = goalName;
        this.completionPercentage = completionPercentage;
        this.status = status;
        this.recommendation = recommendation;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public BigDecimal getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(BigDecimal completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}