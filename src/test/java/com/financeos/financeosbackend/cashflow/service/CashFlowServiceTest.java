package com.financeos.financeosbackend.cashflow.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashFlowServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CurrentUserService currentUserService;

    private CashFlowService cashFlowService;

    private User user;

    @BeforeEach
    void setUp() {

        cashFlowService = new CashFlowService(
                incomeRepository,
                expenseRepository,
                currentUserService
        );

        user = new User();
        user.setId(1L);
    }

    @Test
    void calculateInflows_ShouldReturnTotalIncome() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("55000.00"));

        BigDecimal result =
                cashFlowService.calculateInflows();

        assertEquals(
                0,
                new BigDecimal("55000.00").compareTo(result)
        );
    }

    @Test
    void calculateOutflows_ShouldReturnTotalExpense() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("7800.00"));

        BigDecimal result =
                cashFlowService.calculateOutflows();

        assertEquals(
                0,
                new BigDecimal("7800.00").compareTo(result)
        );
    }

    @Test
    void calculateNetCashFlow_ShouldSubtractOutflowsFromInflows() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("55000.00"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("7800.00"));

        BigDecimal result =
                cashFlowService.calculateNetCashFlow();

        assertEquals(
                0,
                new BigDecimal("47200.00").compareTo(result)
        );
    }

    @Test
    void calculateSavings_ShouldEqualNetCashFlow() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("55000.00"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("7800.00"));

        BigDecimal result =
                cashFlowService.calculateSavings();

        assertEquals(
                0,
                new BigDecimal("47200.00").compareTo(result)
        );
    }

    @Test
    void calculateSavingsRate_ShouldCalculatePercentage() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("55000.00"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("7800.00"));

        BigDecimal result =
                cashFlowService.calculateSavingsRate();

        assertEquals(
                0,
                new BigDecimal("85.82").compareTo(result)
        );
    }

    @Test
    void calculateSavingsRate_ShouldReturnZeroWhenInflowsAreZero() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(BigDecimal.ZERO);

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(BigDecimal.ZERO);

        BigDecimal result =
                cashFlowService.calculateSavingsRate();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(result)
        );
    }

    @Test
    void calculateIncludedInflows_ShouldMatchInflows() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("55000.00"));

        BigDecimal result =
                cashFlowService.calculateIncludedInflows();

        assertEquals(
                0,
                new BigDecimal("55000.00").compareTo(result)
        );
    }

    @Test
    void calculateIncludedOutflows_ShouldMatchOutflows() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("7800.00"));

        BigDecimal result =
                cashFlowService.calculateIncludedOutflows();

        assertEquals(
                0,
                new BigDecimal("7800.00").compareTo(result)
        );
    }
}