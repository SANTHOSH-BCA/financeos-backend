package com.financeos.financeosbackend.dashboard.service;

import com.financeos.financeosbackend.dashboard.dto.DashboardResponse;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import java.math.BigDecimal;

@Service
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public DashboardService(ExpenseRepository expenseRepository,
                            IncomeRepository incomeRepository,
                            UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public DashboardResponse getDashboard() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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

        return response;
    }
}