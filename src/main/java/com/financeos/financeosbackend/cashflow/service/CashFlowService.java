package com.financeos.financeosbackend.cashflow.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.math.BigDecimal;

@Service
public class CashFlowService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    public CashFlowService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            CurrentUserService currentUserService
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.currentUserService = currentUserService;
    }

    public BigDecimal calculateInflows() {

        User user = currentUserService.getCurrentUser();

        return incomeRepository.getTotalIncomeByUser(user);
    }

    public BigDecimal calculateOutflows() {

        User user = currentUserService.getCurrentUser();

        return expenseRepository.getTotalExpenseByUser(user);
    }

    public BigDecimal calculateNetCashFlow() {

        BigDecimal inflows = calculateInflows();
        BigDecimal outflows = calculateOutflows();

        return inflows.subtract(outflows);
    }

    public BigDecimal calculateSavings() {

        return calculateNetCashFlow();
    }

    public BigDecimal calculateSavingsRate() {

        BigDecimal inflows = calculateInflows();
        BigDecimal savings = calculateSavings();

        if (inflows.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return savings
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        inflows,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public BigDecimal calculateIncludedInflows() {

        return calculateInflows();
    }

    public BigDecimal calculateIncludedOutflows() {

        return calculateOutflows();
    }
}