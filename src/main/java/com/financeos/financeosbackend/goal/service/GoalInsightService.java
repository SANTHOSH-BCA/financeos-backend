package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.dto.GoalInsightResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalInsightService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public GoalInsightService(
            GoalRepository goalRepository,
            CurrentUserService currentUserService) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    public List<GoalInsightResponse> getGoalInsights() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        List<GoalInsightResponse> insights = new ArrayList<>();

        if (goals.isEmpty()) {
            insights.add(new GoalInsightResponse(
                    "NO_DATA",
                    "No financial goals available"
            ));
            return insights;
        }

        for (Goal goal : goals) {

            BigDecimal target = goal.getTargetAmount();
            BigDecimal current = goal.getCurrentAmount();

            BigDecimal progress = BigDecimal.ZERO;

            if (target.compareTo(BigDecimal.ZERO) > 0) {
                progress = current
                        .divide(
                                target,
                                6,
                                java.math.RoundingMode.HALF_UP
                        )
                        .multiply(BigDecimal.valueOf(100));
            }

            if (current.compareTo(target) >= 0) {

                insights.add(new GoalInsightResponse(
                        "GOAL_COMPLETED",
                        goal.getGoalName()
                                + " has reached its target"
                ));

            } else if (!goal.getTargetDate()
                    .isAfter(LocalDate.now())) {

                insights.add(new GoalInsightResponse(
                        "GOAL_AT_RISK",
                        goal.getGoalName()
                                + " has not reached its target before the target date"
                ));

            } else if (progress.compareTo(BigDecimal.valueOf(75)) >= 0) {

                insights.add(new GoalInsightResponse(
                        "GOAL_NEAR_TARGET",
                        goal.getGoalName()
                                + " is more than 75% complete"
                ));
            }
        }

        if (insights.isEmpty()) {
            insights.add(new GoalInsightResponse(
                    "GOALS_PROGRESSING",
                    "Your active goals are progressing toward their targets"
            ));
        }

        return insights;
    }
}