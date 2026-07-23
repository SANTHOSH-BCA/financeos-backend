package com.financeos.financeosbackend.integration;

import com.financeos.financeosbackend.integration.helper.InvestmentTestHelper;
import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InvestmentIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create investment successfully")
    void shouldCreateInvestmentSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        AddInvestmentRequest request = InvestmentTestHelper.validInvestment();

        mockMvc.perform(post("/api/investments")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.investmentName").value("HDFC SIP"))
                .andExpect(jsonPath("$.data.investmentType").value("Mutual Fund"))
                .andExpect(jsonPath("$.data.amount").value(5000));
    }

    @Test
    @DisplayName("Should get all investments")
    void shouldGetAllInvestments() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/investments")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].investmentName").value("HDFC SIP"));
    }

    @Test
    @DisplayName("Should update investment successfully")
    void shouldUpdateInvestmentSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long investmentId = createInvestment(token);

        AddInvestmentRequest request = InvestmentTestHelper.updatedInvestment();

        mockMvc.perform(put("/api/investments/{id}", investmentId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.investmentName").value("ICICI SIP"))
                .andExpect(jsonPath("$.data.investmentType").value("Stock"))
                .andExpect(jsonPath("$.data.amount").value(8000));
    }

    @Test
    @DisplayName("Should delete investment successfully")
    void shouldDeleteInvestmentSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long investmentId = createInvestment(token);

        mockMvc.perform(delete("/api/investments/{id}", investmentId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should reject invalid investment request")
    void shouldRejectInvalidInvestmentRequest() throws Exception {

        String token = createAuthenticatedUser();

        AddInvestmentRequest request =
                InvestmentTestHelper.invalidInvestment();

        mockMvc.perform(post("/api/investments")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject future investment date")
    void shouldRejectFutureInvestmentDate() throws Exception {

        String token = createAuthenticatedUser();

        AddInvestmentRequest request =
                InvestmentTestHelper.validInvestment();

        request.setInvestmentDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/investments")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject unauthorized request")
    void shouldRejectUnauthorizedRequest() throws Exception {

        AddInvestmentRequest request =
                InvestmentTestHelper.validInvestment();

        mockMvc.perform(post("/api/investments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 404 for invalid investment update")
    void shouldReturn404ForInvalidInvestmentUpdate() throws Exception {

        String token = createAuthenticatedUser();

        AddInvestmentRequest request = InvestmentTestHelper.updatedInvestment();

        mockMvc.perform(put("/api/investments/{id}", 999999L)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 for invalid investment delete")
    void shouldReturn404ForInvalidInvestmentDelete() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(delete("/api/investments/{id}", 999999L)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paginated investments")
    void shouldReturnPaginatedInvestments() throws Exception {

        String token = createAuthenticatedUser();

        createInvestment(token);

        mockMvc.perform(get("/api/investments")
                        .header("Authorization", bearer(token))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("Should return only current user's investments")
    void shouldReturnOnlyCurrentUsersInvestments() throws Exception {

        String userOneToken = createAuthenticatedUser();
        createInvestment(userOneToken);

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/investments")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

}