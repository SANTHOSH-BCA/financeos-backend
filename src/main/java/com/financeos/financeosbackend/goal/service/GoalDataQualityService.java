package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.dto.GoalDataQualityResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoalDataQualityService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public GoalDataQualityService(
            GoalRepository goalRepository,
            CurrentUserService currentUserService) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    public GoalDataQualityResponse validateGoal(Long goalId) {

        User user = currentUserService.getCurrentUser();

        Goal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() ->
                        new IllegalArgumentException("Goal not found"));

        List<String> warnings = new ArrayList<>();

        if (goal.getTargetAmount() == null
                || goal.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            warnings.add("Target amount must be greater than zero");
        }

        if (goal.getCurrentAmount() == null
                || goal.getCurrentAmount().compareTo(BigDecimal.ZERO) < 0) {
            warnings.add("Current amount cannot be negative");
        }

        if (goal.getTargetDate() == null) {
            warnings.add("Target date is required");
        }

        if (goal.getTargetAmount() != null
                && goal.getCurrentAmount() != null
                && goal.getCurrentAmount()
                .compareTo(goal.getTargetAmount()) > 0) {
            warnings.add("Current amount exceeds target amount");
        }

        if (goal.getTargetDate() != null
                && goal.getTargetDate().isBefore(LocalDate.now())
                && (goal.getCurrentAmount() == null
                || goal.getTargetAmount() == null
                || goal.getCurrentAmount()
                .compareTo(goal.getTargetAmount()) < 0)) {
            warnings.add("Goal target date has passed before completion");
        }

        return new GoalDataQualityResponse(
                warnings.isEmpty(),
                warnings
        );
    }
}