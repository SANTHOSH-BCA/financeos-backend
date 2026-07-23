package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.FinancialReportResponse;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ReportingService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReportingService.class);

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public ReportingService(
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService) {

        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public FinancialReportResponse generateFinancialReport() {

        User user = currentUserService.getCurrentUser();

        logger.info("Generating financial report for user: {}", user.getEmail());

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal totalSavings =
                totalIncome.subtract(totalExpense);

        BigDecimal netWorth =
                investmentRepository.getTotalInvestmentByUser(user);

        String financialHealth;

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {

            financialHealth = "NO DATA";

        } else {

            BigDecimal savingsRate = totalSavings
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalIncome, 2, RoundingMode.HALF_UP);

            if (savingsRate.compareTo(BigDecimal.valueOf(30)) >= 0) {
                financialHealth = "EXCELLENT";
            } else if (savingsRate.compareTo(BigDecimal.valueOf(20)) >= 0) {
                financialHealth = "GOOD";
            } else if (savingsRate.compareTo(BigDecimal.valueOf(10)) >= 0) {
                financialHealth = "AVERAGE";
            } else {
                financialHealth = "POOR";
            }
        }

        logger.info("Financial report generated successfully for user: {}", user.getEmail());

        return new FinancialReportResponse(
                totalIncome,
                totalExpense,
                totalSavings,
                netWorth,
                financialHealth
        );
    }
}