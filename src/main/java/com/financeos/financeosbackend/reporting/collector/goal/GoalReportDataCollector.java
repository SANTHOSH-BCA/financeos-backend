package com.financeos.financeosbackend.reporting.collector.goal;

import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goalintelligence.service.GoalIntelligenceService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoalReportDataCollector {

    private final GoalIntelligenceService goalIntelligenceService;

    public GoalReportDataCollector(
            GoalIntelligenceService goalIntelligenceService) {

        this.goalIntelligenceService = goalIntelligenceService;
    }

    public ReportGoalData collect(
            User user,
            ReportPeriodResponse period) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (period == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null"
            );
        }

        List<Goal> goals =
                goalIntelligenceService.getMyGoals();

        ReportGoalData data = new ReportGoalData();

        if (goals.isEmpty()) {
            data.setDataAvailable(false);
            return data;
        }

        List<ReportGoalItemData> goalItems =
                new ArrayList<>();

        int completedGoals = 0;
        int onTrackGoals = 0;
        int atRiskGoals = 0;

        for (Goal goal : goals) {

            GoalStatus status =
                    goalIntelligenceService.calculateGoalStatus(goal);

            ReportGoalItemData item =
                    new ReportGoalItemData();

            item.setGoalId(goal.getId());
            item.setGoalName(goal.getGoalName());
            item.setTargetAmount(goal.getTargetAmount());
            item.setCurrentAmount(goal.getCurrentAmount());

            item.setRemainingAmount(
                    goalIntelligenceService
                            .calculateRemainingAmount(goal)
            );

            item.setContribution(
                    goalIntelligenceService
                            .calculateRequiredMonthlyContribution(goal)
            );

            item.setTargetDate(goal.getTargetDate());

            item.setStatus(
                    status != null
                            ? status.toString()
                            : null
            );

            goalItems.add(item);

            if (status == GoalStatus.COMPLETED) {
                completedGoals++;
            } else if (status == GoalStatus.AT_RISK) {
                atRiskGoals++;
            } else if (status == GoalStatus.ON_TRACK) {
                onTrackGoals++;
            }
        }

        data.setTotalGoals(goals.size());
        data.setCompletedGoals(completedGoals);
        data.setOnTrackGoals(onTrackGoals);
        data.setAtRiskGoals(atRiskGoals);
        data.setGoals(goalItems);
        data.setDataAvailable(true);

        return data;
    }
}