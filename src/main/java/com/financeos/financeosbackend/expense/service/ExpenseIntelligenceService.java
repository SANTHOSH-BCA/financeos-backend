package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.ExpenseIntelligenceResponse;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseIntelligenceService {

    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    public ExpenseIntelligenceService(
            ExpenseRepository expenseRepository,
            CurrentUserService currentUserService) {

        this.expenseRepository = expenseRepository;
        this.currentUserService = currentUserService;
    }

    public ExpenseIntelligenceResponse getExpenseIntelligence() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);
        YearMonth previousMonth = currentMonth.minusMonths(1);

        BigDecimal currentMonthExpense =
                getMonthTotal(expenses, currentMonth);

        BigDecimal previousMonthExpense =
                getMonthTotal(expenses, previousMonth);

        BigDecimal changeAmount =
                currentMonthExpense.subtract(previousMonthExpense);

        BigDecimal changePercentage =
                calculatePercentageChange(
                        previousMonthExpense,
                        currentMonthExpense
                );

        String direction =
                getDirection(changeAmount);

        Map<String, BigDecimal> categoryTrends =
                calculateCategoryTrends(
                        expenses,
                        currentMonth
                );

        List<ExpenseIntelligenceResponse.UnusualSpending>
                unusualSpending =
                detectUnusualSpending(
                        expenses,
                        currentMonth
                );

        ExpenseIntelligenceResponse.SpendingBehaviour
                spendingBehaviour =
                calculateSpendingBehaviour(
                        expenses,
                        currentMonth
                );

        Map<String, BigDecimal> merchantPatterns =
                calculateMerchantPatterns(
                        expenses,
                        currentMonth
                );

        Map<String, BigDecimal> locationPatterns =
                calculateLocationPatterns(
                        expenses,
                        currentMonth
                );

        ExpenseIntelligenceResponse.HistoricalComparison
                historicalComparison =
                calculateHistoricalComparison(
                        expenses,
                        currentMonth
                );

        return new ExpenseIntelligenceResponse(

                new ExpenseIntelligenceResponse.SpendingTrend(
                        currentMonthExpense,
                        previousMonthExpense,
                        changeAmount,
                        changePercentage,
                        direction
                ),

                categoryTrends,

                unusualSpending,

                spendingBehaviour,

                merchantPatterns,

                locationPatterns,

                historicalComparison
        );
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

    private Map<String, BigDecimal> calculateCategoryTrends(
            List<Expense> expenses,
            YearMonth currentMonth) {

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                expense.getExpenseDate()
                        ).equals(currentMonth)
                )
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        LinkedHashMap::new,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    private List<ExpenseIntelligenceResponse.UnusualSpending>
    detectUnusualSpending(
            List<Expense> expenses,
            YearMonth currentMonth) {

        List<Expense> historicalExpenses =
                expenses.stream()
                        .filter(expense -> {
                            YearMonth month =
                                    YearMonth.from(
                                            expense.getExpenseDate()
                                    );

                            return month.isBefore(currentMonth);
                        })
                        .toList();

        Map<String, BigDecimal> currentByCategory =
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

        Map<String, BigDecimal> historicalByCategory =
                historicalExpenses.stream()
                        .collect(Collectors.groupingBy(
                                Expense::getCategory,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Expense::getAmount,
                                        BigDecimal::add
                                )
                        ));

        return currentByCategory.entrySet()
                .stream()
                .map(entry -> {

                    String category = entry.getKey();

                    BigDecimal currentAmount =
                            entry.getValue();

                    BigDecimal historicalTotal =
                            historicalByCategory.getOrDefault(
                                    category,
                                    BigDecimal.ZERO
                            );

                    long historicalMonths =
                            historicalExpenses.stream()
                                    .filter(expense ->
                                            expense.getCategory()
                                                    .equals(category)
                                    )
                                    .map(expense ->
                                            YearMonth.from(
                                                    expense.getExpenseDate()
                                            )
                                    )
                                    .distinct()
                                    .count();

                    if (historicalMonths == 0) {
                        return null;
                    }

                    BigDecimal historicalAverage =
                            historicalTotal.divide(
                                    BigDecimal.valueOf(
                                            historicalMonths
                                    ),
                                    2,
                                    RoundingMode.HALF_UP
                            );

                    if (historicalAverage.compareTo(
                            BigDecimal.ZERO) == 0) {
                        return null;
                    }

                    BigDecimal increasePercentage =
                            calculatePercentageChange(
                                    historicalAverage,
                                    currentAmount
                            );

                    if (increasePercentage.compareTo(
                            BigDecimal.TEN) <= 0) {
                        return null;
                    }

                    String severity;

                    if (increasePercentage.compareTo(
                            BigDecimal.valueOf(50)) >= 0) {
                        severity = "HIGH";
                    } else if (increasePercentage.compareTo(
                            BigDecimal.valueOf(25)) >= 0) {
                        severity = "MEDIUM";
                    } else {
                        severity = "LOW";
                    }

                    return new ExpenseIntelligenceResponse
                            .UnusualSpending(
                            category,
                            currentAmount,
                            historicalAverage,
                            increasePercentage,
                            severity
                    );
                })
                .filter(item -> item != null)
                .sorted(
                        Comparator.comparing(
                                ExpenseIntelligenceResponse
                                        .UnusualSpending
                                        ::increasePercentage
                        ).reversed()
                )
                .toList();
    }

    private ExpenseIntelligenceResponse.SpendingBehaviour
    calculateSpendingBehaviour(
            List<Expense> expenses,
            YearMonth currentMonth) {

        List<Expense> currentExpenses =
                expenses.stream()
                        .filter(expense ->
                                YearMonth.from(
                                        expense.getExpenseDate()
                                ).equals(currentMonth)
                        )
                        .toList();

        if (currentExpenses.isEmpty()) {

            return new ExpenseIntelligenceResponse
                    .SpendingBehaviour(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    0L,
                    null,
                    "NO_DATA"
            );
        }

        BigDecimal total =
                currentExpenses.stream()
                        .map(Expense::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal average =
                total.divide(
                        BigDecimal.valueOf(
                                currentExpenses.size()
                        ),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal largest =
                currentExpenses.stream()
                        .map(Expense::getAmount)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);

        String dominantCategory =
                currentExpenses.stream()
                        .collect(Collectors.groupingBy(
                                Expense::getCategory,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Expense::getAmount,
                                        BigDecimal::add
                                )
                        ))
                        .entrySet()
                        .stream()
                        .max(
                                Map.Entry.comparingByValue()
                        )
                        .map(Map.Entry::getKey)
                        .orElse(null);

        String behaviour;

        if (currentExpenses.size() <= 5) {
            behaviour = "LOW_ACTIVITY";
        } else if (currentExpenses.size() <= 15) {
            behaviour = "MODERATE";
        } else {
            behaviour = "HIGH_ACTIVITY";
        }

        return new ExpenseIntelligenceResponse
                .SpendingBehaviour(
                average,
                largest,
                (long) currentExpenses.size(),
                dominantCategory,
                behaviour
        );
    }

    private Map<String, BigDecimal> calculateMerchantPatterns(
            List<Expense> expenses,
            YearMonth currentMonth) {

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                expense.getExpenseDate()
                        ).equals(currentMonth)
                )
                .filter(expense ->
                        expense.getTitle() != null
                                && !expense.getTitle()
                                .isBlank()
                )
                .collect(Collectors.groupingBy(
                        Expense::getTitle,
                        LinkedHashMap::new,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    private Map<String, BigDecimal> calculateLocationPatterns(
            List<Expense> expenses,
            YearMonth currentMonth) {

        /*
         * Expense currently does not contain location directly.
         * Location is available through the related transaction.
         */
        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(
                                expense.getExpenseDate()
                        ).equals(currentMonth)
                )
                .filter(expense ->
                        expense.getTransaction() != null
                                && expense.getTransaction()
                                .getLocation() != null
                                && !expense.getTransaction()
                                .getLocation()
                                .isBlank()
                )
                .collect(Collectors.groupingBy(
                        expense ->
                                expense.getTransaction()
                                        .getLocation(),
                        LinkedHashMap::new,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    private ExpenseIntelligenceResponse.HistoricalComparison
    calculateHistoricalComparison(
            List<Expense> expenses,
            YearMonth currentMonth) {

        BigDecimal currentMonthExpense =
                getMonthTotal(
                        expenses,
                        currentMonth
                );

        List<YearMonth> historicalMonths =
                expenses.stream()
                        .map(expense ->
                                YearMonth.from(
                                        expense.getExpenseDate()
                                )
                        )
                        .filter(month ->
                                month.isBefore(currentMonth)
                        )
                        .distinct()
                        .toList();

        if (historicalMonths.isEmpty()) {

            return new ExpenseIntelligenceResponse
                    .HistoricalComparison(
                    currentMonthExpense,
                    BigDecimal.ZERO,
                    currentMonthExpense,
                    BigDecimal.ZERO,
                    "INSUFFICIENT_DATA"
            );
        }

        BigDecimal historicalTotal =
                historicalMonths.stream()
                        .map(month ->
                                getMonthTotal(
                                        expenses,
                                        month
                                )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal historicalAverage =
                historicalTotal.divide(
                        BigDecimal.valueOf(
                                historicalMonths.size()
                        ),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal difference =
                currentMonthExpense.subtract(
                        historicalAverage
                );

        BigDecimal differencePercentage =
                calculatePercentageChange(
                        historicalAverage,
                        currentMonthExpense
                );

        String comparison;

        if (differencePercentage.compareTo(
                BigDecimal.TEN) > 0) {
            comparison = "ABOVE_HISTORICAL_AVERAGE";
        } else if (differencePercentage.compareTo(
                BigDecimal.TEN.negate()) < 0) {
            comparison = "BELOW_HISTORICAL_AVERAGE";
        } else {
            comparison = "AROUND_HISTORICAL_AVERAGE";
        }

        return new ExpenseIntelligenceResponse
                .HistoricalComparison(
                currentMonthExpense,
                historicalAverage,
                difference,
                differencePercentage,
                comparison
        );
    }

    private BigDecimal calculatePercentageChange(
            BigDecimal previous,
            BigDecimal current) {

        if (previous == null
                || previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return current
                .subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        previous,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private String getDirection(BigDecimal change) {

        if (change.compareTo(BigDecimal.ZERO) > 0) {
            return "INCREASED";
        }

        if (change.compareTo(BigDecimal.ZERO) < 0) {
            return "DECREASED";
        }

        return "UNCHANGED";
    }

    public BigDecimal getTotalExpense() {

        User user = currentUserService.getCurrentUser();

        return expenseRepository.getTotalExpenseByUser(user);
    }

    public Map<String, BigDecimal> getCategoryWiseExpense() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    public Map<String, BigDecimal> getMonthlyExpense() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getExpenseDate()
                                .withDayOfMonth(1)
                                .toString(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }
}