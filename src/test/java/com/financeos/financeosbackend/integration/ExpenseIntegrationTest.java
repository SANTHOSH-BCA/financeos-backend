package com.financeos.financeosbackend.integration;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.financeos.financeosbackend.integration.helper.ExpenseTestHelper;


class ExpenseIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create expense successfully")
    void shouldCreateExpenseSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        AddExpenseRequest request = createExpenseRequest();

        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Expense created successfully"))
                .andExpect(jsonPath("$.data.title").value("Petrol"))
                .andExpect(jsonPath("$.data.category").value("Transport"))
                .andExpect(jsonPath("$.data.amount").value(500));
    }

    @Test
    @DisplayName("Should fetch all expenses")
    void shouldGetMyExpenses() throws Exception {

        String token = createAuthenticatedUser();

        createExpense(token);

        mockMvc.perform(get("/api/v1/expenses")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Expenses fetched successfully"))
                .andExpect(jsonPath("$.data[0].title").value("Petrol"))
                .andExpect(jsonPath("$.data[0].category").value("Transport"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should update expense successfully")
    void shouldUpdateExpenseSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long expenseId = createExpense(token);

        AddExpenseRequest request = ExpenseTestHelper.updatedExpense();

        mockMvc.perform(put("/api/v1/expenses/" + expenseId)
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Expense updated successfully"))
                .andExpect(jsonPath("$.data.title").value("Lunch"))
                .andExpect(jsonPath("$.data.category").value("Food"))
                .andExpect(jsonPath("$.data.amount").value(250));
    }

    @Test
    @DisplayName("Should delete expense successfully")
    void shouldDeleteExpenseSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long expenseId = createExpense(token);

        mockMvc.perform(delete("/api/v1/expenses/" + expenseId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Expense deleted successfully"));

        assertFalse(expenseRepository.findById(expenseId).isPresent());
    }


    @Test
    @DisplayName("Should reject unauthorized request")
    void shouldRejectUnauthorizedAccess() throws Exception {

        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should validate required fields")
    void shouldValidateExpenseRequest() throws Exception {

        AddExpenseRequest request = new AddExpenseRequest();

        String token = createAuthenticatedUser();

        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject future expense date")
    void shouldRejectFutureExpenseDate() throws Exception {

        String token = createAuthenticatedUser();

        AddExpenseRequest request = ExpenseTestHelper.futureExpense();

        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing expense")
    void shouldReturnNotFoundWhenUpdatingInvalidExpense() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(put("/api/v1/expenses/999999")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ExpenseTestHelper.updatedExpense())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Expense not found"));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing expense")
    void shouldReturnNotFoundWhenDeletingInvalidExpense() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(delete("/api/v1/expenses/999999")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Expense not found"));
    }
    @Test
    @DisplayName("Should filter expenses by category")
    void shouldFilterExpensesByCategory() throws Exception {

        String token = createAuthenticatedUser();

        createExpense(token);

        mockMvc.perform(get("/api/v1/expenses/filter")
                        .header("Authorization", bearer(token))
                        .param("category", "Transport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].category").value("Transport"));
    }

    @Test
    @DisplayName("Should filter expenses by minimum amount")
    void shouldFilterExpensesByMinimumAmount() throws Exception {

        String token = createAuthenticatedUser();

        createExpense(token);

        mockMvc.perform(get("/api/v1/expenses/filter")
                        .header("Authorization", bearer(token))
                        .param("minAmount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return paginated expenses")
    void shouldReturnPaginatedExpenses() throws Exception {

        String token = createAuthenticatedUser();

        createExpense(token);
        createExpense(token);

        mockMvc.perform(get("/api/v1/expenses")
                        .header("Authorization", bearer(token))
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return only authenticated user's expenses")
    void shouldReturnOnlyCurrentUsersExpenses() throws Exception {

        String token1 = createAuthenticatedUser();
        createExpense(token1);

        String token2 = createAuthenticatedUser();
        createExpense(token2);

        mockMvc.perform(get("/api/v1/expenses")
                        .header("Authorization", bearer(token2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }
}