package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.income.dto.IncomeForecastResponse;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.enums.IncomePattern;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
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
public class IncomeForecastService {

    private static final int MINIMUM_HISTORY_MONTHS = 3;
    private static final int FORECAST_MONTHS = 3;

    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;

    public IncomeForecastService(
            IncomeRepository incomeRepository,
            CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
    }

    public IncomeForecastResponse getForecast() {

        User user = currentUserService.getCurrentUser();

        List<Income> recurringIncomes =
                incomeRepository.findByUser(user)
                        .stream()
                        .filter(income ->
                                income.getPattern()
                                        == IncomePattern.RECURRING)
                        .toList();

        Map<YearMonth, BigDecimal> monthlyRecurringIncome =
                recurringIncomes.stream()
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

        IncomeForecastResponse response =
                new IncomeForecastResponse();

        if (monthlyRecurringIncome.size()
                < MINIMUM_HISTORY_MONTHS) {

            response.setForecastAvailable(false);
            response.setStatus("INSUFFICIENT_DATA");
            response.setMessage(
                    "At least 3 months of recurring income history " +
                            "is required for a forecast."
            );
            response.setAverageRecurringMonthlyIncome(
                    BigDecimal.ZERO
            );
            response.setProjections(
                    new ArrayList<>()
            );

            return response;
        }

        BigDecimal averageMonthlyIncome =
                monthlyRecurringIncome.values()
                        .stream()
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
                        .divide(
                                BigDecimal.valueOf(
                                        monthlyRecurringIncome.size()
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        List<IncomeForecastResponse.ForecastMonth>
                projections = new ArrayList<>();

        YearMonth nextMonth =
                YearMonth.now().plusMonths(1);

        for (int i = 0;
             i < FORECAST_MONTHS;
             i++) {

            YearMonth forecastMonth =
                    nextMonth.plusMonths(i);

            projections.add(
                    new IncomeForecastResponse.ForecastMonth(
                            forecastMonth.toString(),
                            averageMonthlyIncome,
                            "PROJECTION"
                    )
            );
        }

        response.setForecastAvailable(true);
        response.setStatus("PROJECTED");
        response.setMessage(
                "Forecast based on historical recurring income. " +
                        "Projected amounts are not confirmed income."
        );
        response.setAverageRecurringMonthlyIncome(
                averageMonthlyIncome
        );
        response.setProjections(projections);

        return response;
    }
}