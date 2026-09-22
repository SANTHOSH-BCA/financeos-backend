package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportExpenseContributorCollectorTest {

    private ExpenseRepository expenseRepository;
    private ReportChangeAnalysisService changeAnalysisService;

    private ReportExpenseContributorCollector collector;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        changeAnalysisService = mock(ReportChangeAnalysisService.class);

        collector = new ReportExpenseContributorCollector(
                expenseRepository,
                changeAnalysisService
        );
    }

    @Test
    void shouldCollectOnlyConfirmedExpenseStatuses() {

        User user = mock(User.class);

        ReportPeriodResponse currentPeriod =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        ReportPeriodResponse previousPeriod =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );

        Expense confirmed = createExpense(
                "Food",
                "100",
                TransactionStatus.CONFIRMED
        );

        Expense edited = createExpense(
                "Food",
                "200",
                TransactionStatus.EDITED
        );

        Expense reconciled = createExpense(
                "Travel",
                "300",
                TransactionStatus.RECONCILED
        );

        Expense pending = createExpense(
                "Food",
                "1000",
                TransactionStatus.PENDING
        );

        Expense rejected = createExpense(
                "Travel",
                "2000",
                TransactionStatus.REJECTED
        );

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                currentPeriod.getStartDate(),
                currentPeriod.getEndDate()
        )).thenReturn(
                java.util.List.of(
                        confirmed,
                        edited,
                        reconciled,
                        pending,
                        rejected
                )
        );

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                previousPeriod.getStartDate(),
                previousPeriod.getEndDate()
        )).thenReturn(
                java.util.List.of()
        );

        when(changeAnalysisService.identifyContributors(
                anyMap(),
                anyMap()
        )).thenReturn(java.util.List.of());

        collector.collect(
                user,
                currentPeriod,
                previousPeriod
        );

        var currentCaptor =
                org.mockito.ArgumentCaptor.forClass(Map.class);

        var previousCaptor =
                org.mockito.ArgumentCaptor.forClass(Map.class);

        verify(changeAnalysisService).identifyContributors(
                currentCaptor.capture(),
                previousCaptor.capture()
        );

        Map<?, ?> current =
                currentCaptor.getValue();

        assertEquals(
                new BigDecimal("300"),
                current.get("Food")
        );

        assertEquals(
                new BigDecimal("300"),
                current.get("Travel")
        );

        assertFalse(current.containsValue(
                new BigDecimal("1000")
        ));

        assertFalse(current.containsValue(
                new BigDecimal("2000")
        ));
    }

    @Test
    void shouldRetainLegacyExpenseWithoutTransaction() {

        User user = mock(User.class);

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        Expense legacyExpense = new Expense();
        legacyExpense.setCategory("Food");
        legacyExpense.setAmount(new BigDecimal("500"));
        legacyExpense.setTransaction(null);

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(
                java.util.List.of(legacyExpense)
        );

        when(changeAnalysisService.identifyContributors(
                anyMap(),
                anyMap()
        )).thenReturn(java.util.List.of());

        collector.collect(
                user,
                period,
                period
        );

        var captor =
                org.mockito.ArgumentCaptor.forClass(Map.class);

        verify(changeAnalysisService).identifyContributors(
                captor.capture(),
                anyMap()
        );

        assertEquals(
                new BigDecimal("500"),
                captor.getValue().get("Food")
        );
    }

    private Expense createExpense(
            String category,
            String amount,
            TransactionStatus status
    ) {
        Expense expense = new Expense();

        expense.setCategory(category);
        expense.setAmount(new BigDecimal(amount));

        FinancialTransaction transaction =
                new FinancialTransaction();

        transaction.setStatus(status);

        expense.setTransaction(transaction);

        return expense;
    }
}