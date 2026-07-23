package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.GoalInsightResponse;
import com.financeos.financeosbackend.analytics.dto.GoalProgressResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
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
class AnalyticsGoalTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getGoalProgress_ShouldReturnProgress() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Emergency Fund");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("40000"));
        goal.setTargetDate(LocalDate.of(2027, 1, 1));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal));

        List<GoalProgressResponse> response =
                analyticsService.getGoalProgress();

        assertEquals(1, response.size());

        GoalProgressResponse progress = response.get(0);

        assertEquals("Emergency Fund", progress.getGoalName());
        assertEquals(new BigDecimal("100000"), progress.getTargetAmount());
        assertEquals(new BigDecimal("40000"), progress.getCurrentAmount());
        assertEquals(new BigDecimal("40.00"), progress.getCompletionPercentage());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByUser(user);
    }

    @Test
    void getGoalInsights_ShouldReturnInsights() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal1 = new Goal();
        goal1.setGoalName("Emergency Fund");
        goal1.setTargetAmount(new BigDecimal("100000"));
        goal1.setCurrentAmount(new BigDecimal("95000"));

        Goal goal2 = new Goal();
        goal2.setGoalName("Vacation");
        goal2.setTargetAmount(new BigDecimal("50000"));
        goal2.setCurrentAmount(new BigDecimal("25000"));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal1, goal2));

        List<GoalInsightResponse> response =
                analyticsService.getGoalInsights();

        assertEquals(2, response.size());

        GoalInsightResponse first = response.get(0);

        assertEquals("Emergency Fund", first.getGoalName());
        assertEquals(new BigDecimal("95.00"), first.getCompletionPercentage());
        assertEquals("EXCELLENT", first.getStatus());
        assertTrue(first.getRecommendation().contains("almost"));

        GoalInsightResponse second = response.get(1);

        assertEquals("Vacation", second.getGoalName());
        assertEquals(new BigDecimal("50.00"), second.getCompletionPercentage());
        assertEquals("AVERAGE", second.getStatus());
        assertTrue(second.getRecommendation().contains("Increase"));

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByUser(user);
    }

}