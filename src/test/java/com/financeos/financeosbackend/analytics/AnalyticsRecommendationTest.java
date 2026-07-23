package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.SmartRecommendationResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsRecommendationTest {

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
    void getSmartRecommendations_ShouldReturnRecommendations() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Emergency Fund");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("10000")); //10% -> LOW

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal));

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("100000"));

        List<Object[]> distribution = new ArrayList<>();

        distribution.add(new Object[]{
                "Stocks",
                new BigDecimal("80000")
        });
        when(investmentRepository.getInvestmentDistributionByUser(user))
                .thenReturn(distribution);

        List<SmartRecommendationResponse> response =
                analyticsService.getSmartRecommendations();

        assertEquals(2, response.size());

        SmartRecommendationResponse first = response.get(0);

        assertEquals("Goal Progress", first.getTitle());
        assertEquals("HIGH", first.getPriority());
        assertTrue(first.getRecommendation().contains("Prioritize"));

        SmartRecommendationResponse second = response.get(1);

        assertEquals("Investment Portfolio", second.getTitle());
        assertEquals("MEDIUM", second.getPriority());
        assertTrue(second.getRecommendation().contains("diversifying"));

        verify(currentUserService, atLeastOnce()).getCurrentUser();
        verify(goalRepository).findByUser(user);
        verify(investmentRepository).getTotalInvestmentByUser(user);
        verify(investmentRepository).getInvestmentDistributionByUser(user);
    }
}