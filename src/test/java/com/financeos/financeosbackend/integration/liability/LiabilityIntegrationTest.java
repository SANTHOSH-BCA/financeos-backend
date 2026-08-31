package com.financeos.financeosbackend.integration.liability;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.integration.BaseIntegrationTest;
import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LiabilityIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create individual liability successfully")
    void createLiability_ShouldCreateIndividualLiability() throws Exception {

        String token = createAuthenticatedUser();

        CreateLiabilityRequest request = createLiabilityRequest(
                "Personal Loan",
                LiabilityType.PERSONAL_LOAN,
                new BigDecimal("500000"),
                ResponsibilityType.INDIVIDUAL,
                null
        );

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.liabilityName").value("Personal Loan"))
                .andExpect(jsonPath("$.liabilityType")
                        .value("PERSONAL_LOAN"))
                .andExpect(jsonPath("$.outstandingAmount")
                        .value(500000))
                .andExpect(jsonPath("$.responsibilityType")
                        .value("INDIVIDUAL"))
                .andExpect(jsonPath("$.responsibilityPercentage")
                        .value(100))
                .andExpect(jsonPath("$.recognizedLiability")
                        .value(500000))
                .andExpect(jsonPath("$.includedInNetWorth")
                        .value(true));
    }

    @Test
    @DisplayName("Should create shared liability using responsibility percentage")
    void createLiability_ShouldCalculateSharedResponsibility() throws Exception {

        String token = createAuthenticatedUser();

        CreateLiabilityRequest request = createLiabilityRequest(
                "Home Loan",
                LiabilityType.HOME_LOAN,
                new BigDecimal("2000000"),
                ResponsibilityType.SHARED,
                new BigDecimal("70")
        );

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.liabilityName")
                        .value("Home Loan"))
                .andExpect(jsonPath("$.responsibilityType")
                        .value("SHARED"))
                .andExpect(jsonPath("$.responsibilityPercentage")
                        .value(70))
                .andExpect(jsonPath("$.recognizedLiability")
                        .value(1400000))
                .andExpect(jsonPath("$.includedInNetWorth")
                        .value(true));
    }

    @Test
    @DisplayName("Should exclude family unclear liability from personal net worth")
    void createLiability_ShouldExcludeFamilyUnclearLiability() throws Exception {

        String token = createAuthenticatedUser();

        CreateLiabilityRequest request = createLiabilityRequest(
                "Family Home Loan",
                LiabilityType.HOME_LOAN,
                new BigDecimal("1500000"),
                ResponsibilityType.FAMILY_UNCLEAR,
                new BigDecimal("50")
        );

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responsibilityType")
                        .value("FAMILY_UNCLEAR"))
                .andExpect(jsonPath("$.responsibilityPercentage")
                        .doesNotExist())
                .andExpect(jsonPath("$.recognizedLiability")
                        .value(0))
                .andExpect(jsonPath("$.includedInNetWorth")
                        .value(false));
    }

    @Test
    @DisplayName("Should return current user's liabilities only")
    void getLiabilities_ShouldReturnCurrentUsersLiabilities() throws Exception {

        String userOneToken = createAuthenticatedUser();

        CreateLiabilityRequest request = createLiabilityRequest(
                "User One Loan",
                LiabilityType.PERSONAL_LOAN,
                new BigDecimal("300000"),
                ResponsibilityType.INDIVIDUAL,
                null
        );

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(userOneToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/v2/liabilities")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should reject another user's liability")
    void getLiability_ShouldRejectAnotherUsersLiability() throws Exception {

        String userOneToken = createAuthenticatedUser();

        CreateLiabilityRequest request = createLiabilityRequest(
                "User One Loan",
                LiabilityType.PERSONAL_LOAN,
                new BigDecimal("300000"),
                ResponsibilityType.INDIVIDUAL,
                null
        );

        String response = mockMvc.perform(
                        post("/api/v2/liabilities")
                                .header(
                                        "Authorization",
                                        bearer(userOneToken)
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        Long liabilityId = json.get("id").asLong();

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(
                        get("/api/v2/liabilities/" + liabilityId)
                                .header(
                                        "Authorization",
                                        bearer(userTwoToken)
                                )
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should reject invalid liability request")
    void createLiability_ShouldRejectInvalidRequest() throws Exception {

        String token = createAuthenticatedUser();

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setLiabilityName("");
        request.setLiabilityType(null);
        request.setOutstandingAmount(null);
        request.setResponsibilityType(null);
        request.setResponsibilityPercentage(null);
        request.setValuationDate(null);

        mockMvc.perform(post("/api/v2/liabilities")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject unauthorized liability access")
    void liability_ShouldRejectUnauthorizedAccess() throws Exception {

        mockMvc.perform(get("/api/v2/liabilities"))
                .andExpect(status().isForbidden());
    }

    private CreateLiabilityRequest createLiabilityRequest(
            String name,
            LiabilityType type,
            BigDecimal amount,
            ResponsibilityType responsibilityType,
            BigDecimal responsibilityPercentage
    ) {

        CreateLiabilityRequest request =
                new CreateLiabilityRequest();

        request.setLiabilityName(name);
        request.setLiabilityType(type);
        request.setOutstandingAmount(amount);
        request.setResponsibilityType(responsibilityType);
        request.setResponsibilityPercentage(
                responsibilityPercentage
        );
        request.setValuationDate(LocalDate.of(2026, 8, 31));

        return request;
    }
}