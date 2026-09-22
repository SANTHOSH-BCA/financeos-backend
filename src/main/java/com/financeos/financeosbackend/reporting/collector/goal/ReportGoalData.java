package com.financeos.financeosbackend.reporting.collector.goal;

import java.util.ArrayList;
import java.util.List;

public class ReportGoalData {

    private int totalGoals;
    private int completedGoals;
    private int onTrackGoals;
    private int atRiskGoals;

    private List<ReportGoalItemData> goals = new ArrayList<>();

    private boolean dataAvailable;

    public ReportGoalData() {
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

    public List<ReportGoalItemData> getGoals() {
        return goals;
    }

    public void setGoals(List<ReportGoalItemData> goals) {
        this.goals = goals;
    }

    public boolean isDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(boolean dataAvailable) {
        this.dataAvailable = dataAvailable;
    }
}