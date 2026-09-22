package com.financeos.financeosbackend.reporting.comparison;

import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportComparisonDataCollectorTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private LiabilityRepaymentRepository liabilityRepaymentRepository;

    @Mock
    private ReportComparisonService comparisonService;

    @InjectMocks
    private ReportComparisonDataCollector collector;

    @Test
    void shouldCollectIncomeExpenseAndSavingsComparison() {
        User user = new User();

        ReportPeriodResponse current =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        ReportPeriodResponse previous =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );

        when(comparisonService.resolvePreviousPeriod(current))
                .thenReturn(previous);

        when(
                incomeRepository.getTotalIncomeByUserAndDateRange(
                        user,
                        current.getStartDate(),
                        current.getEndDate()
                )
        ).thenReturn(new BigDecimal("50000"));

        when(
                expenseRepository.getTotalExpenseByUserAndDateRange(
                        user,
                        current.getStartDate(),
                        current.getEndDate()
                )
        ).thenReturn(new BigDecimal("20000"));

        when(
                incomeRepository.getTotalIncomeByUserAndDateRange(
                        user,
                        previous.getStartDate(),
                        previous.getEndDate()
                )
        ).thenReturn(new BigDecimal("45000"));

        when(
                expenseRepository.getTotalExpenseByUserAndDateRange(
                        user,
                        previous.getStartDate(),
                        previous.getEndDate()
                )
        ).thenReturn(new BigDecimal("18000"));

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        ReportMetricChangeData incomeChange =
                comparisonChange(
                        new BigDecimal("5000")
                );

        ReportMetricChangeData expenseChange =
                comparisonChange(
                        new BigDecimal("2000")
                );

        ReportMetricChangeData savingsChange =
                comparisonChange(
                        new BigDecimal("3000")
                );

        when(
                comparisonService.calculateChange(
                        new BigDecimal("50000.00"),
                        new BigDecimal("45000.00")
                )
        ).thenReturn(incomeChange);

        when(
                comparisonService.calculateChange(
                        new BigDecimal("20000.00"),
                        new BigDecimal("18000.00")
                )
        ).thenReturn(expenseChange);

        when(
                comparisonService.calculateChange(
                        new BigDecimal("30000.00"),
                        new BigDecimal("27000.00")
                )
        ).thenReturn(savingsChange);

        ReportComparisonData result =
                collector.collect(user, current);

        assertNotNull(result);
        assertTrue(result.isComparisonAvailable());

        assertEquals(current, result.getCurrentPeriod());
        assertEquals(previous, result.getPreviousPeriod());

        assertSame(incomeChange, result.getIncome());
        assertSame(expenseChange, result.getExpenses());
        assertSame(savingsChange, result.getSavings());

        assertNull(result.getInvestments());
        assertNull(result.getNetWorth());
    }

    @Test
    void shouldRejectNullUser() {
        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> collector.collect(null, period)
        );
    }

    @Test
    void shouldRejectNullPeriod() {
        assertThrows(
                IllegalArgumentException.class,
                () -> collector.collect(new User(), null)
        );
    }

    private ReportMetricChangeData comparisonChange(
            BigDecimal absoluteChange
    ) {
        ReportMetricChangeData data =
                new ReportMetricChangeData();

        data.setAbsoluteChange(absoluteChange);

        return data;
    }
}