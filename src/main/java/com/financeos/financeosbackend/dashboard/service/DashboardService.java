package com.financeos.financeosbackend.dashboard.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.dashboard.dto.DashboardResponse;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final CurrentUserService currentUserService;
    private final InvestmentRepository investmentRepository;
    private final GoalRepository goalRepository;

    public DashboardService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository,
            CurrentUserService currentUserService,
            InvestmentRepository investmentRepository,
            GoalRepository goalRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
        this.investmentRepository = investmentRepository;
        this.goalRepository = goalRepository;
    }

    public DashboardResponse getDashboard() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal totalInvestments =
                investmentRepository.getTotalInvestmentByUser(user);

        Long incomeCount =
                incomeRepository.countIncomeByUser(user);

        Long expenseCount =
                expenseRepository.countExpensesByUser(user);

        Long investmentCount =
                investmentRepository.countInvestmentsByUser(user);

        Long goalCount =
                goalRepository.countGoalsByUser(user);

        // Prevent null values from SUM queries
        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }

        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        if (totalInvestments == null) {
            totalInvestments = BigDecimal.ZERO;
        }

        BigDecimal netSavings =
                totalIncome.subtract(totalExpense);

        BigDecimal totalNetWorth =
                netSavings.add(totalInvestments);

        Long totalTransactions =
                incomeCount + expenseCount;

        DashboardResponse response = new DashboardResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetSavings(netSavings);
        response.setTotalNetWorth(totalNetWorth);
        response.setTotalInvestments(totalInvestments);
        response.setTotalTransactions(totalTransactions);
        response.setGoalCount(goalCount);
        response.setIncomeCount(incomeCount);
        response.setExpenseCount(expenseCount);
        response.setInvestmentCount(investmentCount);

        return response;
    }
}