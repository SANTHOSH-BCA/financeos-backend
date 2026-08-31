package com.financeos.financeosbackend.dashboard.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.dashboard.dto.DashboardResponse;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getDashboard_ShouldReturnDashboardSummary() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("50000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("20000"));

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("150000"));

        when(goalRepository.countGoalsByUser(user))
                .thenReturn(4L);

        when(expenseRepository.countExpensesByUser(user))
                .thenReturn(20L);

        when(incomeRepository.countIncomeByUser(user))
                .thenReturn(10L);

        when(investmentRepository.countInvestmentsByUser(user))
                .thenReturn(5L);

        DashboardResponse response =
                dashboardService.getDashboard();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("50000"),
                response.getTotalIncome());

        assertEquals(
                new BigDecimal("20000"),
                response.getTotalExpense());

        assertEquals(
                new BigDecimal("30000"),
                response.getNetSavings());

        assertEquals(
                30L,
                response.getTotalTransactions());

        assertEquals(
                new BigDecimal("150000"),
                response.getTotalInvestments());

        assertEquals(4L, response.getGoalCount());
        assertEquals(20L, response.getExpenseCount());
        assertEquals(10L, response.getIncomeCount());
        assertEquals(5L, response.getInvestmentCount());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
        verify(investmentRepository).getTotalInvestmentByUser(user);
        verify(goalRepository).countGoalsByUser(user);
        verify(expenseRepository).countExpensesByUser(user);
        verify(incomeRepository).countIncomeByUser(user);
        verify(investmentRepository).countInvestmentsByUser(user);
    }
}