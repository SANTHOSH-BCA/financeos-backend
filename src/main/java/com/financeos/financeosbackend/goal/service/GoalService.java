package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.financeos.financeosbackend.common.service.CurrentUserService;import com.financeos.financeosbackend.goal.dto.GoalProgressResponse;import java.math.BigDecimal;
import com.financeos.financeosbackend.goal.dto.GoalProgressResponse;import com.financeos.financeosbackend.goal.enums.GoalStatus;import com.financeos.financeosbackend.goal.enums.GoalStatus;import com.financeos.financeosbackend.goal.repository.GoalContributionRepository;
import com.financeos.financeosbackend.goal.dto.GoalContributionResponse;
import com.financeos.financeosbackend.goal.entity.GoalContribution;import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;import com.financeos.financeosbackend.goal.dto.GoalPerformanceResponse;

@Service
public class GoalService {

    private static final Logger logger =
            LoggerFactory.getLogger(GoalService.class);

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final GoalContributionRepository goalContributionRepository;

    public GoalService(
            GoalRepository goalRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService,
            GoalContributionRepository goalContributionRepository) {

        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.goalContributionRepository = goalContributionRepository;
    }

    public GoalResponse addGoal(AddGoalRequest request) {

        if (request.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Target date cannot be in the past");
        }

        User user = currentUserService.getCurrentUser();

        Goal goal = new Goal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());
        goal.setUser(user);

        logger.info("Creating goal '{}' for user: {}", request.getGoalName(), user.getEmail());

        Goal savedGoal = goalRepository.save(goal);

        logger.info("Goal created successfully with ID: {}", savedGoal.getId());

        return mapToResponse(savedGoal);
    }

    public Page<GoalResponse> getMyGoals(Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        return goalRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public GoalResponse updateGoal(Long id, AddGoalRequest request) {

        if (request.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Target date cannot be in the past"
            );
        }

        User user = currentUserService.getCurrentUser();

        logger.info(
                "Updating goal with ID: {} for user: {}",
                id,
                user.getEmail()
        );

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());

        Goal updatedGoal = goalRepository.save(goal);

        logger.info(
                "Goal updated successfully with ID: {}",
                updatedGoal.getId()
        );

        return mapToResponse(updatedGoal);
    }

    public void deleteGoal(Long id) {

        User user = currentUserService.getCurrentUser();

        logger.info("Deleting goal with ID: {} for user: {}", id, user.getEmail());

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goalRepository.delete(goal);

        logger.info("Goal deleted successfully with ID: {}", id);
    }

    public GoalProgressResponse getGoalProgress(Long id) {

        User user = currentUserService.getCurrentUser();

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        BigDecimal target = goal.getTargetAmount();
        BigDecimal current = goal.getCurrentAmount();

        BigDecimal remaining = target.subtract(current);

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }

        BigDecimal progressPercentage = BigDecimal.ZERO;

        if (target.compareTo(BigDecimal.ZERO) > 0) {
            progressPercentage = current
                    .divide(target, 6, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        long daysRemaining =
                java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        goal.getTargetDate()
                );

        BigDecimal requiredMonthlyContribution = BigDecimal.ZERO;

        if (remaining.compareTo(BigDecimal.ZERO) > 0
                && daysRemaining > 0) {

            long monthsRemaining =
                    Math.max(1, (daysRemaining + 29) / 30);

            requiredMonthlyContribution =
                    remaining.divide(
                            BigDecimal.valueOf(monthsRemaining),
                            2,
                            java.math.RoundingMode.HALF_UP
                    );
        }

        GoalStatus progressStatus;

        if (current.compareTo(target) >= 0) {

            progressStatus = GoalStatus.COMPLETED;

        } else if (daysRemaining <= 0) {

            progressStatus = GoalStatus.AT_RISK;

        } else {

            BigDecimal progressRatio =
                    current.divide(
                            target,
                            6,
                            java.math.RoundingMode.HALF_UP
                    );

            long totalDays =
                    java.time.temporal.ChronoUnit.DAYS.between(
                            goal.getTargetDate().minusDays(daysRemaining),
                            goal.getTargetDate()
                    );

            BigDecimal expectedProgress = BigDecimal.ZERO;

            if (totalDays > 0) {

                long elapsedDays = totalDays - daysRemaining;

                expectedProgress =
                        BigDecimal.valueOf(elapsedDays)
                                .divide(
                                        BigDecimal.valueOf(totalDays),
                                        6,
                                        java.math.RoundingMode.HALF_UP
                                );
            }

            progressStatus =
                    progressRatio.compareTo(expectedProgress) >= 0
                            ? GoalStatus.ON_TRACK
                            : GoalStatus.AT_RISK;
        }

        return new GoalProgressResponse(
                goal.getId(),
                goal.getGoalName(),
                target,
                current,
                remaining,
                progressPercentage,
                daysRemaining,
                requiredMonthlyContribution,
                progressStatus.name()
        );
    }

    public GoalContributionResponse addContribution(
            Long goalId,
            BigDecimal amount,
            LocalDate contributionDate) {

        User user = currentUserService.getCurrentUser();

        Goal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Contribution amount must be greater than zero");
        }

        if (contributionDate == null) {
            throw new IllegalArgumentException(
                    "Contribution date is required");
        }

        if (contributionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Contribution date cannot be in the future");
        }

        GoalContribution contribution = new GoalContribution();

        contribution.setGoal(goal);
        contribution.setAmount(amount);
        contribution.setContributionDate(contributionDate);

        GoalContribution saved =
                goalContributionRepository.save(contribution);

        goal.setCurrentAmount(
                goal.getCurrentAmount().add(amount)
        );

        goalRepository.save(goal);

        return new GoalContributionResponse(
                saved.getId(),
                goal.getId(),
                saved.getAmount(),
                saved.getContributionDate(),
                null
        );
    }

    public List<GoalContributionResponse> getContributions(Long goalId) {

        User user = currentUserService.getCurrentUser();

        Goal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found"));

        return goalContributionRepository
                .findByGoalOrderByContributionDateDesc(goal)
                .stream()
                .map(contribution ->
                        new GoalContributionResponse(
                                contribution.getId(),
                                goal.getId(),
                                contribution.getAmount(),
                                contribution.getContributionDate(),
                                contribution.getTransaction() != null
                                        ? contribution.getTransaction().getId()
                                        : null
                        )
                )
                .toList();
    }

    public GoalContributionResponse createContributionFromTransaction(
            FinancialTransaction transaction,
            Long goalId) {

        User user = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        Goal goal = goalRepository.findByIdAndUser(goalId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found"));

        if (goalContributionRepository.findByTransaction(transaction).isPresent()) {
            throw new IllegalStateException(
                    "Transaction has already been converted to a goal contribution");
        }

        if (transaction.getStatus() != TransactionStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed transactions can become goal contributions");
        }

        if (transaction.getType() != TransactionType.GOAL_CONTRIBUTION) {
            throw new IllegalStateException(
                    "Only goal contribution transactions can become contributions");
        }

        GoalContribution contribution = new GoalContribution();

        contribution.setGoal(goal);
        contribution.setAmount(transaction.getAmount());
        contribution.setContributionDate(
                transaction.getTransactionDateTime().toLocalDate()
        );
        contribution.setTransaction(transaction);

        GoalContribution saved =
                goalContributionRepository.save(contribution);

        goal.setCurrentAmount(
                goal.getCurrentAmount().add(transaction.getAmount())
        );

        goalRepository.save(goal);

        return new GoalContributionResponse(
                saved.getId(),
                goal.getId(),
                saved.getAmount(),
                saved.getContributionDate(),
                transaction.getId()
        );
    }

    public GoalPerformanceResponse getGoalPerformance() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        BigDecimal totalTargetAmount = goals.stream()
                .map(Goal::getTargetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrentAmount = goals.stream()
                .map(Goal::getCurrentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRemainingAmount =
                totalTargetAmount.subtract(totalCurrentAmount);

        if (totalRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalRemainingAmount = BigDecimal.ZERO;
        }

        BigDecimal overallProgressPercentage = BigDecimal.ZERO;

        if (totalTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
            overallProgressPercentage = totalCurrentAmount
                    .divide(
                            totalTargetAmount,
                            6,
                            java.math.RoundingMode.HALF_UP
                    )
                    .multiply(BigDecimal.valueOf(100));
        }

        long completed = goals.stream()
                .filter(goal ->
                        goal.getCurrentAmount()
                                .compareTo(goal.getTargetAmount()) >= 0)
                .count();

        long onTrack = goals.stream()
                .filter(goal ->
                        goal.getCurrentAmount()
                                .compareTo(goal.getTargetAmount()) < 0
                                && goal.getTargetDate()
                                .isAfter(LocalDate.now()))
                .count();

        long atRisk = goals.size() - completed - onTrack;

        return new GoalPerformanceResponse(
                (long) goals.size(),
                totalTargetAmount,
                totalCurrentAmount,
                totalRemainingAmount,
                overallProgressPercentage,
                completed,
                onTrack,
                atRisk
        );
    }

    private GoalResponse mapToResponse(Goal goal) {

        GoalResponse response = new GoalResponse();

        response.setId(goal.getId());
        response.setGoalName(goal.getGoalName());
        response.setTargetAmount(goal.getTargetAmount());
        response.setCurrentAmount(goal.getCurrentAmount());

        BigDecimal remainingAmount =
                goal.getTargetAmount().subtract(goal.getCurrentAmount());

        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            remainingAmount = BigDecimal.ZERO;
        }

        BigDecimal progressPercentage =
                goal.getCurrentAmount()
                        .divide(
                                goal.getTargetAmount(),
                                6,
                                java.math.RoundingMode.HALF_UP
                        )
                        .multiply(BigDecimal.valueOf(100));

        response.setRemainingAmount(remainingAmount);
        response.setProgressPercentage(progressPercentage);
        response.setTargetDate(goal.getTargetDate());
        response.setGoalStatus(goal.getGoalStatus());

        return response;
    }
}