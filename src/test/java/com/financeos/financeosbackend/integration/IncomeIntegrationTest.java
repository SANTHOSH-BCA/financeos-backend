package com.financeos.financeosbackend.integration;

import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.integration.helper.IncomeTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IncomeIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create income successfully")
    void shouldCreateIncomeSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        AddIncomeRequest request = IncomeTestHelper.validIncome();

        mockMvc.perform(post("/api/incomes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.source").value("Salary"))
                .andExpect(jsonPath("$.data.amount").value(25000));
    }

    @Test
    @DisplayName("Should get all incomes")
    void shouldGetAllIncomes() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);

        mockMvc.perform(get("/api/incomes")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].source").value("Salary"));
    }

    @Test
    @DisplayName("Should update income successfully")
    void shouldUpdateIncomeSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long incomeId = createIncome(token);

        AddIncomeRequest request = IncomeTestHelper.updatedIncome();

        mockMvc.perform(put("/api/incomes/{id}", incomeId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.source").value("Freelancing"))
                .andExpect(jsonPath("$.data.amount").value(30000));
    }

    @Test
    @DisplayName("Should delete income successfully")
    void shouldDeleteIncomeSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long incomeId = createIncome(token);

        mockMvc.perform(delete("/api/incomes/{id}", incomeId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should reject invalid income request")
    void shouldRejectInvalidIncomeRequest() throws Exception {

        String token = createAuthenticatedUser();

        AddIncomeRequest request = IncomeTestHelper.invalidIncome();

        mockMvc.perform(post("/api/incomes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject future income date")
    void shouldRejectFutureIncomeDate() throws Exception {

        String token = createAuthenticatedUser();

        AddIncomeRequest request = IncomeTestHelper.validIncome();
        request.setIncomeDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/incomes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject unauthorized request")
    void shouldRejectUnauthorizedRequest() throws Exception {

        AddIncomeRequest request = IncomeTestHelper.validIncome();

        mockMvc.perform(post("/api/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 404 for invalid income update")
    void shouldReturn404ForInvalidIncomeUpdate() throws Exception {

        String token = createAuthenticatedUser();

        AddIncomeRequest request = IncomeTestHelper.updatedIncome();

        mockMvc.perform(put("/api/incomes/{id}", 999999L)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 for invalid income delete")
    void shouldReturn404ForInvalidIncomeDelete() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(delete("/api/incomes/{id}", 999999L)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paginated incomes")
    void shouldReturnPaginatedIncomes() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);

        mockMvc.perform(get("/api/incomes")
                        .header("Authorization", bearer(token))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    @Test
    @DisplayName("Should return only current user's incomes")
    void shouldReturnOnlyCurrentUsersIncomes() throws Exception {

        String userOneToken = createAuthenticatedUser();
        createIncome(userOneToken);

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/incomes")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(0));
    }

}