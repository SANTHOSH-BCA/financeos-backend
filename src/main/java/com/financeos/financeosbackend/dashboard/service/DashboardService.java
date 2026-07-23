package com.financeos.financeosbackend.dashboard.service;

import com.financeos.financeosbackend.dashboard.dto.DashboardResponse;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import com.financeos.financeosbackend.common.service.CurrentUserService;
@Service
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final InvestmentRepository investmentRepository;
    private final GoalRepository goalRepository;

    public DashboardService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService,
            InvestmentRepository investmentRepository,
            GoalRepository goalRepository) {

        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.investmentRepository = investmentRepository;
        this.goalRepository = goalRepository;
    }

    public DashboardResponse getDashboard() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalInvestments =
                investmentRepository.getTotalInvestmentByUser(user);

        Long goalCount =
                goalRepository.countGoalsByUser(user);

        Long expenseCount =
                expenseRepository.countExpensesByUser(user);

        Long incomeCount =
                incomeRepository.countIncomeByUser(user);

        Long investmentCount =
                investmentRepository.countInvestmentsByUser(user);

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal netSavings =
                totalIncome.subtract(totalExpense);

        Long totalTransactions =
                incomeRepository.countIncomeByUser(user)
                        + expenseRepository.countExpensesByUser(user);

        DashboardResponse response = new DashboardResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetSavings(netSavings);
        response.setTotalTransactions(totalTransactions);
        response.setTotalInvestments(totalInvestments);
        response.setGoalCount(goalCount);
        response.setExpenseCount(expenseCount);
        response.setIncomeCount(incomeCount);
        response.setInvestmentCount(investmentCount);

        return response;
    }
}