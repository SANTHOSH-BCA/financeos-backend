package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.ExpenseInsightResponse;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseInsightService {

    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    public ExpenseInsightService(
            ExpenseRepository expenseRepository,
            CurrentUserService currentUserService) {

        this.expenseRepository = expenseRepository;
        this.currentUserService = currentUserService;
    }

    public List<ExpenseInsightResponse> getExpenseInsights() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        List<ExpenseInsightResponse> insights =
                new ArrayList<>();

        if (expenses.isEmpty()) {
            insights.add(new ExpenseInsightResponse(
                    "NO_DATA",
                    "Not enough expense data to generate insights."
            ));

            return insights;
        }

        YearMonth currentMonth =
                YearMonth.now();

        YearMonth previousMonth =
                currentMonth.minusMonths(1);

        BigDecimal currentTotal =
                getMonthTotal(expenses, currentMonth);

        BigDecimal previousTotal =
                getMonthTotal(expenses, previousMonth);

        if (previousTotal.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal changePercentage =
                    currentTotal
                            .subtract(previousTotal)
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    previousTotal,
                                    2,
                                    RoundingMode.HALF_UP
                            );

            if (changePercentage.compareTo(BigDecimal.TEN) > 0) {

                insights.add(new ExpenseInsightResponse(
                        "SPENDING_INCREASE",
                        "Your spending increased by "
                                + changePercentage
                                + "% compared with last month."
                ));

            } else if (changePercentage.compareTo(
                    BigDecimal.TEN.negate()) < 0) {

                insights.add(new ExpenseInsightResponse(
                        "SPENDING_DECREASE",
                        "Your spending decreased by "
                                + changePercentage.abs()
                                + "% compared with last month."
                ));
            }
        }

        Map<String, BigDecimal> categoryTotals =
                expenses.stream()
                        .filter(expense ->
                                YearMonth.from(
                                        expense.getExpenseDate()
                                ).equals(currentMonth)
                        )
                        .collect(Collectors.groupingBy(
                                Expense::getCategory,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Expense::getAmount,
                                        BigDecimal::add
                                )
                        ));

        categoryTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry ->
                        insights.add(
                                new ExpenseInsightResponse(
                                        "TOP_CATEGORY",
                                        "Your highest spending category this month is "
                                                + entry.getKey()
                                                + " with an expense of "
                                                + entry.getValue()
                                                + "."
                                )
                        )
                );

        List<Expense> currentExpenses =
                expenses.stream()
                        .filter(expense ->
                                YearMonth.from(
                                        expense.getExpenseDate()
                                ).equals(currentMonth)
                        )
                        .toList();

        currentExpenses.stream()
                .max(Comparator.comparing(
                        Expense::getAmount
                ))
                .ifPresent(expense ->
                        insights.add(
                                new ExpenseInsightResponse(
                                        "LARGEST_EXPENSE",
                                        "Your largest expense this month is "
                                                + expense.getTitle()
                                                + " for "
                                                + expense.getAmount()
                                                + "."
                                )
                        )
                );

        return insights;
    }

    private BigDecimal getMonthTotal(
            List<Expense> expenses,
            YearMonth month) {

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                expense.getExpenseDate()
                        ).equals(month)
                )
                .map(Expense::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}