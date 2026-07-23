package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.BudgetWarningResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialHealthResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialInsightResponse;
import com.financeos.financeosbackend.analytics.dto.SavingsScoreResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.entity.Income;
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
class AnalyticsHealthTest {

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
    void getFinancialInsight_ShouldReturnExcellentStatus() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("100000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 1));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("50000"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.of(2026, 7, 5));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        FinancialInsightResponse response =
                analyticsService.getFinancialInsight();

        assertNotNull(response);
        assertEquals("EXCELLENT", response.getStatus());
        assertTrue(response.getMessage().contains("Excellent"));

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getBudgetWarning_ShouldReturnWarning() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("100000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 1));

        Expense expense = new Expense();
        expense.setCategory("Shopping");
        expense.setAmount(new BigDecimal("50000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 5));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        BudgetWarningResponse response =
                analyticsService.getBudgetWarning();

        assertNotNull(response);
        assertEquals("Shopping", response.getCategory());
        assertEquals(new BigDecimal("50000"), response.getSpentAmount());
        assertEquals(new BigDecimal("100000"), response.getIncome());
        assertEquals(new BigDecimal("50.00"), response.getPercentage());
        assertTrue(response.getWarning().contains("Warning"));

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getSavingsScore_ShouldReturnExcellentScore() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("100000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 1));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("50000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 5));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        SavingsScoreResponse response =
                analyticsService.getSavingsScore();

        assertNotNull(response);
        assertEquals(100, response.getScore());
        assertEquals(new BigDecimal("50.00"), response.getSavingsPercentage());
        assertTrue(response.getMessage().contains("Excellent"));

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getFinancialHealth_ShouldReturnExcellentHealth() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("100000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 1));

        Expense expense = new Expense();
        expense.setCategory("Shopping");
        expense.setAmount(new BigDecimal("50000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 5));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        FinancialHealthResponse response =
                analyticsService.getFinancialHealth();

        assertNotNull(response);

        assertEquals(70, response.getScore());
        assertEquals("AVERAGE", response.getStatus());
        assertTrue(response.getMessage().contains("stable"));

        verify(currentUserService, atLeastOnce()).getCurrentUser();
        verify(incomeRepository, atLeastOnce()).findByUser(user);
        verify(expenseRepository, atLeastOnce()).findByUser(user);
    }

}