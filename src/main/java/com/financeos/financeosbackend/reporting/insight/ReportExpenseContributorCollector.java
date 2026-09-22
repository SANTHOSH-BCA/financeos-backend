package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportExpenseContributorCollector {

    private final ExpenseRepository expenseRepository;
    private final ReportChangeAnalysisService changeAnalysisService;

    public ReportExpenseContributorCollector(
            ExpenseRepository expenseRepository,
            ReportChangeAnalysisService changeAnalysisService
    ) {
        this.expenseRepository = expenseRepository;
        this.changeAnalysisService = changeAnalysisService;
    }

    public List<ReportChangeContributorData> collect(
            User user,
            ReportPeriodResponse currentPeriod,
            ReportPeriodResponse previousPeriod
    ) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        if (currentPeriod == null || previousPeriod == null) {
            throw new IllegalArgumentException("Reporting periods are required");
        }

        Map<String, BigDecimal> currentCategories =
                collectCategoryTotals(
                        user,
                        currentPeriod
                );

        Map<String, BigDecimal> previousCategories =
                collectCategoryTotals(
                        user,
                        previousPeriod
                );

        return changeAnalysisService.identifyContributors(
                currentCategories,
                previousCategories
        );
    }

    private Map<String, BigDecimal> collectCategoryTotals(
            User user,
            ReportPeriodResponse period
    ) {
        List<Expense> expenses =
                expenseRepository.findByUserAndExpenseDateBetween(
                        user,
                        period.getStartDate(),
                        period.getEndDate()
                );

        Map<String, BigDecimal> totals =
                new HashMap<>();

        for (Expense expense : expenses) {

            FinancialTransaction transaction =
                    expense.getTransaction();

            if (transaction != null) {

                TransactionStatus status =
                        transaction.getStatus();

                if (status != TransactionStatus.CONFIRMED
                        && status != TransactionStatus.EDITED
                        && status != TransactionStatus.RECONCILED) {
                    continue;
                }
            }

            String category =
                    expense.getCategory();

            if (category == null || category.isBlank()) {
                category = "UNCATEGORIZED";
            }

            BigDecimal amount =
                    expense.getAmount() == null
                            ? BigDecimal.ZERO
                            : expense.getAmount();

            totals.merge(
                    category,
                    amount,
                    BigDecimal::add
            );
        }

        return totals;
    }
}