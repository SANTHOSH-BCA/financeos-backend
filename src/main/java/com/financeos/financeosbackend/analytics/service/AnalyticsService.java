package com.financeos.financeosbackend.analytics.service;

import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.analytics.dto.MonthlyIncomeExpenseResponse;
import java.util.List;
import java.util.ArrayList;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.expense.entity.Expense;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;
import com.financeos.financeosbackend.analytics.dto.ExpenseCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlySavingsResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialInsightResponse;
import com.financeos.financeosbackend.analytics.dto.HighestSpendingCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.BudgetWarningResponse;
import com.financeos.financeosbackend.analytics.dto.SavingsScoreResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialHealthResponse;
import com.financeos.financeosbackend.analytics.dto.GoalProgressResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import java.math.RoundingMode;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.analytics.dto.InvestmentSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyCashFlowResponse;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.analytics.dto.InvestmentSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentDistributionResponse;
import java.math.RoundingMode;
import com.financeos.financeosbackend.analytics.dto.CashFlowResponse;
import com.financeos.financeosbackend.analytics.dto.NetWorthResponse;
import com.financeos.financeosbackend.analytics.dto.GoalInsightResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.analytics.dto.SmartRecommendationResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyFinancialSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyCashFlowResponse;import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsCashFlowV2Response;import com.financeos.financeosbackend.networth.service.NetWorthService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsNetWorthV2Response;import com.financeos.financeosbackend.investment.service.InvestmentService;
import com.financeos.financeosbackend.investment.service.InvestmentInsightService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsInvestmentV2Response;import com.financeos.financeosbackend.goalintelligence.service.GoalIntelligenceService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsGoalV2Response;
import com.financeos.financeosbackend.goal.enums.GoalStatus;import com.financeos.financeosbackend.financialhealth.service.FinancialHealthService;
import com.financeos.financeosbackend.analytics.dto.AnalyticsFinancialHealthV2Response;import com.financeos.financeosbackend.financialprofile.service.FinancialProfileService;import com.financeos.financeosbackend.analytics.dto.AnalyticsFinancialProfileV2Response;import com.financeos.financeosbackend.financialprofile.service.EmergencyFundContextService;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfileIncomeNatureService;
import com.financeos.financeosbackend.financialprofile.service.FinancialProfilePriorityService;
import com.financeos.financeosbackend.financialprofile.service.FinancialResponsibilityContextService;
import com.financeos.financeosbackend.financialprofile.service.InvestmentExperienceAssessmentService;
import com.financeos.financeosbackend.financialprofile.service.ProtectionContextService;import com.financeos.financeosbackend.financialprofile.dto.EmergencyFundContextResponse;
import com.financeos.financeosbackend.financialprofile.dto.FinancialResponsibilityContextResponse;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;import com.financeos.financeosbackend.analytics.dto.AnalyticsUnifiedV2Response;import com.financeos.financeosbackend.financialposition.service.FinancialPositionService;import com.financeos.financeosbackend.analytics.dto.AnalyticsFinancialPositionV2Response;import com.financeos.financeosbackend.financialposition.dto.FinancialPositionResponse;import com.financeos.financeosbackend.analytics.dto.AnalyticsUnifiedV2Response;


@Service
public class AnalyticsService {
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;
    private final InvestmentRepository investmentRepository;
    private final CashFlowService cashFlowService;
    private final FinancialPositionService financialPositionService;
    private final NetWorthService netWorthService;
    private final InvestmentService investmentService;
    private final InvestmentInsightService investmentInsightService;
    private final GoalIntelligenceService goalIntelligenceService;
    private final FinancialHealthService financialHealthService;
    private final FinancialProfileService financialProfileService;
    private final FinancialProfileIncomeNatureService financialProfileIncomeNatureService;
    private final FinancialProfilePriorityService financialProfilePriorityService;
    private final InvestmentExperienceAssessmentService investmentExperienceAssessmentService;
    private final FinancialResponsibilityContextService financialResponsibilityContextService;
    private final EmergencyFundContextService emergencyFundContextService;
    private final ProtectionContextService protectionContextService;

    public AnalyticsService(
            ExpenseRepository expenseRepository,
            IncomeRepository incomeRepository,
            GoalRepository goalRepository,
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService,
            CashFlowService cashFlowService,
            FinancialPositionService financialPositionService,
            NetWorthService netWorthService,
            InvestmentService investmentService,
            InvestmentInsightService investmentInsightService,
            GoalIntelligenceService goalIntelligenceService,
            FinancialHealthService financialHealthService,
            FinancialProfileService financialProfileService,
            FinancialProfileIncomeNatureService financialProfileIncomeNatureService,
            FinancialProfilePriorityService financialProfilePriorityService,
            InvestmentExperienceAssessmentService investmentExperienceAssessmentService,
            FinancialResponsibilityContextService financialResponsibilityContextService,
            EmergencyFundContextService emergencyFundContextService,
            ProtectionContextService protectionContextService) {

        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.currentUserService = currentUserService;
        this.goalRepository = goalRepository;
        this.investmentRepository = investmentRepository;
        this.cashFlowService = cashFlowService;
        this.financialPositionService = financialPositionService;
        this.netWorthService = netWorthService;
        this.investmentService = investmentService;
        this.investmentInsightService = investmentInsightService;
        this.goalIntelligenceService = goalIntelligenceService;
        this.financialHealthService = financialHealthService;
        this.financialProfileService = financialProfileService;
        this.financialProfileIncomeNatureService = financialProfileIncomeNatureService;
        this.financialProfilePriorityService = financialProfilePriorityService;
        this.investmentExperienceAssessmentService = investmentExperienceAssessmentService;
        this.financialResponsibilityContextService = financialResponsibilityContextService;
        this.emergencyFundContextService = emergencyFundContextService;
        this.protectionContextService = protectionContextService;
    }

    public List<MonthlyIncomeExpenseResponse> getMonthlyIncomeExpense() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);
        Map<YearMonth, BigDecimal> monthlyExpense = getMonthlyExpense(user);

        Set<YearMonth> allMonths = new TreeSet<>();

        allMonths.addAll(monthlyIncome.keySet());
        allMonths.addAll(monthlyExpense.keySet());

        List<MonthlyIncomeExpenseResponse> response = new ArrayList<>();

        for (YearMonth month : allMonths) {

            MonthlyIncomeExpenseResponse item = new MonthlyIncomeExpenseResponse();

            item.setMonth(month.toString());

            item.setTotalIncome(
                    monthlyIncome.getOrDefault(month, BigDecimal.ZERO)
            );

            item.setTotalExpense(
                    monthlyExpense.getOrDefault(month, BigDecimal.ZERO)
            );

            response.add(item);
        }

        return response;
    }
    public List<ExpenseCategoryResponse> getExpenseByCategory() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses = expenseRepository.findByUser(user);

        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        List<ExpenseCategoryResponse> response = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {

            response.add(new ExpenseCategoryResponse(
                    entry.getKey(),
                    entry.getValue()
            ));
        }

        return response;
    }

    public List<MonthlySavingsResponse> getMonthlySavings() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);
        Map<YearMonth, BigDecimal> monthlyExpense = getMonthlyExpense(user);

        Set<YearMonth> allMonths = new TreeSet<>();
        allMonths.addAll(monthlyIncome.keySet());
        allMonths.addAll(monthlyExpense.keySet());

        List<MonthlySavingsResponse> response = new ArrayList<>();

        for (YearMonth month : allMonths) {

            BigDecimal income = monthlyIncome.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal expense = monthlyExpense.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal savings = income.subtract(expense);

            response.add(new MonthlySavingsResponse(
                    month.toString(),
                    income,
                    expense,
                    savings
            ));
        }

        return response;
    }

    public FinancialInsightResponse getFinancialInsight() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);
        Map<YearMonth, BigDecimal> monthlyExpense = getMonthlyExpense(user);

        if (monthlyIncome.isEmpty()) {
            return new FinancialInsightResponse(
                    "NO_DATA",
                    "No income records available."
            );
        }

        YearMonth latestMonth = monthlyIncome.keySet()
                .stream()
                .max(YearMonth::compareTo)
                .orElseThrow();

        BigDecimal income = monthlyIncome.getOrDefault(latestMonth, BigDecimal.ZERO);
        BigDecimal expense = monthlyExpense.getOrDefault(latestMonth, BigDecimal.ZERO);

        if (income.compareTo(BigDecimal.ZERO) == 0) {
            return new FinancialInsightResponse(
                    "NO_DATA",
                    "Income is zero for the latest month."
            );
        }

        BigDecimal savings = income.subtract(expense);

        BigDecimal savingsPercentage = savings
                .multiply(BigDecimal.valueOf(100))
                .divide(income, 2, java.math.RoundingMode.HALF_UP);

        String status;
        String message;

        if (savingsPercentage.compareTo(BigDecimal.valueOf(40)) >= 0) {

            status = "EXCELLENT";
            message = "Excellent! You saved " + savingsPercentage + "% of your income this month.";

        } else if (savingsPercentage.compareTo(BigDecimal.valueOf(20)) >= 0) {

            status = "GOOD";
            message = "Good! You saved " + savingsPercentage + "% of your income this month.";

        } else if (savingsPercentage.compareTo(BigDecimal.ZERO) >= 0) {

            status = "AVERAGE";
            message = "You saved only " + savingsPercentage + "% of your income. Consider reducing expenses.";

        } else {

            status = "WARNING";
            message = "Your expenses exceeded your income this month.";
        }

        return new FinancialInsightResponse(status, message);
    }

    public HighestSpendingCategoryResponse getHighestSpendingCategory() {

        User user = currentUserService.getCurrentUser();

        List<Expense> expenses = expenseRepository.findByUser(user);

        if (expenses.isEmpty()) {
            return new HighestSpendingCategoryResponse(
                    "NO_DATA",
                    BigDecimal.ZERO
            );
        }

        YearMonth latestMonth = expenses.stream()
                .map(expense -> YearMonth.from(expense.getExpenseDate()))
                .max(YearMonth::compareTo)
                .orElseThrow();

        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .filter(expense -> YearMonth.from(expense.getExpenseDate()).equals(latestMonth))
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        Map.Entry<String, BigDecimal> highest = categoryTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (highest == null) {
            return new HighestSpendingCategoryResponse(
                    "NO_DATA",
                    BigDecimal.ZERO
            );
        }

        return new HighestSpendingCategoryResponse(
                highest.getKey(),
                highest.getValue()
        );
    }

    public BudgetWarningResponse getBudgetWarning() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);

        if (monthlyIncome.isEmpty()) {
            return new BudgetWarningResponse(
                    "NO_DATA",
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    "No income data available."
            );
        }

        YearMonth latestMonth = monthlyIncome.keySet()
                .stream()
                .max(YearMonth::compareTo)
                .orElseThrow();

        BigDecimal income = monthlyIncome.get(latestMonth);

        List<Expense> latestMonthExpenses = expenseRepository.findByUser(user)
                .stream()
                .filter(expense ->
                        YearMonth.from(expense.getExpenseDate()).equals(latestMonth))
                .toList();

        if (latestMonthExpenses.isEmpty()) {
            return new BudgetWarningResponse(
                    "NO_DATA",
                    BigDecimal.ZERO,
                    income,
                    BigDecimal.ZERO,
                    "No expenses found for the latest month."
            );
        }

        Map<String, BigDecimal> categoryTotals = latestMonthExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        Map.Entry<String, BigDecimal> highestCategory = categoryTotals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        BigDecimal percentage = highestCategory.getValue()
                .multiply(BigDecimal.valueOf(100))
                .divide(income, 2, java.math.RoundingMode.HALF_UP);

        String warning;

        if (percentage.compareTo(BigDecimal.valueOf(35)) > 0) {
            warning = "Warning! " + highestCategory.getKey()
                    + " spending is " + percentage
                    + "% of your monthly income.";
        } else {
            warning = "Your spending is within the recommended budget.";
        }

        return new BudgetWarningResponse(
                highestCategory.getKey(),
                highestCategory.getValue(),
                income,
                percentage,
                warning
        );
    }

    public SavingsScoreResponse getSavingsScore() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);
        Map<YearMonth, BigDecimal> monthlyExpense = getMonthlyExpense(user);

        if (monthlyIncome.isEmpty()) {
            return new SavingsScoreResponse(
                    0,
                    BigDecimal.ZERO,
                    "No income data available."
            );
        }

        YearMonth latestMonth = monthlyIncome.keySet()
                .stream()
                .max(YearMonth::compareTo)
                .orElseThrow();

        BigDecimal income = monthlyIncome.getOrDefault(latestMonth, BigDecimal.ZERO);
        BigDecimal expense = monthlyExpense.getOrDefault(latestMonth, BigDecimal.ZERO);

        if (income.compareTo(BigDecimal.ZERO) == 0) {
            return new SavingsScoreResponse(
                    0,
                    BigDecimal.ZERO,
                    "Income is zero."
            );
        }

        BigDecimal savings = income.subtract(expense);

        BigDecimal savingsPercentage = savings
                .multiply(BigDecimal.valueOf(100))
                .divide(income, 2, java.math.RoundingMode.HALF_UP);

        int score;
        String message;

        if (savingsPercentage.compareTo(BigDecimal.valueOf(40)) >= 0) {
            score = 100;
            message = "Excellent savings habit.";
        } else if (savingsPercentage.compareTo(BigDecimal.valueOf(30)) >= 0) {
            score = 90;
            message = "Very good savings habit.";
        } else if (savingsPercentage.compareTo(BigDecimal.valueOf(20)) >= 0) {
            score = 75;
            message = "Good savings habit.";
        } else if (savingsPercentage.compareTo(BigDecimal.valueOf(10)) >= 0) {
            score = 60;
            message = "Average savings habit.";
        } else if (savingsPercentage.compareTo(BigDecimal.ZERO) >= 0) {
            score = 40;
            message = "Poor savings habit. Try to save more.";
        } else {
            score = 0;
            message = "Critical. Your expenses exceed your income.";
        }

        return new SavingsScoreResponse(
                score,
                savingsPercentage,
                message
        );
    }

    public FinancialHealthResponse getFinancialHealth() {

        SavingsScoreResponse savingsScore = getSavingsScore();
        BudgetWarningResponse budgetWarning = getBudgetWarning();
        FinancialInsightResponse insight = getFinancialInsight();

        int score = 0;

        // Savings Score (40%)
        score += (int) (savingsScore.getScore() * 0.4);

        // Budget Score (30%)
        if (budgetWarning.getPercentage().compareTo(BigDecimal.valueOf(35)) <= 0) {
            score += 30;
        }

        // Financial Insight Score (30%)
        switch (insight.getStatus()) {

            case "EXCELLENT":
                score += 30;
                break;

            case "GOOD":
                score += 25;
                break;

            case "AVERAGE":
                score += 15;
                break;

            case "WARNING":
                score += 0;
                break;

            default:
                score += 0;
        }

        String status;
        String message;

        if (score >= 90) {

            status = "EXCELLENT";
            message = "Your financial health is excellent. Keep up the great work.";

        } else if (score >= 75) {

            status = "GOOD";
            message = "Your finances are in good condition.";

        } else if (score >= 60) {

            status = "AVERAGE";
            message = "Your finances are stable but have room for improvement.";

        } else if (score >= 40) {

            status = "POOR";
            message = "Your financial health needs attention.";

        } else {

            status = "CRITICAL";
            message = "Immediate financial improvement is recommended.";
        }

        return new FinancialHealthResponse(
                score,
                status,
                message
        );
    }

    public List<GoalProgressResponse> getGoalProgress() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        List<GoalProgressResponse> response = new ArrayList<>();

        for (Goal goal : goals) {

            BigDecimal remainingAmount =
                    goal.getTargetAmount().subtract(goal.getCurrentAmount());

            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                remainingAmount = BigDecimal.ZERO;
            }

            BigDecimal completionPercentage;

            if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
                completionPercentage = BigDecimal.ZERO;
            } else {
                completionPercentage =
                        goal.getCurrentAmount()
                                .multiply(BigDecimal.valueOf(100))
                                .divide(goal.getTargetAmount(), 2, RoundingMode.HALF_UP);
            }

            response.add(
                    new GoalProgressResponse(
                            goal.getGoalName(),
                            goal.getTargetAmount(),
                            goal.getCurrentAmount(),
                            remainingAmount,
                            completionPercentage
                    )
            );
        }

        return response;
    }

    public InvestmentSummaryResponse getInvestmentSummary() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalInvestment =
                investmentRepository.getTotalInvestmentByUser(user);

        return new InvestmentSummaryResponse(totalInvestment);
    }

    public List<InvestmentDistributionResponse> getInvestmentDistribution() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalInvestment =
                investmentRepository.getTotalInvestmentByUser(user);

        List<Object[]> results =
                investmentRepository.getInvestmentDistributionByUser(user);

        List<InvestmentDistributionResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            String investmentType = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];

            BigDecimal percentage;

            if (totalInvestment.compareTo(BigDecimal.ZERO) == 0) {
                percentage = BigDecimal.ZERO;
            } else {
                percentage = amount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(totalInvestment, 2, RoundingMode.HALF_UP);
            }

            response.add(
                    new InvestmentDistributionResponse(
                            investmentType,
                            amount,
                            percentage
                    )
            );
        }

        return response;
    }

    public CashFlowResponse getCashFlow() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal netCashFlow = totalIncome.subtract(totalExpense);

        return new CashFlowResponse(
                totalIncome,
                totalExpense,
                netCashFlow
        );
    }

    public AnalyticsCashFlowV2Response getCashFlowV2() {

        BigDecimal inflows = cashFlowService.calculateIncludedInflows();
        BigDecimal outflows = cashFlowService.calculateIncludedOutflows();
        BigDecimal netCashFlow = cashFlowService.calculateNetCashFlow();
        BigDecimal savings = cashFlowService.calculateSavings();
        BigDecimal savingsRate = cashFlowService.calculateSavingsRate();

        return new AnalyticsCashFlowV2Response(
                inflows,
                outflows,
                netCashFlow,
                savings,
                savingsRate
        );
    }

    public NetWorthResponse getNetWorth() {

        User user = currentUserService.getCurrentUser();

        BigDecimal totalAssets =
                investmentRepository.getTotalInvestmentByUser(user);

        BigDecimal totalLiabilities = BigDecimal.ZERO;

        BigDecimal netWorth =
                totalAssets.subtract(totalLiabilities);

        return new NetWorthResponse(
                totalAssets,
                totalLiabilities,
                netWorth
        );
    }

    public AnalyticsNetWorthV2Response getNetWorthV2() {

        BigDecimal recognizedAssets =
                netWorthService.calculateIncludedAssets();

        BigDecimal recognizedLiabilities =
                netWorthService.calculateIncludedLiabilities();

        BigDecimal netWorth =
                netWorthService.calculateNetWorth();

        return new AnalyticsNetWorthV2Response(
                recognizedAssets,
                recognizedLiabilities,
                netWorth
        );
    }

    public List<GoalInsightResponse> getGoalInsights() {

        User user = currentUserService.getCurrentUser();

        List<Goal> goals = goalRepository.findByUser(user);

        List<GoalInsightResponse> response = new ArrayList<>();

        for (Goal goal : goals) {

            BigDecimal percentage;

            if (goal.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
                percentage = BigDecimal.ZERO;
            } else {
                percentage = goal.getCurrentAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(goal.getTargetAmount(), 2, RoundingMode.HALF_UP);
            }

            String status;
            String recommendation;

            if (percentage.compareTo(BigDecimal.valueOf(90)) >= 0) {
                status = "EXCELLENT";
                recommendation = "Goal is almost completed.";
            } else if (percentage.compareTo(BigDecimal.valueOf(70)) >= 0) {
                status = "GOOD";
                recommendation = "Keep contributing consistently.";
            } else if (percentage.compareTo(BigDecimal.valueOf(40)) >= 0) {
                status = "AVERAGE";
                recommendation = "Increase monthly contributions.";
            } else {
                status = "LOW";
                recommendation = "Prioritize this goal to improve progress.";
            }

            response.add(
                    new GoalInsightResponse(
                            goal.getGoalName(),
                            percentage,
                            status,
                            recommendation
                    )
            );
        }

        return response;
    }

    public List<InvestmentInsightResponse> getInvestmentInsights() {

        List<InvestmentDistributionResponse> distribution =
                getInvestmentDistribution();

        List<InvestmentInsightResponse> response = new ArrayList<>();

        for (InvestmentDistributionResponse investment : distribution) {

            String status;
            String recommendation;

            if (investment.getPercentage().compareTo(BigDecimal.valueOf(70)) >= 0) {
                status = "HIGH";
                recommendation = "Portfolio is highly concentrated. Consider diversifying into other investment types.";
            } else if (investment.getPercentage().compareTo(BigDecimal.valueOf(40)) >= 0) {
                status = "GOOD";
                recommendation = "Portfolio allocation is balanced. Continue monitoring diversification.";
            } else {
                status = "LOW";
                recommendation = "Allocation is within a healthy diversification range.";
            }

            response.add(
                    new InvestmentInsightResponse(
                            investment.getInvestmentType(),
                            investment.getAmount(),
                            investment.getPercentage(),
                            status,
                            recommendation
                    )
            );
        }

        return response;
    }

    public AnalyticsInvestmentV2Response getInvestmentV2() {

        return new AnalyticsInvestmentV2Response(
                investmentService.getPortfolioPerformance(),
                investmentService.getAssetAllocation(),
                investmentService.getInvestmentExposure(),
                investmentInsightService.getInvestmentInsights(),
                investmentService.calculateInvestmentToNetWorthPercentage()
        );
    }

    public List<SmartRecommendationResponse> getSmartRecommendations() {

        List<SmartRecommendationResponse> recommendations = new ArrayList<>();

        // Goal Recommendations
        List<GoalInsightResponse> goalInsights = getGoalInsights();

        for (GoalInsightResponse goal : goalInsights) {

            if ("LOW".equals(goal.getStatus())) {

                recommendations.add(
                        new SmartRecommendationResponse(
                                "Goal Progress",
                                goal.getRecommendation(),
                                "HIGH"
                        )
                );
            }
        }

        // Investment Recommendations
        List<InvestmentInsightResponse> investmentInsights =
                getInvestmentInsights();

        for (InvestmentInsightResponse investment : investmentInsights) {

            if ("HIGH".equals(investment.getStatus())) {

                recommendations.add(
                        new SmartRecommendationResponse(
                                "Investment Portfolio",
                                investment.getRecommendation(),
                                "MEDIUM"
                        )
                );
            }
        }

        // Default Recommendation
        if (recommendations.isEmpty()) {

            recommendations.add(
                    new SmartRecommendationResponse(
                            "Financial Health",
                            "Your financial profile looks healthy. Continue your current financial habits.",
                            "LOW"
                    )
            );
        }

        return recommendations;
    }

    public MonthlyFinancialSummaryResponse getMonthlyFinancialSummary() {

        User user = currentUserService.getCurrentUser();

        BigDecimal income = incomeRepository.getTotalIncomeByUser(user);
        BigDecimal expense = expenseRepository.getTotalExpenseByUser(user);

        BigDecimal savings = income.subtract(expense);

        BigDecimal savingsRate;

        if (income.compareTo(BigDecimal.ZERO) == 0) {
            savingsRate = BigDecimal.ZERO;
        } else {
            savingsRate = savings
                    .multiply(BigDecimal.valueOf(100))
                    .divide(income, 2, RoundingMode.HALF_UP);
        }

        String financialHealth;
        String summary;

        if (savingsRate.compareTo(BigDecimal.valueOf(30)) >= 0) {

            financialHealth = "EXCELLENT";
            summary = "Excellent month. You saved a significant portion of your income and maintained healthy financial habits.";

        } else if (savingsRate.compareTo(BigDecimal.valueOf(20)) >= 0) {

            financialHealth = "GOOD";
            summary = "Good month. Your savings are on track, but there is room for improvement.";

        } else if (savingsRate.compareTo(BigDecimal.valueOf(10)) >= 0) {

            financialHealth = "AVERAGE";
            summary = "Average month. Consider reducing discretionary spending to improve your savings.";

        } else {

            financialHealth = "POOR";
            summary = "Your savings rate is low. Review your expenses and focus on increasing monthly savings.";
        }

        return new MonthlyFinancialSummaryResponse(
                income,
                expense,
                savings,
                savingsRate,
                financialHealth,
                summary
        );
    }

    public AnalyticsGoalV2Response getGoalV2() {

        List<Goal> goals = goalIntelligenceService.getMyGoals();

        int totalGoals = goals.size();
        int completedGoals = 0;
        int atRiskGoals = 0;
        int onTrackGoals = 0;

        BigDecimal totalTargetAmount = BigDecimal.ZERO;
        BigDecimal totalCurrentAmount = BigDecimal.ZERO;
        BigDecimal totalRemainingAmount = BigDecimal.ZERO;

        for (Goal goal : goals) {

            GoalStatus status =
                    goalIntelligenceService.calculateGoalStatus(goal);

            if (status == GoalStatus.COMPLETED) {
                completedGoals++;
            } else if (status == GoalStatus.AT_RISK) {
                atRiskGoals++;
            } else if (status == GoalStatus.ON_TRACK) {
                onTrackGoals++;
            }

            totalTargetAmount =
                    totalTargetAmount.add(goal.getTargetAmount());

            totalCurrentAmount =
                    totalCurrentAmount.add(goal.getCurrentAmount());

            totalRemainingAmount =
                    totalRemainingAmount.add(
                            goalIntelligenceService.calculateRemainingAmount(goal)
                    );
        }

        return new AnalyticsGoalV2Response(
                totalGoals,
                completedGoals,
                atRiskGoals,
                onTrackGoals,
                totalTargetAmount,
                totalCurrentAmount,
                totalRemainingAmount
        );
    }

    public AnalyticsFinancialHealthV2Response getFinancialHealthV2() {

        return new AnalyticsFinancialHealthV2Response(
                financialHealthService.calculateCashFlowHealth(),
                financialHealthService.calculateDebtHealth(),
                financialHealthService.calculateSavingsHealth(),
                financialHealthService.calculateInvestmentHealth(),
                financialHealthService.calculateGoalHealth(),
                financialHealthService.calculateWealthHealth(),
                financialHealthService.calculateOverallFinancialHealth()
        );
    }

    public List<MonthlyCashFlowResponse> getMonthlyCashFlow() {

        User user = currentUserService.getCurrentUser();

        Map<YearMonth, BigDecimal> monthlyIncome = getMonthlyIncome(user);
        Map<YearMonth, BigDecimal> monthlyExpense = getMonthlyExpense(user);

        Set<YearMonth> allMonths = new TreeSet<>();
        allMonths.addAll(monthlyIncome.keySet());
        allMonths.addAll(monthlyExpense.keySet());

        List<MonthlyCashFlowResponse> response = new ArrayList<>();

        for (YearMonth month : allMonths) {

            response.add(
                    new MonthlyCashFlowResponse(
                            month.toString(),
                            monthlyIncome.getOrDefault(month, BigDecimal.ZERO),
                            monthlyExpense.getOrDefault(month, BigDecimal.ZERO)
                    )
            );
        }

        return response;
    }

    public AnalyticsFinancialProfileV2Response getFinancialProfileV2() {

        InvestmentExperienceAssessmentResponse investmentExperience = null;
        FinancialResponsibilityContextResponse responsibility = null;
        EmergencyFundContextResponse emergencyFund = null;
        ProtectionContextResponse protection = null;

        try {
            investmentExperience =
                    investmentExperienceAssessmentService.getAssessment();
        } catch (RuntimeException ignored) {
        }

        try {
            responsibility =
                    financialResponsibilityContextService.get();
        } catch (RuntimeException ignored) {
        }

        try {
            emergencyFund =
                    emergencyFundContextService.get();
        } catch (RuntimeException ignored) {
        }

        try {
            protection =
                    protectionContextService.get();
        } catch (RuntimeException ignored) {
        }

        return new AnalyticsFinancialProfileV2Response(
                financialProfileService.getMyProfile(),
                financialProfileIncomeNatureService.getIncomeNatures(),
                financialProfilePriorityService.getPriorities(),
                investmentExperience,
                responsibility,
                emergencyFund,
                protection
        );
    }

    public AnalyticsUnifiedV2Response getUnifiedV2() {

        return new AnalyticsUnifiedV2Response(
                getFinancialPositionV2(),
                getCashFlowV2(),
                getNetWorthV2(),
                getInvestmentV2(),
                getGoalV2(),
                getFinancialHealthV2(),
                getFinancialProfileV2()
        );
    }

    public AnalyticsFinancialPositionV2Response getFinancialPositionV2() {

        FinancialPositionResponse financialPosition =
                new FinancialPositionResponse(
                        financialPositionService.calculateNetWorth(),
                        financialPositionService.calculateNetCashFlow(),
                        financialPositionService.calculateInvestmentValue(),
                        financialPositionService.calculateDebtValue(),
                        financialPositionService.calculateLiquidAssets(),
                        financialPositionService.calculateAssetAllocation(),
                        financialPositionService.calculateSavings(),
                        financialPositionService.calculateSavingsRate()
                );

        return new AnalyticsFinancialPositionV2Response(
                financialPosition
        );
    }

    private Map<YearMonth, BigDecimal> getMonthlyIncome(User user) {

        List<Income> incomes = incomeRepository.findByUser(user);

        return incomes.stream()
                .collect(Collectors.groupingBy(
                        income -> YearMonth.from(income.getIncomeDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Income::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    private Map<YearMonth, BigDecimal> getMonthlyExpense(User user) {

        List<Expense> expenses = expenseRepository.findByUser(user);

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> YearMonth.from(expense.getExpenseDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

}