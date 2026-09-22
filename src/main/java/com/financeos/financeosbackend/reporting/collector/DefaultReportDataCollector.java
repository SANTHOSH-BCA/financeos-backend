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
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultReportDataCollector implements ReportDataCollector {

    private static final EnumSet<TransactionStatus> ACCEPTED_STATUSES =
            EnumSet.of(
                    TransactionStatus.CONFIRMED,
                    TransactionStatus.EDITED,
                    TransactionStatus.RECONCILED
            );

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    private final LiabilityRepository liabilityRepository;
    private final LiabilityRepaymentRepository liabilityRepaymentRepository;

    public DefaultReportDataCollector(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,

            LiabilityRepository liabilityRepository,
            LiabilityRepaymentRepository liabilityRepaymentRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;

        this.liabilityRepository = liabilityRepository;
        this.liabilityRepaymentRepository = liabilityRepaymentRepository;
    }

    @Override
    public ReportDataContext collect(
            User user,
            ReportPeriodResponse period
    ) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        if (period == null) {
            throw new IllegalArgumentException("Report period must not be null");
        }

        ReportDataContext context = new ReportDataContext();

        context.setIncome(collectIncome(user, period));
        context.setExpenses(collectExpenses(user, period));
        context.setCashFlow(
                collectCashFlow(
                        context.getIncome(),
                        context.getExpenses(),
                        user,
                        period
                )
        );

        return context;
    }

    private ReportIncomeData collectIncome(
            User user,
            ReportPeriodResponse period
    ) {
        List<Income> incomes =
                incomeRepository.findByUserAndIncomeDateBetween(
                        user,
                        period.getStartDate(),
                        period.getEndDate()
                );

        ReportIncomeData data = new ReportIncomeData();

        Map<String, BigDecimal> sourceBreakdown = new HashMap<>();

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal recurring = BigDecimal.ZERO;
        BigDecimal irregular = BigDecimal.ZERO;

        for (Income income : incomes) {

            BigDecimal amount = safeAmount(income.getAmount());

            total = total.add(amount);

            if (income.getPattern() == IncomePattern.RECURRING) {
                recurring = recurring.add(amount);
            } else {
                irregular = irregular.add(amount);
            }

            sourceBreakdown.merge(
                    income.getSource(),
                    amount,
                    BigDecimal::add
            );
        }

        data.setTotalIncome(scale(total));
        data.setRecurringIncome(scale(recurring));
        data.setIrregularIncome(scale(irregular));
        data.setSourceBreakdown(sourceBreakdown);

        return data;
    }

    private ReportExpenseData collectExpenses(
            User user,
            ReportPeriodResponse period
    ) {
        List<Expense> expenses =
                expenseRepository.findByUserAndExpenseDateBetween(
                        user,
                        period.getStartDate(),
                        period.getEndDate()
                );

        ReportExpenseData data = new ReportExpenseData();

        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();

        BigDecimal confirmedExpenses = BigDecimal.ZERO;

        for (Expense expense : expenses) {

            FinancialTransaction transaction =
                    expense.getTransaction();

            /*
             * A transaction-backed expense must have an accepted
             * transaction status before it is treated as confirmed.
             *
             * Legacy expenses without a transaction are retained as
             * valid expense records because the Expense domain itself
             * has no status field.
             */
            if (transaction != null
                    && !ACCEPTED_STATUSES.contains(transaction.getStatus())) {
                continue;
            }

            BigDecimal amount = safeAmount(expense.getAmount());

            confirmedExpenses = confirmedExpenses.add(amount);

            categoryBreakdown.merge(
                    expense.getCategory(),
                    amount,
                    BigDecimal::add
            );
        }

        data.setTotalConfirmedExpenses(scale(confirmedExpenses));
        data.setCategoryBreakdown(categoryBreakdown);

        /*
         * The current domain model does not contain a reliable
         * relationship proving that a specific expense came from
         * HELP_GIVEN. Therefore these values remain zero until that
         * domain relationship is explicitly represented.
         */
        data.setHelpAmounts(BigDecimal.ZERO);
        data.setConvertedHelpExpenses(BigDecimal.ZERO);

        return data;
    }

    private ReportCashFlowData collectCashFlow(
            ReportIncomeData income,
            ReportExpenseData expenses,
            User user,
            ReportPeriodResponse period
    ) {
        ReportCashFlowData data = new ReportCashFlowData();

        BigDecimal inflows = safeAmount(income.getTotalIncome());
        BigDecimal expenseOutflows =
                safeAmount(expenses.getTotalConfirmedExpenses());

        BigDecimal debtPaymentOutflows =
                calculateDebtPayments(user, period);

        BigDecimal outflows =
                expenseOutflows.add(debtPaymentOutflows);

        BigDecimal netCashFlow =
                inflows.subtract(outflows);

        BigDecimal savingsRate = BigDecimal.ZERO;

        if (inflows.compareTo(BigDecimal.ZERO) != 0) {
            savingsRate = netCashFlow
                    .multiply(BigDecimal.valueOf(100))
                    .divide(
                            inflows,
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        data.setInflows(scale(inflows));
        data.setOutflows(scale(outflows));
        data.setDebtPaymentOutflows(scale(debtPaymentOutflows));
        data.setNetCashFlow(scale(netCashFlow));
        data.setSavings(scale(netCashFlow));
        data.setSavingsRate(scale(savingsRate));

        return data;
    }

    private BigDecimal calculateDebtPayments(
            User user,
            ReportPeriodResponse period
    ) {
        BigDecimal total = BigDecimal.ZERO;

        List<Liability> liabilities =
                liabilityRepository.findAllByUser(user);

        for (Liability liability : liabilities) {

            List<LiabilityRepayment> repayments =
                    liabilityRepaymentRepository
                            .findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
                                    liability,
                                    period.getStartDate(),
                                    period.getEndDate()
                            );

            for (LiabilityRepayment repayment : repayments) {
                total = total.add(
                        safeAmount(repayment.getPaymentAmount())
                );
            }
        }

        return scale(total);
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null
                ? BigDecimal.ZERO
                : amount;
    }

    private BigDecimal scale(BigDecimal amount) {
        return safeAmount(amount)
                .setScale(2, RoundingMode.HALF_UP);
    }
}