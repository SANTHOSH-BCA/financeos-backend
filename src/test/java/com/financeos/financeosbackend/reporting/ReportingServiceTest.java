package com.financeos.financeosbackend.reporting;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.reporting.dto.FinancialReportResponse;
import com.financeos.financeosbackend.reporting.service.ReportingService;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ReportingService reportingService;

    @Test
    void generateFinancialReport_ShouldReturnExcellentReport() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(new BigDecimal("100000"));

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(new BigDecimal("60000"));

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("500000"));

        FinancialReportResponse response =
                reportingService.generateFinancialReport();

        assertNotNull(response);

        assertEquals(new BigDecimal("100000"),
                response.getTotalIncome());

        assertEquals(new BigDecimal("60000"),
                response.getTotalExpense());

        assertEquals(new BigDecimal("40000"),
                response.getTotalSavings());

        assertEquals(new BigDecimal("500000"),
                response.getNetWorth());

        assertEquals("EXCELLENT",
                response.getFinancialHealth());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
        verify(investmentRepository).getTotalInvestmentByUser(user);
    }

    @Test
    void generateFinancialReport_ShouldReturnNoData_WhenIncomeIsZero() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(incomeRepository.getTotalIncomeByUser(user))
                .thenReturn(BigDecimal.ZERO);

        when(expenseRepository.getTotalExpenseByUser(user))
                .thenReturn(BigDecimal.ZERO);

        when(investmentRepository.getTotalInvestmentByUser(user))
                .thenReturn(new BigDecimal("100000"));

        FinancialReportResponse response =
                reportingService.generateFinancialReport();

        assertNotNull(response);

        assertEquals(BigDecimal.ZERO,
                response.getTotalIncome());

        assertEquals(BigDecimal.ZERO,
                response.getTotalExpense());

        assertEquals(BigDecimal.ZERO,
                response.getTotalSavings());

        assertEquals(new BigDecimal("100000"),
                response.getNetWorth());

        assertEquals("NO DATA",
                response.getFinancialHealth());

        verify(currentUserService).getCurrentUser();
        verify(incomeRepository).getTotalIncomeByUser(user);
        verify(expenseRepository).getTotalExpenseByUser(user);
        verify(investmentRepository).getTotalInvestmentByUser(user);
    }
}