package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FinancialHealthGoalService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public FinancialHealthGoalService(
            GoalRepository goalRepository,
            CurrentUserService currentUserService
    ) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    public GoalHealthResponse calculateGoalHealth() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        long totalGoals = goals.size();

        long completedGoals = goals.stream()
                .filter(goal -> goal.getGoalStatus() == GoalStatus.COMPLETED)
                .count();

        long atRiskGoals = goals.stream()
                .filter(goal -> goal.getGoalStatus() == GoalStatus.AT_RISK)
                .count();

        long onTrackGoals = goals.stream()
                .filter(goal -> goal.getGoalStatus() == GoalStatus.ON_TRACK)
                .count();

        BigDecimal totalTargetAmount = goals.stream()
                .map(Goal::getTargetAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrentAmount = goals.stream()
                .map(Goal::getCurrentAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String status;

        if (totalGoals == 0) {
            status = "NO_GOALS";
        } else if (atRiskGoals > 0) {
            status = "AT_RISK";
        } else if (completedGoals == totalGoals) {
            status = "COMPLETED";
        } else {
            status = "ON_TRACK";
        }

        return new GoalHealthResponse(
                totalGoals,
                completedGoals,
                atRiskGoals,
                onTrackGoals,
                totalTargetAmount,
                totalCurrentAmount,
                status
        );
    }
}