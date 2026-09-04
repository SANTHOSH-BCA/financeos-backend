package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.income.service.IncomeService;
import com.financeos.financeosbackend.expense.service.ExpenseService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.dto.GoalIntelligenceResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.income.dto.MonthlyIncomeResponse;
import com.financeos.financeosbackend.expense.dto.MonthlyExpenseResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;


@Service
public class GoalIntelligenceService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public GoalIntelligenceService(
            GoalRepository goalRepository,
            CurrentUserService currentUserService,
            IncomeService incomeService,
            ExpenseService expenseService) {

        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    public GoalIntelligenceResponse getGoalIntelligence() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        BigDecimal monthlyIncome = incomeService.getMonthlyIncomeHistory()
                .stream()
                .map(MonthlyIncomeResponse::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyExpenses = expenseService.getMonthlyExpenseHistory()
                .stream()
                .map(MonthlyExpenseResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal financialCapacity =
                monthlyIncome.subtract(monthlyExpenses);

        long financiallyAchievableGoals = goals.stream()
                .filter(goal -> {
                    BigDecimal remainingAmount =
                            goal.getTargetAmount()
                                    .subtract(goal.getCurrentAmount());

                    if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        return true;
                    }

                    long monthsRemaining = java.time.temporal.ChronoUnit.MONTHS.between(
                            java.time.LocalDate.now().withDayOfMonth(1),
                            goal.getTargetDate().withDayOfMonth(1)
                    );

                    if (monthsRemaining <= 0) {
                        return false;
                    }

                    BigDecimal requiredMonthlyContribution =
                            remainingAmount.divide(
                                    BigDecimal.valueOf(monthsRemaining),
                                    2,
                                    RoundingMode.HALF_UP
                            );

                    return requiredMonthlyContribution
                            .compareTo(financialCapacity) <= 0;
                })
                .count();

        if (goals.isEmpty()) {
            return new GoalIntelligenceResponse(
                    0L,
                    0L,
                    0L,
                    0L,
                    BigDecimal.ZERO,
                    null,
                    null,
                    "No goals available",
                    monthlyIncome,
                    monthlyExpenses,
                    financialCapacity,
                    0L
            );
        }

        long completedGoals = goals.stream()
                .filter(g -> g.getCurrentAmount()
                        .compareTo(g.getTargetAmount()) >= 0)
                .count();

        long atRiskGoals = goals.stream()
                .filter(g -> {
                    if (g.getCurrentAmount()
                            .compareTo(g.getTargetAmount()) >= 0) {
                        return false;
                    }

                    long monthsRemaining = java.time.temporal.ChronoUnit.MONTHS.between(
                            java.time.LocalDate.now().withDayOfMonth(1),
                            g.getTargetDate().withDayOfMonth(1)
                    );

                    if (monthsRemaining <= 0) {
                        return true;
                    }

                    BigDecimal remainingAmount =
                            g.getTargetAmount()
                                    .subtract(g.getCurrentAmount());

                    BigDecimal requiredMonthlyContribution =
                            remainingAmount.divide(
                                    BigDecimal.valueOf(monthsRemaining),
                                    2,
                                    RoundingMode.HALF_UP
                            );

                    return requiredMonthlyContribution
                            .compareTo(financialCapacity) > 0;
                })
                .count();

        long onTrackGoals =
                goals.size() - completedGoals - atRiskGoals;

        BigDecimal totalTarget = goals.stream()
                .map(Goal::getTargetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrent = goals.stream()
                .map(Goal::getCurrentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal progress = BigDecimal.ZERO;

        if (totalTarget.compareTo(BigDecimal.ZERO) > 0) {
            progress = totalCurrent
                    .divide(totalTarget, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        Goal strongest = goals.stream()
                .max(Comparator.comparing(
                        g -> g.getCurrentAmount()
                                .divide(
                                        g.getTargetAmount(),
                                        6,
                                        RoundingMode.HALF_UP
                                )))
                .orElse(null);

        Goal weakest = goals.stream()
                .min(Comparator.comparing(
                        g -> g.getCurrentAmount()
                                .divide(
                                        g.getTargetAmount(),
                                        6,
                                        RoundingMode.HALF_UP
                                )))
                .orElse(null);

        String observation;

        if (completedGoals == goals.size()) {
            observation = "All goals have been completed";
        } else if (financialCapacity.compareTo(BigDecimal.ZERO) <= 0) {
            observation = "Current financial capacity is insufficient for goal contributions";
        } else if (atRiskGoals > 0) {
            observation = "Some goals require attention based on financial capacity";
        } else {
            observation = "Goals are financially achievable with current capacity";
        }

        return new GoalIntelligenceResponse(
                (long) goals.size(),
                completedGoals,
                onTrackGoals,
                atRiskGoals,
                progress,
                strongest != null
                        ? strongest.getGoalName()
                        : null,
                weakest != null
                        ? weakest.getGoalName()
                        : null,
                observation,
                monthlyIncome,
                monthlyExpenses,
                financialCapacity,
                financiallyAchievableGoals
        );
    }
}