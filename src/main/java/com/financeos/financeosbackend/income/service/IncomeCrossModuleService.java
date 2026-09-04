package com.financeos.financeosbackend.income.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.dto.IncomeCrossModuleResponse;
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
public class IncomeCrossModuleService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    public IncomeCrossModuleService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.currentUserService = currentUserService;
    }

    public IncomeCrossModuleResponse getCrossModuleIntelligence() {

        User user = currentUserService.getCurrentUser();

        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);

        List<Income> incomes = incomeRepository.findByUser(user);
        List<Expense> expenses = expenseRepository.findByUser(user);

        BigDecimal currentIncome = sumIncomeForMonth(incomes, currentMonth);
        BigDecimal previousIncome = sumIncomeForMonth(incomes, previousMonth);

        BigDecimal currentExpense = sumExpenseForMonth(expenses, currentMonth);
        BigDecimal previousExpense = sumExpenseForMonth(expenses, previousMonth);

        BigDecimal incomeChange =
                calculatePercentageChange(previousIncome, currentIncome);

        BigDecimal expenseChange =
                calculatePercentageChange(previousExpense, currentExpense);

        BigDecimal surplus = currentIncome.subtract(currentExpense);

        IncomeCrossModuleResponse response =
                new IncomeCrossModuleResponse();

        response.setCurrentMonthIncome(currentIncome);
        response.setCurrentMonthExpense(currentExpense);
        response.setIncomeChangePercentage(incomeChange);
        response.setExpenseChangePercentage(expenseChange);
        response.setCurrentMonthSurplus(surplus);

        if (expenseChange.compareTo(incomeChange) > 0) {

            response.setRelationship("EXPENSES_GROWING_FASTER");

            response.setInsight(
                    "Your expenses are growing faster than your income."
            );

        } else if (incomeChange.compareTo(expenseChange) > 0) {

            response.setRelationship("INCOME_GROWING_FASTER");

            response.setInsight(
                    "Your income is growing faster than your expenses."
            );

        } else {

            response.setRelationship("GROWING_AT_SIMILAR_RATE");

            response.setInsight(
                    "Your income and expenses are changing at a similar rate."
            );
        }

        return response;
    }

    private BigDecimal sumIncomeForMonth(
            List<Income> incomes,
            YearMonth month) {

        return incomes.stream()
                .filter(income ->
                        YearMonth.from(income.getIncomeDate()).equals(month))
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumExpenseForMonth(
            List<Expense> expenses,
            YearMonth month) {

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(expense.getExpenseDate()).equals(month))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePercentageChange(
            BigDecimal previous,
            BigDecimal current) {

        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 2, RoundingMode.HALF_UP);
    }
}