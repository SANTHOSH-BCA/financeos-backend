package com.financeos.financeosbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnalyticsIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should return monthly income and expense")
    void shouldReturnMonthlyIncomeAndExpense() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/monthly-income-expense")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return expense by category")
    void shouldReturnExpenseByCategory() throws Exception {

        String token = createAuthenticatedUser();

        createExpense(token);

        mockMvc.perform(get("/api/analytics/expense-by-category")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return monthly savings")
    void shouldReturnMonthlySavings() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/monthly-savings")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return financial insight")
    void shouldReturnFinancialInsight() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/financial-insight")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").exists())
                .andExpect(jsonPath("$.data.message").exists());
    }

    @Test
    @DisplayName("Should return budget warning")
    void shouldReturnBudgetWarning() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/budget-warning")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.warning").exists());
    }

    @Test
    @DisplayName("Should return savings score")
    void shouldReturnSavingsScore() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/savings-score")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.score").exists())
                .andExpect(jsonPath("$.data.message").exists());
    }

    @Test
    @DisplayName("Should return cash flow")
    void shouldReturnCashFlow() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/cash-flow")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalIncome").exists())
                .andExpect(jsonPath("$.data.totalExpense").exists())
                .andExpect(jsonPath("$.data.netCashFlow").exists());
    }

    @Test
    @DisplayName("Should return net worth")
    void shouldReturnNetWorth() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/analytics/net-worth")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalAssets").exists())
                .andExpect(jsonPath("$.data.totalLiabilities").exists())
                .andExpect(jsonPath("$.data.netWorth").exists());
    }

    @Test
    @DisplayName("Should return financial health")
    void shouldReturnFinancialHealth() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/financial-health")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.score").exists())
                .andExpect(jsonPath("$.data.status").exists())
                .andExpect(jsonPath("$.data.message").exists());
    }

    @Test
    @DisplayName("Should return goal progress")
    void shouldReturnGoalProgress() throws Exception {

        String token = createAuthenticatedUser();

        createGoal(token);

        mockMvc.perform(get("/api/analytics/goal-progress")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return investment summary")
    void shouldReturnInvestmentSummary() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/analytics/investment-summary")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalInvestment").exists());
    }

    @Test
    @DisplayName("Should return investment distribution")
    void shouldReturnInvestmentDistribution() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/analytics/investment-distribution")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return goal insights")
    void shouldReturnGoalInsights() throws Exception {

        String token = createAuthenticatedUser();

        createGoal(token);

        mockMvc.perform(get("/api/analytics/goal-insights")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return investment insights")
    void shouldReturnInvestmentInsights() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/analytics/investment-insights")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return smart recommendations")
    void shouldReturnSmartRecommendations() throws Exception {

        String token = createAuthenticatedUser();

        createGoal(token);
        createInvestment(token);

        mockMvc.perform(get("/api/analytics/smart-recommendations")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should return monthly financial summary")
    void shouldReturnMonthlyFinancialSummary() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);

        mockMvc.perform(get("/api/analytics/monthly-financial-summary")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.income").exists())
                .andExpect(jsonPath("$.data.expense").exists())
                .andExpect(jsonPath("$.data.savings").exists())
                .andExpect(jsonPath("$.data.savingsRate").exists())
                .andExpect(jsonPath("$.data.financialHealth").exists());
    }

}