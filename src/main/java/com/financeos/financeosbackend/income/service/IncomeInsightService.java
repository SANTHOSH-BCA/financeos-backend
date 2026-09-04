package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.income.dto.IncomeInsightResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.enums.IncomePattern;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomeInsightService {

    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;

    public IncomeInsightService(
            IncomeRepository incomeRepository,
            CurrentUserService currentUserService) {
        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
    }

    public List<IncomeInsightResponse> getIncomeInsights() {

        User user = currentUserService.getCurrentUser();

        List<Income> incomes = incomeRepository.findByUser(user);

        List<IncomeInsightResponse> insights = new ArrayList<>();

        if (incomes.isEmpty()) {
            insights.add(new IncomeInsightResponse(
                    "INSUFFICIENT_DATA",
                    "There is not enough income history to generate meaningful insights."
            ));

            return insights;
        }

        addStabilityInsight(incomes, insights);
        addPrimarySourceInsight(incomes, insights);
        addIrregularIncomeInsight(incomes, insights);
        addRecurringIncomeInsight(incomes, insights);

        return insights;
    }

    private void addStabilityInsight(
            List<Income> incomes,
            List<IncomeInsightResponse> insights) {

        Map<YearMonth, BigDecimal> monthlyIncome = incomes.stream()
                .collect(Collectors.groupingBy(
                        income -> YearMonth.from(income.getIncomeDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Income::getAmount,
                                BigDecimal::add
                        )
                ));

        if (monthlyIncome.size() < 3) {
            return;
        }

        BigDecimal average = monthlyIncome.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        BigDecimal.valueOf(monthlyIncome.size()),
                        2,
                        RoundingMode.HALF_UP
                );

        if (average.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        BigDecimal totalDeviation = monthlyIncome.values()
                .stream()
                .map(amount -> amount.subtract(average).abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageDeviation = totalDeviation
                .divide(
                        BigDecimal.valueOf(monthlyIncome.size()),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal deviationPercentage = averageDeviation
                .multiply(BigDecimal.valueOf(100))
                .divide(average, 2, RoundingMode.HALF_UP);

        if (deviationPercentage.compareTo(BigDecimal.TEN) <= 0) {

            insights.add(new IncomeInsightResponse(
                    "STABILITY",
                    "Your income has remained stable across recent months."
            ));

        } else if (deviationPercentage.compareTo(BigDecimal.valueOf(30)) <= 0) {

            insights.add(new IncomeInsightResponse(
                    "VARIABILITY",
                    "Your income shows some month-to-month variation."
            ));

        } else {

            insights.add(new IncomeInsightResponse(
                    "INSTABILITY",
                    "Your income has significant month-to-month variation."
            ));
        }
    }

    private void addPrimarySourceInsight(
            List<Income> incomes,
            List<IncomeInsightResponse> insights) {

        BigDecimal totalIncome = incomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        Map<String, BigDecimal> sourceTotals = incomes.stream()
                .collect(Collectors.groupingBy(
                        income -> income.getSource().trim(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Income::getAmount,
                                BigDecimal::add
                        )
                ));

        Map.Entry<String, BigDecimal> primarySource = sourceTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (primarySource == null) {
            return;
        }

        BigDecimal contributionPercentage = primarySource.getValue()
                .multiply(BigDecimal.valueOf(100))
                .divide(totalIncome, 2, RoundingMode.HALF_UP);

        if (contributionPercentage.compareTo(BigDecimal.valueOf(70)) >= 0) {

            insights.add(new IncomeInsightResponse(
                    "SOURCE_DEPENDENCY",
                    "Your primary income source contributes "
                            + contributionPercentage.stripTrailingZeros().toPlainString()
                            + "% of your recorded income."
            ));
        }
    }

    private void addIrregularIncomeInsight(
            List<Income> incomes,
            List<IncomeInsightResponse> insights) {

        YearMonth currentMonth = YearMonth.now();

        BigDecimal currentIrregularIncome = incomes.stream()
                .filter(income ->
                        YearMonth.from(income.getIncomeDate()).equals(currentMonth))
                .filter(income ->
                        income.getPattern() == IncomePattern.IRREGULAR
                                || income.getPattern() == IncomePattern.ONE_TIME)
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (currentIrregularIncome.compareTo(BigDecimal.ZERO) > 0) {

            insights.add(new IncomeInsightResponse(
                    "IRREGULAR_INCOME",
                    "Your irregular or one-time income increased this month."
            ));
        }
    }

    private void addRecurringIncomeInsight(
            List<Income> incomes,
            List<IncomeInsightResponse> insights) {

        long recurringCount = incomes.stream()
                .filter(income ->
                        income.getPattern() == IncomePattern.RECURRING)
                .count();

        if (recurringCount > 0) {

            insights.add(new IncomeInsightResponse(
                    "RECURRING_INCOME",
                    "You have recorded recurring income, providing a predictable part of your cash flow."
            ));
        }
    }
}