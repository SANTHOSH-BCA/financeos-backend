package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.income.dto.IncomeIntelligenceResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.income.enums.IncomeStability;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomeIntelligenceService {

    private static final BigDecimal MEANINGFUL_CHANGE_THRESHOLD =
            new BigDecimal("10");

    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;

    public IncomeIntelligenceService(
            IncomeRepository incomeRepository,
            CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
    }

    public IncomeIntelligenceResponse getIncomeIntelligence() {

        User user = currentUserService.getCurrentUser();

        List<Income> incomes =
                incomeRepository.findByUser(user);

        YearMonth currentMonth =
                YearMonth.now();

        YearMonth previousMonth =
                currentMonth.minusMonths(1);

        BigDecimal currentMonthIncome =
                getMonthlyTotal(incomes, currentMonth);

        BigDecimal previousMonthIncome =
                getMonthlyTotal(incomes, previousMonth);

        BigDecimal changeAmount =
                currentMonthIncome.subtract(previousMonthIncome);

        BigDecimal changePercentage =
                calculatePercentageChange(
                        previousMonthIncome,
                        currentMonthIncome
                );

        IncomeIntelligenceResponse response =
                new IncomeIntelligenceResponse();

        response.setCurrentMonthIncome(
                currentMonthIncome
        );

        response.setPreviousMonthIncome(
                previousMonthIncome
        );

        response.setChangeAmount(
                changeAmount
        );

        response.setChangePercentage(
                changePercentage
        );

        response.setTrend(
                detectTrend(changePercentage)
        );

        response.setStability(
                detectStability(incomes)
        );
        Map<String, BigDecimal> sourceContributions =
                getCurrentMonthSourceContributions(
                        incomes,
                        currentMonth
                );

        response.setSourceContributionPercentages(
                calculateSourceContributionPercentages(
                        sourceContributions
                )
        );

        response.setMajorIncomeSource(
                findMajorIncomeSource(sourceContributions)
        );

        response.setMeaningfulChange(
                detectMeaningfulChange(
                        changePercentage
                )
        );

        return response;
    }

    private BigDecimal getMonthlyTotal(
            List<Income> incomes,
            YearMonth month) {

        return incomes.stream()
                .filter(income ->
                        YearMonth.from(
                                income.getIncomeDate()
                        ).equals(month)
                )
                .map(Income::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private BigDecimal calculatePercentageChange(
            BigDecimal previous,
            BigDecimal current) {

        if (previous.compareTo(BigDecimal.ZERO) == 0) {

            if (current.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            return BigDecimal.valueOf(100);
        }

        return current
                .subtract(previous)
                .divide(
                        previous,
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String detectTrend(
            BigDecimal changePercentage) {

        if (changePercentage.compareTo(BigDecimal.ZERO) > 0) {
            return "INCREASING";
        }

        if (changePercentage.compareTo(BigDecimal.ZERO) < 0) {
            return "DECREASING";
        }

        return "STABLE";
    }

    private IncomeStability detectStability(
            List<Income> incomes) {

        if (incomes.isEmpty()) {
            return IncomeStability.INSUFFICIENT_DATA;
        }

        Map<YearMonth, BigDecimal> monthlyIncome =
                incomes.stream()
                        .collect(Collectors.groupingBy(
                                income ->
                                        YearMonth.from(
                                                income.getIncomeDate()
                                        ),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Income::getAmount,
                                        BigDecimal::add
                                )
                        ));

        if (monthlyIncome.size() < 3) {
            return IncomeStability.INSUFFICIENT_DATA;
        }

        List<BigDecimal> values =
                monthlyIncome.values().stream().toList();

        BigDecimal average =
                values.stream()
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
                        .divide(
                                BigDecimal.valueOf(values.size()),
                                2,
                                RoundingMode.HALF_UP
                        );

        if (average.compareTo(BigDecimal.ZERO) == 0) {
            return IncomeStability.UNSTABLE;
        }

        BigDecimal totalDeviation =
                values.stream()
                        .map(value ->
                                value.subtract(average).abs()
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal averageDeviation =
                totalDeviation.divide(
                        BigDecimal.valueOf(values.size()),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal deviationPercentage =
                averageDeviation
                        .divide(
                                average,
                                4,
                                RoundingMode.HALF_UP
                        )
                        .multiply(BigDecimal.valueOf(100));

        if (deviationPercentage.compareTo(
                BigDecimal.valueOf(10)) <= 0) {

            return IncomeStability.STABLE;
        }

        if (deviationPercentage.compareTo(
                BigDecimal.valueOf(30)) <= 0) {

            return IncomeStability.VARIABLE;
        }

        return IncomeStability.UNSTABLE;
    }

    private Map<String, BigDecimal>
    getCurrentMonthSourceContributions(
            List<Income> incomes,
            YearMonth month) {

        return incomes.stream()
                .filter(income ->
                        YearMonth.from(
                                income.getIncomeDate()
                        ).equals(month)
                )
                .collect(Collectors.groupingBy(
                        Income::getSource,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Income::getAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, BigDecimal>
                                        comparingByValue()
                                .reversed()
                )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                BigDecimal::add,
                                LinkedHashMap::new
                        )
                );
    }

    private String findMajorIncomeSource(
            Map<String, BigDecimal> sourceContributions) {

        return sourceContributions.entrySet()
                .stream()
                .max(
                        Comparator.comparing(
                                Map.Entry::getValue
                        )
                )
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String detectMeaningfulChange(
            BigDecimal changePercentage) {

        if (changePercentage.abs().compareTo(
                MEANINGFUL_CHANGE_THRESHOLD
        ) < 0) {

            return "No meaningful change in income";
        }

        if (changePercentage.compareTo(
                BigDecimal.ZERO
        ) > 0) {

            return "Income increased meaningfully";
        }

        return "Income decreased meaningfully";
    }

    private Map<String, BigDecimal>
    calculateSourceContributionPercentages(
            Map<String, BigDecimal> sourceContributions) {

        BigDecimal total =
                sourceContributions.values()
                        .stream()
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return Map.of();
        }

        return sourceContributions.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .divide(
                                        total,
                                        4,
                                        RoundingMode.HALF_UP
                                )
                                .multiply(
                                        BigDecimal.valueOf(100)
                                )
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                )
                ));
    }
}