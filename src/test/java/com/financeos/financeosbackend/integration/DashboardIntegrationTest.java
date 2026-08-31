package com.financeos.financeosbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DashboardIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should return empty dashboard for new user")
    void shouldReturnEmptyDashboard() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(get("/api/v1/dashboard")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalIncome").value(0))
                .andExpect(jsonPath("$.data.totalExpense").value(0))
                .andExpect(jsonPath("$.data.netSavings").value(0))
                .andExpect(jsonPath("$.data.totalTransactions").value(0))
                .andExpect(jsonPath("$.data.goalCount").value(0))
                .andExpect(jsonPath("$.data.expenseCount").value(0))
                .andExpect(jsonPath("$.data.incomeCount").value(0))
                .andExpect(jsonPath("$.data.investmentCount").value(0))
                .andExpect(jsonPath("$.data.totalInvestments").value(0));
    }

    @Test
    @DisplayName("Should return dashboard summary")
    void shouldReturnDashboardSummary() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);
        createGoal(token);

        mockMvc.perform(get("/api/v1/dashboard")
                        .header("Authorization", bearer(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.incomeCount").value(1))
                .andExpect(jsonPath("$.data.expenseCount").value(1))
                .andExpect(jsonPath("$.data.investmentCount").value(1))
                .andExpect(jsonPath("$.data.goalCount").value(1))
                .andExpect(jsonPath("$.data.totalTransactions").value(2));
    }

    @Test
    @DisplayName("Should return dashboard only for current user")
    void shouldReturnDashboardForCurrentUserOnly() throws Exception {

        String userOneToken = createAuthenticatedUser();

        createIncome(userOneToken);
        createExpense(userOneToken);
        createInvestment(userOneToken);
        createGoal(userOneToken);

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v1/dashboard")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.incomeCount").value(0))
                .andExpect(jsonPath("$.data.expenseCount").value(0))
                .andExpect(jsonPath("$.data.investmentCount").value(0))
                .andExpect(jsonPath("$.data.goalCount").value(0))
                .andExpect(jsonPath("$.data.totalTransactions").value(0));
    }

    @Test
    @DisplayName("Should reject unauthorized dashboard access")
    void shouldRejectUnauthorizedDashboardAccess() throws Exception {

        mockMvc.perform(get("/api/v1/dashboard"))
                .andExpect(status().isForbidden());
    }
}