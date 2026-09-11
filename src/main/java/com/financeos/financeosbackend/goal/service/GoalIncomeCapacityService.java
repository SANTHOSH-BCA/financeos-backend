package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class GoalIncomeCapacityService {

    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;

    public GoalIncomeCapacityService(
            IncomeRepository incomeRepository,
            CurrentUserService currentUserService
    ) {
        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
    }

    public BigDecimal getCurrentMonthIncome() {

        User user = currentUserService.getCurrentUser();

        YearMonth currentMonth =
                YearMonth.from(LocalDate.now());

        LocalDate startDate =
                currentMonth.atDay(1);

        LocalDate endDate =
                currentMonth.atEndOfMonth();

        List<Income> incomes =
                incomeRepository.findByUserAndIncomeDateBetween(
                        user,
                        startDate,
                        endDate
                );

        return incomes.stream()
                .map(Income::getAmount)
                .filter(amount -> amount != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
}