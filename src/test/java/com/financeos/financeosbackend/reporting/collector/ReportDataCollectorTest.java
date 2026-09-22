package com.financeos.financeosbackend.reporting.collector;

import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.enums.IncomePattern;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
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
class ReportDataCollectorTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private LiabilityRepaymentRepository liabilityRepaymentRepository;

    @InjectMocks
    private DefaultReportDataCollector reportDataCollector;

    private final User user = createUser();

    private final ReportPeriodResponse period =
            new ReportPeriodResponse(
                    ReportPeriodType.MONTHLY,
                    LocalDate.of(2026, 8, 1),
                    LocalDate.of(2026, 8, 31)
            );

    @Test
    void collect_ShouldCalculateIncomeBreakdown() {

        Income salary = new Income();
        salary.setSource("Salary");
        salary.setAmount(new BigDecimal("100000"));
        salary.setIncomeDate(LocalDate.of(2026, 8, 1));
        salary.setPattern(IncomePattern.RECURRING);

        Income freelance = new Income();
        freelance.setSource("Freelance");
        freelance.setAmount(new BigDecimal("20000"));
        freelance.setIncomeDate(LocalDate.of(2026, 8, 15));
        freelance.setPattern(IncomePattern.ONE_TIME);

        when(incomeRepository.findByUserAndIncomeDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of(salary, freelance));

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of());

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        ReportDataContext result =
                reportDataCollector.collect(user, period);

        assertNotNull(result);
        assertNotNull(result.getIncome());

        assertEquals(
                new BigDecimal("120000.00"),
                result.getIncome().getTotalIncome()
        );

        assertEquals(
                new BigDecimal("100000.00"),
                result.getIncome().getRecurringIncome()
        );

        assertEquals(
                new BigDecimal("20000.00"),
                result.getIncome().getIrregularIncome()
        );

        assertEquals(
                0,
                new BigDecimal("100000.00")
                        .compareTo(
                                result.getIncome()
                                        .getSourceBreakdown()
                                        .get("Salary")
                        )
        );

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(
                                result.getIncome()
                                        .getSourceBreakdown()
                                        .get("Freelance")
                        )
        );
    }

    @Test
    void collect_ShouldIncludeOnlyAcceptedTransactionBackedExpenses() {

        Expense confirmedExpense = createExpense(
                "Food",
                new BigDecimal("5000"),
                TransactionStatus.CONFIRMED
        );

        Expense pendingExpense = createExpense(
                "Shopping",
                new BigDecimal("3000"),
                TransactionStatus.PENDING
        );

        Expense rejectedExpense = createExpense(
                "Travel",
                new BigDecimal("2000"),
                TransactionStatus.REJECTED
        );

        when(incomeRepository.findByUserAndIncomeDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of());

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(
                List.of(
                        confirmedExpense,
                        pendingExpense,
                        rejectedExpense
                )
        );

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        ReportDataContext result =
                reportDataCollector.collect(user, period);

        assertEquals(
                new BigDecimal("5000.00"),
                result.getExpenses().getTotalConfirmedExpenses()
        );

        assertEquals(
                0,
                new BigDecimal("5000.00")
                        .compareTo(
                                result.getExpenses()
                                        .getCategoryBreakdown()
                                        .get("Food")
                        )
        );

        assertFalse(
                result.getExpenses()
                        .getCategoryBreakdown()
                        .containsKey("Shopping")
        );

        assertFalse(
                result.getExpenses()
                        .getCategoryBreakdown()
                        .containsKey("Travel")
        );
    }

    @Test
    void collect_ShouldCalculateCashFlowIncludingDebtPayments() {

        Income income = new Income();
        income.setSource("Salary");
        income.setAmount(new BigDecimal("100000"));
        income.setIncomeDate(LocalDate.of(2026, 8, 1));
        income.setPattern(IncomePattern.RECURRING);

        Expense expense = createExpense(
                "Living",
                new BigDecimal("60000"),
                TransactionStatus.CONFIRMED
        );

        Liability liability = new Liability();

        LiabilityRepayment repayment = new LiabilityRepayment();
        repayment.setLiability(liability);
        repayment.setPaymentAmount(new BigDecimal("10000"));
        repayment.setPrincipalAmount(new BigDecimal("8000"));
        repayment.setInterestAmount(new BigDecimal("2000"));
        repayment.setRepaymentDate(LocalDate.of(2026, 8, 15));

        when(incomeRepository.findByUserAndIncomeDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of(income));

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of(expense));

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(liabilityRepaymentRepository
                .findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
                        liability,
                        period.getStartDate(),
                        period.getEndDate()
                ))
                .thenReturn(List.of(repayment));

        ReportDataContext result =
                reportDataCollector.collect(user, period);

        ReportCashFlowData cashFlow = result.getCashFlow();

        assertEquals(
                new BigDecimal("100000.00"),
                cashFlow.getInflows()
        );

        assertEquals(
                new BigDecimal("70000.00"),
                cashFlow.getOutflows()
        );

        assertEquals(
                new BigDecimal("10000.00"),
                cashFlow.getDebtPaymentOutflows()
        );

        assertEquals(
                new BigDecimal("30000.00"),
                cashFlow.getNetCashFlow()
        );

        assertEquals(
                new BigDecimal("30000.00"),
                cashFlow.getSavings()
        );

        assertEquals(
                new BigDecimal("30.00"),
                cashFlow.getSavingsRate()
        );
    }

    @Test
    void collect_ShouldKeepHelpAmountsZero_WhenDomainCannotProveConversion() {

        when(incomeRepository.findByUserAndIncomeDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of());

        when(expenseRepository.findByUserAndExpenseDateBetween(
                user,
                period.getStartDate(),
                period.getEndDate()
        )).thenReturn(List.of());

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of());

        ReportDataContext result =
                reportDataCollector.collect(user, period);

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        result.getExpenses().getHelpAmounts()
                )
        );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        result.getExpenses().getConvertedHelpExpenses()
                )
        );
    }

    @Test
    void collect_ShouldRejectNullUser() {

        assertThrows(
                IllegalArgumentException.class,
                () -> reportDataCollector.collect(null, period)
        );
    }

    @Test
    void collect_ShouldRejectNullPeriod() {

        assertThrows(
                IllegalArgumentException.class,
                () -> reportDataCollector.collect(user, null)
        );
    }

    private User createUser() {
        User testUser = new User();
        testUser.setEmail("test@financeos.com");
        return testUser;
    }

    private Expense createExpense(
            String category,
            BigDecimal amount,
            TransactionStatus status
    ) {
        Expense expense = new Expense();

        expense.setTitle(category);
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setExpenseDate(LocalDate.of(2026, 8, 10));
        expense.setUser(user);

        FinancialTransaction transaction =
                new FinancialTransaction();

        transaction.setStatus(status);

        expense.setTransaction(transaction);

        return expense;
    }
}