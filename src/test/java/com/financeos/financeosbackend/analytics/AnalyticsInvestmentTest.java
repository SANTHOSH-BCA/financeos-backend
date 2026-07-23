package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.InvestmentDistributionResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentSummaryResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsInvestmentTest {

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
    void getInvestmentSummary_ShouldReturnTotalInvestment() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("500000"));

        InvestmentSummaryResponse response =
                analyticsService.getInvestmentSummary();

        assertNotNull(response);
        assertEquals(new BigDecimal("500000"),
                response.getTotalInvestment());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).getTotalInvestmentByUser(user);
    }

    @Test
    void getInvestmentDistribution_ShouldReturnDistribution() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("500000"));

        List<Object[]> repositoryResult = List.of(
                new Object[]{"Stocks", new BigDecimal("300000")},
                new Object[]{"Mutual Funds", new BigDecimal("200000")}
        );

        when(investmentRepository.getInvestmentDistributionByUser(user))
                .thenReturn(repositoryResult);

        List<InvestmentDistributionResponse> response =
                analyticsService.getInvestmentDistribution();

        assertEquals(2, response.size());

        InvestmentDistributionResponse first = response.get(0);

        assertEquals("Stocks", first.getInvestmentType());
        assertEquals(new BigDecimal("300000"), first.getAmount());
        assertEquals(new BigDecimal("60.00"), first.getPercentage());

        InvestmentDistributionResponse second = response.get(1);

        assertEquals("Mutual Funds", second.getInvestmentType());
        assertEquals(new BigDecimal("200000"), second.getAmount());
        assertEquals(new BigDecimal("40.00"), second.getPercentage());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).getTotalInvestmentByUser(user);
        verify(investmentRepository).getInvestmentDistributionByUser(user);
    }

    @Test
    void getInvestmentInsights_ShouldReturnInsights() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("500000"));

        List<Object[]> repositoryResult = List.of(
                new Object[]{"Stocks", new BigDecimal("400000")},
                new Object[]{"Gold", new BigDecimal("100000")}
        );

        when(investmentRepository.getInvestmentDistributionByUser(user))
                .thenReturn(repositoryResult);

        List<InvestmentInsightResponse> response =
                analyticsService.getInvestmentInsights();

        assertEquals(2, response.size());

        InvestmentInsightResponse first = response.get(0);

        assertEquals("Stocks", first.getInvestmentType());
        assertEquals(new BigDecimal("400000"), first.getAmount());
        assertEquals(new BigDecimal("80.00"), first.getPercentage());
        assertEquals("HIGH", first.getStatus());
        assertTrue(first.getRecommendation().contains("diversifying"));

        InvestmentInsightResponse second = response.get(1);

        assertEquals("Gold", second.getInvestmentType());
        assertEquals(new BigDecimal("100000"), second.getAmount());
        assertEquals(new BigDecimal("20.00"), second.getPercentage());
        assertEquals("LOW", second.getStatus());
        assertTrue(second.getRecommendation().contains("healthy diversification"));

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).getTotalInvestmentByUser(user);
        verify(investmentRepository).getInvestmentDistributionByUser(user);
    }

}