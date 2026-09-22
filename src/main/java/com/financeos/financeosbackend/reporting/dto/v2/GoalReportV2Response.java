package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalReportV2Response {

    private ReportSectionMetadata metadata;

    private int totalGoals;
    private int completedGoals;
    private int onTrackGoals;
    private int atRiskGoals;

    private List<GoalItem> goals = new ArrayList<>();

    public GoalReportV2Response() {
    }

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
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

    public int getOnTrackGoals() {
        return onTrackGoals;
    }

    public void setOnTrackGoals(int onTrackGoals) {
        this.onTrackGoals = onTrackGoals;
    }

    public int getAtRiskGoals() {
        return atRiskGoals;
    }

    public void setAtRiskGoals(int atRiskGoals) {
        this.atRiskGoals = atRiskGoals;
    }

    public List<GoalItem> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalItem> goals) {
        this.goals = goals;
    }

    public static class GoalItem {

        private Long goalId;
        private String goalName;

        private BigDecimal targetAmount;
        private BigDecimal currentAmount;
        private BigDecimal remainingAmount;
        private BigDecimal contribution;

        private LocalDate targetDate;
        private LocalDate projectedCompletionDate;

        private String status;

        public GoalItem() {
        }

        public Long getGoalId() {
            return goalId;
        }

        public void setGoalId(Long goalId) {
            this.goalId = goalId;
        }

        public String getGoalName() {
            return goalName;
        }

        public void setGoalName(String goalName) {
            this.goalName = goalName;
        }

        public BigDecimal getTargetAmount() {
            return targetAmount;
        }

        public void setTargetAmount(BigDecimal targetAmount) {
            this.targetAmount = targetAmount;
        }

        public BigDecimal getCurrentAmount() {
            return currentAmount;
        }

        public void setCurrentAmount(BigDecimal currentAmount) {
            this.currentAmount = currentAmount;
        }

        public BigDecimal getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(BigDecimal remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public BigDecimal getContribution() {
            return contribution;
        }

        public void setContribution(BigDecimal contribution) {
            this.contribution = contribution;
        }

        public LocalDate getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(LocalDate targetDate) {
            this.targetDate = targetDate;
        }

        public LocalDate getProjectedCompletionDate() {
            return projectedCompletionDate;
        }

        public void setProjectedCompletionDate(
                LocalDate projectedCompletionDate) {
            this.projectedCompletionDate = projectedCompletionDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}