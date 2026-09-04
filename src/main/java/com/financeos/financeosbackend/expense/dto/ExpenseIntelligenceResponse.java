package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ExpenseIntelligenceResponse(

        SpendingTrend spendingTrend,

        Map<String, BigDecimal> categoryTrends,

        List<UnusualSpending> unusualSpending,

        SpendingBehaviour spendingBehaviour,

        Map<String, BigDecimal> merchantPatterns,

        Map<String, BigDecimal> locationPatterns,

        HistoricalComparison historicalComparison
) {

    public record SpendingTrend(
            BigDecimal currentMonth,
            BigDecimal previousMonth,
            BigDecimal changeAmount,
            BigDecimal changePercentage,
            String direction
    ) {
    }

    public record UnusualSpending(
            String category,
            BigDecimal currentAmount,
            BigDecimal historicalAverage,
            BigDecimal increasePercentage,
            String severity
    ) {
    }

    public record SpendingBehaviour(
            BigDecimal averageExpenseAmount,
            BigDecimal largestExpenseAmount,
            Long expenseCount,
            String dominantCategory,
            String behaviour
    ) {
    }

    public record HistoricalComparison(
            BigDecimal currentMonthExpense,
            BigDecimal averageHistoricalMonthlyExpense,
            BigDecimal differenceAmount,
            BigDecimal differencePercentage,
            String comparison
    ) {
    }
}