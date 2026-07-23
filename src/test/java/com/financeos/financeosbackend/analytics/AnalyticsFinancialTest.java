package com.financeos.financeosbackend.analytics;

import com.financeos.financeosbackend.analytics.dto.CashFlowResponse;
import com.financeos.financeosbackend.analytics.dto.ExpenseCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyFinancialSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyIncomeExpenseResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlySavingsResponse;
import com.financeos.financeosbackend.analytics.dto.NetWorthResponse;
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
class AnalyticsFinancialTest {

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
    void getMonthlyIncomeExpense_ShouldReturnMonthlySummary() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("50000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 10));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("20000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 15));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        List<MonthlyIncomeExpenseResponse> response =
                analyticsService.getMonthlyIncomeExpense();

        assertEquals(1, response.size());

        MonthlyIncomeExpenseResponse month = response.get(0);

        assertEquals("2026-07", month.getMonth());
        assertEquals(new BigDecimal("50000"), month.getTotalIncome());
        assertEquals(new BigDecimal("20000"), month.getTotalExpense());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getExpenseByCategory_ShouldReturnCategoryTotals() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense e1 = new Expense();
        e1.setCategory("Food");
        e1.setAmount(new BigDecimal("500"));

        Expense e2 = new Expense();
        e2.setCategory("Food");
        e2.setAmount(new BigDecimal("1000"));

        Expense e3 = new Expense();
        e3.setCategory("Travel");
        e3.setAmount(new BigDecimal("2000"));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(e1, e2, e3));

        List<ExpenseCategoryResponse> response =
                analyticsService.getExpenseByCategory();

        assertEquals(2, response.size());

        ExpenseCategoryResponse food = response.stream()
                .filter(r -> r.getCategory().equals("Food"))
                .findFirst()
                .orElseThrow();

        ExpenseCategoryResponse travel = response.stream()
                .filter(r -> r.getCategory().equals("Travel"))
                .findFirst()
                .orElseThrow();

        assertEquals(new BigDecimal("1500"), food.getTotalAmount());
        assertEquals(new BigDecimal("2000"), travel.getTotalAmount());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getMonthlySavings_ShouldReturnMonthlySavings() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Income income = new Income();
        income.setAmount(new BigDecimal("60000"));
        income.setIncomeDate(LocalDate.of(2026, 7, 5));

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("25000"));
        expense.setExpenseDate(LocalDate.of(2026, 7, 10));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.findByUser(user))
                .thenReturn(List.of(income));

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        List<MonthlySavingsResponse> response =
                analyticsService.getMonthlySavings();

        assertEquals(1, response.size());

        MonthlySavingsResponse summary = response.get(0);

        assertEquals("2026-07", summary.getMonth());
        assertEquals(new BigDecimal("60000"), summary.getIncome());
        assertEquals(new BigDecimal("25000"), summary.getExpense());
        assertEquals(new BigDecimal("35000"), summary.getSavings());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).findByUser(user);
        verify(expenseRepository).findByUser(user);
    }

    @Test
    void getCashFlow_ShouldReturnCashFlow() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("80000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("30000"));

        CashFlowResponse response =
                analyticsService.getCashFlow();

        assertNotNull(response);
        assertEquals(new BigDecimal("80000"), response.getTotalIncome());
        assertEquals(new BigDecimal("30000"), response.getTotalExpense());
        assertEquals(new BigDecimal("50000"), response.getNetCashFlow());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
    }

    @Test
    void getNetWorth_ShouldReturnNetWorth() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("450000"));

        NetWorthResponse response =
                analyticsService.getNetWorth();

        assertNotNull(response);
        assertEquals(new BigDecimal("450000"), response.getTotalAssets());
        assertEquals(BigDecimal.ZERO, response.getTotalLiabilities());
        assertEquals(new BigDecimal("450000"), response.getNetWorth());

        verify(currentUserService).getCurrentUser();
        verify(investmentRepository).getTotalInvestmentByUser(user);
    }

    @Test
    void getMonthlyFinancialSummary_ShouldReturnSummary() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("100000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("70000"));

        MonthlyFinancialSummaryResponse response =
                analyticsService.getMonthlyFinancialSummary();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("100000"),
                response.getIncome());

        assertEquals(
                new BigDecimal("70000"),
                response.getExpense());

        assertEquals(
                new BigDecimal("30000"),
                response.getSavings());

        assertEquals(
                new BigDecimal("30.00"),
                response.getSavingsRate());

        assertEquals(
                "EXCELLENT",
                response.getFinancialHealth());

        assertTrue(
                response.getSummary().contains("Excellent"));

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
    }

}