package com.financeos.financeosbackend.goalintelligence.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalIntelligenceServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private CashFlowService cashFlowService;

    @InjectMocks
    private GoalIntelligenceService goalIntelligenceService;

    @Test
    void calculateProgressPercentage_ShouldReturnCorrectPercentage() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));

        BigDecimal result =
                goalIntelligenceService.calculateProgressPercentage(goal);

        assertEquals(
                new BigDecimal("25.00"),
                result
        );
    }

    @Test
    void calculateRemainingAmount_ShouldReturnRemainingAmount() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));

        BigDecimal result =
                goalIntelligenceService.calculateRemainingAmount(goal);

        assertEquals(
                new BigDecimal("75000"),
                result
        );
    }

    @Test
    void calculateRemainingAmount_ShouldReturnZeroWhenGoalExceeded() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("120000"));

        BigDecimal result =
                goalIntelligenceService.calculateRemainingAmount(goal);

        assertEquals(
                BigDecimal.ZERO,
                result
        );
    }

    @Test
    void getMyGoals_ShouldReturnCurrentUsersGoals() {

        var user = new com.financeos.financeosbackend.user.entity.User();

        Goal goal = new Goal();
        goal.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal));

        List<Goal> result =
                goalIntelligenceService.getMyGoals();

        assertEquals(1, result.size());
        assertEquals(goal, result.get(0));

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByUser(user);
    }

    @Test
    void calculateCurrentContributionCapacity_ShouldReturnCurrentSavings() {

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("30000"));

        BigDecimal result =
                goalIntelligenceService
                        .calculateCurrentContributionCapacity();

        assertEquals(
                new BigDecimal("30000"),
                result
        );

        verify(cashFlowService).calculateSavings();
    }

    @Test
    void calculateTimeToGoalInMonths_ShouldCalculateBasedOnContributionCapacity() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("120000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(12));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("20000"));

        BigDecimal result =
                goalIntelligenceService
                        .calculateTimeToGoalInMonths(goal);

        assertEquals(
                new BigDecimal("5.00"),
                result
        );

        verify(cashFlowService).calculateSavings();
    }

    @Test
    void calculateTimeToGoalInMonths_ShouldReturnZeroWhenNoContributionCapacity() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("120000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(12));

        when(cashFlowService.calculateSavings())
                .thenReturn(BigDecimal.ZERO);

        BigDecimal result =
                goalIntelligenceService
                        .calculateTimeToGoalInMonths(goal);

        assertEquals(
                BigDecimal.ZERO,
                result
        );
    }

    @Test
    void calculateGoalStatus_ShouldReturnCompletedWhenTargetReached() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("100000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        GoalStatus result =
                goalIntelligenceService.calculateGoalStatus(goal);

        assertEquals(
                GoalStatus.COMPLETED,
                result
        );

        verifyNoInteractions(cashFlowService);
    }

    @Test
    void calculateGoalStatus_ShouldReturnAtRiskWhenTargetDatePassed() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().minusDays(1));

        GoalStatus result =
                goalIntelligenceService.calculateGoalStatus(goal);

        assertEquals(
                GoalStatus.AT_RISK,
                result
        );

        verifyNoInteractions(cashFlowService);
    }

    @Test
    void calculateGoalStatus_ShouldReturnOnTrackWhenCapacityMeetsRequirement() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("20000"));

        GoalStatus result =
                goalIntelligenceService.calculateGoalStatus(goal);

        assertEquals(
                GoalStatus.ON_TRACK,
                result
        );
    }

    @Test
    void calculateGoalStatus_ShouldReturnAtRiskWhenCapacityIsInsufficient() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("5000"));

        GoalStatus result =
                goalIntelligenceService.calculateGoalStatus(goal);

        assertEquals(
                GoalStatus.AT_RISK,
                result
        );
    }

    @Test
    void isGoalAtRisk_ShouldReturnTrueWhenGoalIsAtRisk() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("5000"));

        assertTrue(
                goalIntelligenceService.isGoalAtRisk(goal)
        );
    }

    @Test
    void isGoalAtRisk_ShouldReturnFalseWhenGoalIsOnTrack() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("20000"));

        assertFalse(
                goalIntelligenceService.isGoalAtRisk(goal)
        );
    }

    @Test
    void isGoalAffordable_ShouldReturnTrueWhenCapacityMeetsRequirement() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("20000"));

        assertTrue(
                goalIntelligenceService.isGoalAffordable(goal)
        );
    }

    @Test
    void isGoalAffordable_ShouldReturnFalseWhenCapacityIsInsufficient() {

        Goal goal = new Goal();
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("20000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("5000"));

        assertFalse(
                goalIntelligenceService.isGoalAffordable(goal)
        );
    }
}