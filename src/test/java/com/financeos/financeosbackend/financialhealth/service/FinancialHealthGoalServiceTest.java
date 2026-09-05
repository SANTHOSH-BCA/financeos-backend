package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthGoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private FinancialHealthGoalService financialHealthGoalService;

    @Test
    void calculateGoalHealth_ShouldReturnNoGoals_WhenUserHasNoGoals() {

        User user = new User();

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of());

        GoalHealthResponse response =
                financialHealthGoalService.calculateGoalHealth();

        assertEquals(0L, response.getTotalGoals());
        assertEquals(0L, response.getCompletedGoals());
        assertEquals(0L, response.getAtRiskGoals());
        assertEquals(0L, response.getOnTrackGoals());
        assertEquals(BigDecimal.ZERO, response.getTotalTargetAmount());
        assertEquals(BigDecimal.ZERO, response.getTotalCurrentAmount());
        assertEquals("NO_GOALS", response.getStatus());
    }

    @Test
    void calculateGoalHealth_ShouldReturnOnTrack_WhenAllGoalsAreOnTrack() {

        User user = new User();

        Goal goal1 = new Goal();
        goal1.setTargetAmount(new BigDecimal("100000"));
        goal1.setCurrentAmount(new BigDecimal("40000"));
        goal1.setGoalStatus(GoalStatus.ON_TRACK);

        Goal goal2 = new Goal();
        goal2.setTargetAmount(new BigDecimal("50000"));
        goal2.setCurrentAmount(new BigDecimal("20000"));
        goal2.setGoalStatus(GoalStatus.ON_TRACK);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal1, goal2));

        GoalHealthResponse response =
                financialHealthGoalService.calculateGoalHealth();

        assertEquals(2L, response.getTotalGoals());
        assertEquals(0L, response.getCompletedGoals());
        assertEquals(0L, response.getAtRiskGoals());
        assertEquals(2L, response.getOnTrackGoals());
        assertEquals(
                new BigDecimal("150000"),
                response.getTotalTargetAmount()
        );
        assertEquals(
                new BigDecimal("60000"),
                response.getTotalCurrentAmount()
        );
        assertEquals("ON_TRACK", response.getStatus());
    }

    @Test
    void calculateGoalHealth_ShouldReturnAtRisk_WhenAnyGoalIsAtRisk() {

        User user = new User();

        Goal goal1 = new Goal();
        goal1.setTargetAmount(new BigDecimal("100000"));
        goal1.setCurrentAmount(new BigDecimal("30000"));
        goal1.setGoalStatus(GoalStatus.ON_TRACK);

        Goal goal2 = new Goal();
        goal2.setTargetAmount(new BigDecimal("50000"));
        goal2.setCurrentAmount(new BigDecimal("10000"));
        goal2.setGoalStatus(GoalStatus.AT_RISK);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal1, goal2));

        GoalHealthResponse response =
                financialHealthGoalService.calculateGoalHealth();

        assertEquals(2L, response.getTotalGoals());
        assertEquals(0L, response.getCompletedGoals());
        assertEquals(1L, response.getAtRiskGoals());
        assertEquals(1L, response.getOnTrackGoals());
        assertEquals("AT_RISK", response.getStatus());
    }

    @Test
    void calculateGoalHealth_ShouldReturnCompleted_WhenAllGoalsAreCompleted() {

        User user = new User();

        Goal goal1 = new Goal();
        goal1.setTargetAmount(new BigDecimal("100000"));
        goal1.setCurrentAmount(new BigDecimal("100000"));
        goal1.setGoalStatus(GoalStatus.COMPLETED);

        Goal goal2 = new Goal();
        goal2.setTargetAmount(new BigDecimal("50000"));
        goal2.setCurrentAmount(new BigDecimal("50000"));
        goal2.setGoalStatus(GoalStatus.COMPLETED);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal1, goal2));

        GoalHealthResponse response =
                financialHealthGoalService.calculateGoalHealth();

        assertEquals(2L, response.getTotalGoals());
        assertEquals(2L, response.getCompletedGoals());
        assertEquals(0L, response.getAtRiskGoals());
        assertEquals(0L, response.getOnTrackGoals());
        assertEquals("COMPLETED", response.getStatus());
    }
}