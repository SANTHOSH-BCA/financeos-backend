package com.financeos.financeosbackend.reporting.comparison;

import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportComparisonDataCollector {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final LiabilityRepository liabilityRepository;
    private final LiabilityRepaymentRepository liabilityRepaymentRepository;
    private final ReportComparisonService comparisonService;

    public ReportComparisonDataCollector(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            LiabilityRepository liabilityRepository,
            LiabilityRepaymentRepository liabilityRepaymentRepository,
            ReportComparisonService comparisonService
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.liabilityRepository = liabilityRepository;
        this.liabilityRepaymentRepository = liabilityRepaymentRepository;
        this.comparisonService = comparisonService;
    }

    public ReportComparisonData collect(
            User user,
            ReportPeriodResponse currentPeriod
    ) {
        validate(user, currentPeriod);

        ReportPeriodResponse previousPeriod =
                comparisonService.resolvePreviousPeriod(currentPeriod);

        PeriodFinancialData current =
                collectPeriod(user, currentPeriod);

        PeriodFinancialData previous =
                collectPeriod(user, previousPeriod);

        ReportComparisonData result =
                new ReportComparisonData();

        result.setComparisonAvailable(true);
        result.setCurrentPeriod(currentPeriod);
        result.setPreviousPeriod(previousPeriod);

        result.setIncome(
                comparisonService.calculateChange(
                        current.income(),
                        previous.income()
                )
        );

        result.setExpenses(
                comparisonService.calculateChange(
                        current.expenses(),
                        previous.expenses()
                )
        );

        result.setSavings(
                comparisonService.calculateChange(
                        current.savings(),
                        previous.savings()
                )
        );

        /*
         * Investment and net-worth historical comparison are not
         * collected here because the current domain cannot reliably
         * reconstruct complete historical period-end values.
         */
        result.setInvestments(null);
        result.setNetWorth(null);

        return result;
    }

    private PeriodFinancialData collectPeriod(
            User user,
            ReportPeriodResponse period
    ) {
        BigDecimal income =
                safe(
                        incomeRepository
                                .getTotalIncomeByUserAndDateRange(
                                        user,
                                        period.getStartDate(),
                                        period.getEndDate()
                                )
                );

        BigDecimal expenses =
                safe(
                        expenseRepository
                                .getTotalExpenseByUserAndDateRange(
                                        user,
                                        period.getStartDate(),
                                        period.getEndDate()
                                )
                );

        BigDecimal debtPayments =
                calculateDebtPayments(
                        user,
                        period.getStartDate(),
                        period.getEndDate()
                );

        BigDecimal savings =
                income
                        .subtract(expenses)
                        .subtract(debtPayments)
                        .setScale(2, RoundingMode.HALF_UP);

        return new PeriodFinancialData(
                income,
                expenses,
                debtPayments,
                savings
        );
    }

    private BigDecimal calculateDebtPayments(
            User user,
            LocalDate startDate,
            LocalDate endDate
    ) {
        BigDecimal total = BigDecimal.ZERO;

        List<Liability> liabilities =
                liabilityRepository.findAllByUser(user);

        for (Liability liability : liabilities) {

            List<LiabilityRepayment> repayments =
                    liabilityRepaymentRepository
                            .findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
                                    liability,
                                    startDate,
                                    endDate
                            );

            for (LiabilityRepayment repayment : repayments) {

                if (repayment.getPaymentAmount() != null) {
                    total = total.add(
                            repayment.getPaymentAmount()
                    );
                }
            }
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO
                : value.setScale(2, RoundingMode.HALF_UP);
    }

    private void validate(
            User user,
            ReportPeriodResponse period
    ) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null."
            );
        }

        if (period == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null."
            );
        }

        if (period.getStartDate() == null
                || period.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Report period dates must not be null."
            );
        }
    }

    private record PeriodFinancialData(
            BigDecimal income,
            BigDecimal expenses,
            BigDecimal debtPayments,
            BigDecimal savings
    ) {
    }
}