package com.financeos.financeosbackend.analytics.controller;

import com.financeos.financeosbackend.analytics.dto.MonthlyIncomeExpenseResponse;
import com.financeos.financeosbackend.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.financeos.financeosbackend.analytics.dto.ExpenseCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlySavingsResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialInsightResponse;
import com.financeos.financeosbackend.analytics.dto.HighestSpendingCategoryResponse;
import com.financeos.financeosbackend.analytics.dto.BudgetWarningResponse;
import com.financeos.financeosbackend.analytics.dto.SavingsScoreResponse;
import com.financeos.financeosbackend.analytics.dto.FinancialHealthResponse;
import com.financeos.financeosbackend.analytics.dto.GoalProgressResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentSummaryResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentDistributionResponse;
import com.financeos.financeosbackend.analytics.dto.CashFlowResponse;
import com.financeos.financeosbackend.analytics.dto.NetWorthResponse;
import com.financeos.financeosbackend.analytics.dto.GoalInsightResponse;
import com.financeos.financeosbackend.analytics.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.analytics.dto.SmartRecommendationResponse;
import com.financeos.financeosbackend.analytics.dto.MonthlyFinancialSummaryResponse;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/monthly-income-expense")
    public ResponseEntity<ApiResponse<List<MonthlyIncomeExpenseResponse>>> getMonthlyIncomeExpense() {

        List<MonthlyIncomeExpenseResponse> response =
                analyticsService.getMonthlyIncomeExpense();

        return ResponseBuilder.success(
                "Monthly income and expense retrieved successfully",
                response
        );
    }

    @GetMapping("/expense-by-category")
    public ResponseEntity<ApiResponse<List<ExpenseCategoryResponse>>> getExpenseByCategory() {

        List<ExpenseCategoryResponse> response =
                analyticsService.getExpenseByCategory();

        return ResponseBuilder.success(
                "Expense by category retrieved successfully",
                response
        );
    }
    @GetMapping("/monthly-savings")
    public ResponseEntity<ApiResponse<List<MonthlySavingsResponse>>> getMonthlySavings() {

        List<MonthlySavingsResponse> response =
                analyticsService.getMonthlySavings();

        return ResponseBuilder.success(
                "Monthly savings retrieved successfully",
                response
        );
    }
    @GetMapping("/financial-insight")
    public ResponseEntity<ApiResponse<FinancialInsightResponse>> getFinancialInsight() {

        FinancialInsightResponse response =
                analyticsService.getFinancialInsight();

        return ResponseBuilder.success(
                "Financial insight retrieved successfully",
                response
        );
    }
    @GetMapping("/highest-spending-category")
    public ResponseEntity<ApiResponse<HighestSpendingCategoryResponse>> getHighestSpendingCategory() {

        HighestSpendingCategoryResponse response =
                analyticsService.getHighestSpendingCategory();

        return ResponseBuilder.success(
                "Highest spending category retrieved successfully",
                response
        );
    }
    @GetMapping("/budget-warning")
    public ResponseEntity<ApiResponse<BudgetWarningResponse>> getBudgetWarning() {

        BudgetWarningResponse response =
                analyticsService.getBudgetWarning();

        return ResponseBuilder.success(
                "Budget warning retrieved successfully",
                response
        );
    }
    @GetMapping("/savings-score")
    public ResponseEntity<ApiResponse<SavingsScoreResponse>> getSavingsScore() {

        SavingsScoreResponse response =
                analyticsService.getSavingsScore();

        return ResponseBuilder.success(
                "Savings score retrieved successfully",
                response
        );
    }
    @GetMapping("/financial-health")
    public ResponseEntity<ApiResponse<FinancialHealthResponse>> getFinancialHealth() {

        FinancialHealthResponse response =
                analyticsService.getFinancialHealth();

        return ResponseBuilder.success(
                "Financial health retrieved successfully",
                response
        );
    }
    @GetMapping("/goal-progress")
    public ResponseEntity<ApiResponse<List<GoalProgressResponse>>> getGoalProgress() {

        List<GoalProgressResponse> response =
                analyticsService.getGoalProgress();

        return ResponseBuilder.success(
                "Goal progress retrieved successfully",
                response
        );
    }
    @GetMapping("/investment-summary")
    public ResponseEntity<ApiResponse<InvestmentSummaryResponse>> getInvestmentSummary() {

        InvestmentSummaryResponse response =
                analyticsService.getInvestmentSummary();

        return ResponseBuilder.success(
                "Investment summary retrieved successfully",
                response
        );
    }
    @GetMapping("/investment-distribution")
    public ResponseEntity<ApiResponse<List<InvestmentDistributionResponse>>> getInvestmentDistribution() {

        List<InvestmentDistributionResponse> response =
                analyticsService.getInvestmentDistribution();

        return ResponseBuilder.success(
                "Investment distribution retrieved successfully",
                response
        );
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<ApiResponse<CashFlowResponse>> getCashFlow() {

        CashFlowResponse response =
                analyticsService.getCashFlow();

        return ResponseBuilder.success(
                "Cash flow retrieved successfully",
                response
        );
    }
    @GetMapping("/net-worth")
    public ResponseEntity<ApiResponse<NetWorthResponse>> getNetWorth() {

        NetWorthResponse response =
                analyticsService.getNetWorth();

        return ResponseBuilder.success(
                "Net worth retrieved successfully",
                response
        );
    }
    @GetMapping("/goal-insights")
    public ResponseEntity<ApiResponse<List<GoalInsightResponse>>> getGoalInsights() {

        List<GoalInsightResponse> response =
                analyticsService.getGoalInsights();

        return ResponseBuilder.success(
                "Goal insights retrieved successfully",
                response
        );
    }
    @GetMapping("/investment-insights")
    public ResponseEntity<ApiResponse<List<InvestmentInsightResponse>>> getInvestmentInsights() {

        List<InvestmentInsightResponse> response =
                analyticsService.getInvestmentInsights();

        return ResponseBuilder.success(
                "Investment insights retrieved successfully",
                response
        );
    }
    @GetMapping("/smart-recommendations")
    public ResponseEntity<ApiResponse<List<SmartRecommendationResponse>>> getSmartRecommendations() {

        List<SmartRecommendationResponse> response =
                analyticsService.getSmartRecommendations();

        return ResponseBuilder.success(
                "Smart recommendations retrieved successfully",
                response
        );
    }

    @GetMapping("/monthly-financial-summary")
    public ResponseEntity<ApiResponse<MonthlyFinancialSummaryResponse>> getMonthlyFinancialSummary() {

        MonthlyFinancialSummaryResponse response =
                analyticsService.getMonthlyFinancialSummary();

        return ResponseBuilder.success(
                "Monthly financial summary retrieved successfully",
                response
        );
    }
}