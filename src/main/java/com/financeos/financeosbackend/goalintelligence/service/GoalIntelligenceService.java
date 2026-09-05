package com.financeos.financeosbackend.goalintelligence.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import com.financeos.financeosbackend.goal.enums.GoalStatus;

@Service
public class GoalIntelligenceService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;
    private final CashFlowService cashFlowService;

    public GoalIntelligenceService(
            GoalRepository goalRepository,
            CurrentUserService currentUserService,
            CashFlowService cashFlowService
    ) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
        this.cashFlowService = cashFlowService;
    }

    public BigDecimal calculateProgressPercentage(Goal goal) {

        if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return goal.getCurrentAmount()
                .divide(
                        goal.getTargetAmount(),
                        2,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculateRemainingAmount(Goal goal) {

        BigDecimal remaining =
                goal.getTargetAmount()
                        .subtract(goal.getCurrentAmount());

        return remaining.compareTo(BigDecimal.ZERO) < 0
                ? BigDecimal.ZERO
                : remaining;
    }

    public BigDecimal calculateRequiredMonthlyContribution(Goal goal) {

        BigDecimal remainingAmount =
                calculateRemainingAmount(goal);

        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        long daysRemaining =
                java.time.temporal.ChronoUnit.DAYS.between(
                        java.time.LocalDate.now(),
                        goal.getTargetDate()
                );

        if (daysRemaining <= 0) {
            return BigDecimal.ZERO;
        }

        long monthsRemaining =
                Math.max(1, (daysRemaining + 29) / 30);

        return remainingAmount.divide(
                BigDecimal.valueOf(monthsRemaining),
                2,
                RoundingMode.HALF_UP
        );
    }

    public List<Goal> getMyGoals() {

        User user = currentUserService.getCurrentUser();

        return goalRepository.findByUser(user);
    }

    public BigDecimal calculateCurrentContributionCapacity() {
        return cashFlowService.calculateSavings();
    }

    public BigDecimal calculateTimeToGoalInMonths(Goal goal) {

        BigDecimal remainingAmount =
                calculateRemainingAmount(goal);

        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal contributionCapacity =
                calculateCurrentContributionCapacity();

        if (contributionCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return remainingAmount
                .divide(
                        contributionCapacity,
                        2,
                        RoundingMode.CEILING
                );
    }

    public GoalStatus calculateGoalStatus(Goal goal) {

        if (goal.getCurrentAmount()
                .compareTo(goal.getTargetAmount()) >= 0) {

            return GoalStatus.COMPLETED;
        }

        long daysRemaining =
                java.time.temporal.ChronoUnit.DAYS.between(
                        java.time.LocalDate.now(),
                        goal.getTargetDate()
                );

        if (daysRemaining <= 0) {
            return GoalStatus.AT_RISK;
        }

        BigDecimal requiredContribution =
                calculateRequiredMonthlyContribution(goal);

        BigDecimal contributionCapacity =
                calculateCurrentContributionCapacity();

        return contributionCapacity.compareTo(requiredContribution) >= 0
                ? GoalStatus.ON_TRACK
                : GoalStatus.AT_RISK;
    }

    public boolean isGoalAtRisk(Goal goal) {
        return calculateGoalStatus(goal) == GoalStatus.AT_RISK;
    }

    public boolean isGoalAffordable(Goal goal) {

        BigDecimal requiredContribution =
                calculateRequiredMonthlyContribution(goal);

        BigDecimal contributionCapacity =
                calculateCurrentContributionCapacity();

        return contributionCapacity.compareTo(requiredContribution) >= 0;
    }
}