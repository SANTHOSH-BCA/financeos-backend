package com.financeos.financeosbackend.integration.financialposition;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.integration.BaseIntegrationTest;
import com.financeos.financeosbackend.integration.helper.ExpenseTestHelper;
import com.financeos.financeosbackend.integration.helper.IncomeTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FinancialPositionIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should return consolidated financial position")
    void getFinancialPosition_ShouldReturnConsolidatedPosition()
            throws Exception {

        String token = createAuthenticatedUser();

        AddIncomeRequest incomeRequest =
                IncomeTestHelper.validIncome();

        incomeRequest.setAmount(
                new java.math.BigDecimal("50000")
        );

        mockMvc.perform(post("/api/incomes")
                        .header(
                                "Authorization",
                                bearer(token)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        incomeRequest
                                )
                        ))
                .andExpect(status().isCreated());

        AddExpenseRequest expenseRequest =
                ExpenseTestHelper.validExpense();

        expenseRequest.setAmount(
                new java.math.BigDecimal("8000")
        );

        mockMvc.perform(post("/api/v1/expenses")
                        .header(
                                "Authorization",
                                bearer(token)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        expenseRequest
                                )
                        ))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v2/financial-position")
                        .header(
                                "Authorization",
                                bearer(token)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.netWorth").value(0))
                .andExpect(jsonPath("$.data.netCashFlow").value(42000))
                .andExpect(jsonPath("$.data.investmentValue").value(0))
                .andExpect(jsonPath("$.data.debtValue").value(0))
                .andExpect(jsonPath("$.data.liquidAssets").value(0))
                .andExpect(jsonPath("$.data.savings").value(42000))
                .andExpect(jsonPath("$.data.savingsRate").value(84));
    }

    @Test
    @DisplayName("Should return zero financial position when user has no records")
    void getFinancialPosition_ShouldReturnZeroWhenNoRecords()
            throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/financial-position")
                        .header(
                                "Authorization",
                                bearer(token)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.netWorth").value(0))
                .andExpect(jsonPath("$.data.netCashFlow").value(0))
                .andExpect(jsonPath("$.data.investmentValue").value(0))
                .andExpect(jsonPath("$.data.debtValue").value(0))
                .andExpect(jsonPath("$.data.liquidAssets").value(0))
                .andExpect(jsonPath("$.data.savings").value(0))
                .andExpect(jsonPath("$.data.savingsRate").value(0));
    }

    @Test
    @DisplayName("Should return only current user's financial position")
    void getFinancialPosition_ShouldReturnCurrentUsersValuesOnly()
            throws Exception {

        String userOneToken = createAuthenticatedUser();

        AddIncomeRequest incomeRequest =
                IncomeTestHelper.validIncome();

        incomeRequest.setAmount(
                new java.math.BigDecimal("100000")
        );

        mockMvc.perform(post("/api/incomes")
                        .header(
                                "Authorization",
                                bearer(userOneToken)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        incomeRequest
                                )
                        ))
                .andExpect(status().isCreated());

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/financial-position")
                        .header(
                                "Authorization",
                                bearer(userTwoToken)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.netWorth").value(0))
                .andExpect(jsonPath("$.data.netCashFlow").value(0))
                .andExpect(jsonPath("$.data.investmentValue").value(0))
                .andExpect(jsonPath("$.data.debtValue").value(0))
                .andExpect(jsonPath("$.data.liquidAssets").value(0))
                .andExpect(jsonPath("$.data.savings").value(0))
                .andExpect(jsonPath("$.data.savingsRate").value(0));
    }

    @Test
    @DisplayName("Should reject unauthorized financial position request")
    void getFinancialPosition_ShouldRejectUnauthorizedRequest()
            throws Exception {

        mockMvc.perform(get("/api/v2/financial-position"))
                .andExpect(status().isForbidden());
    }
}