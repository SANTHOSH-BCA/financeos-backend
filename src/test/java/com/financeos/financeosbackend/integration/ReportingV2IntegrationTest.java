package com.financeos.financeosbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportingV2IntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should generate and persist V2 financial report")
    void shouldGenerateAndPersistV2FinancialReport() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);
        createGoal(token);

        String response =
                mockMvc.perform(
                        post("/api/v2/reports")
                                .header(
                                        "Authorization",
                                        bearer(token)
                                )
                                .contentType("application/json")
                                .content("""
                                        {
                                          "periodType": "MONTHLY"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reportId").isNumber())
                .andExpect(jsonPath("$.data.reportStatus")
                        .value("GENERATED"))
                .andExpect(jsonPath("$.data.reportPeriod.periodType")
                        .value("MONTHLY"))
                .andExpect(jsonPath("$.data.reportPeriod.startDate")
                        .exists())
                .andExpect(jsonPath("$.data.reportPeriod.endDate")
                        .exists())
                .andExpect(jsonPath("$.data.income").exists())
                .andExpect(jsonPath("$.data.expenses").exists())
                .andExpect(jsonPath("$.data.cashFlow").exists())
                .andExpect(jsonPath("$.data.investments").exists())
                .andExpect(jsonPath("$.data.goals").exists())
                .andExpect(jsonPath("$.data.assets").exists())
                .andExpect(jsonPath("$.data.liabilities").exists())
                .andExpect(jsonPath("$.data.netWorth").exists())
                .andExpect(jsonPath("$.data.financialHealth").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reportId =
                objectMapper.readTree(response)
                        .path("data")
                        .path("reportId")
                        .asLong();

        if (reportId <= 0) {
            throw new AssertionError(
                    "Generated report ID must be greater than zero"
            );
        }
    }

    @Test
    @DisplayName("Should retrieve generated V2 report snapshot")
    void shouldRetrieveGeneratedV2ReportSnapshot() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);

        String response =
                mockMvc.perform(
                        post("/api/v2/reports")
                                .header(
                                        "Authorization",
                                        bearer(token)
                                )
                                .contentType("application/json")
                                .content("""
                                        {
                                          "periodType": "MONTHLY"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reportId").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reportId =
                objectMapper.readTree(response)
                        .path("data")
                        .path("reportId")
                        .asLong();

        mockMvc.perform(
                get("/api/v2/reports/{reportId}", reportId)
                        .header(
                                "Authorization",
                                bearer(token)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reportId")
                        .value(reportId))
                .andExpect(jsonPath("$.data.reportStatus")
                        .value("GENERATED"))
                .andExpect(jsonPath("$.data.reportPeriod")
                        .exists())
                .andExpect(jsonPath("$.data.income")
                        .exists())
                .andExpect(jsonPath("$.data.expenses")
                        .exists())
                .andExpect(jsonPath("$.data.cashFlow")
                        .exists())
                .andExpect(jsonPath("$.data.investments")
                        .exists())
                .andExpect(jsonPath("$.data.netWorth")
                        .exists())
                .andExpect(jsonPath("$.data.financialHealth")
                        .exists());
    }

    @Test
    @DisplayName("Should prevent one user from accessing another user's report")
    void shouldPreventCrossUserReportAccess() throws Exception {

        String ownerToken = createAuthenticatedUser();

        createIncome(ownerToken);
        createExpense(ownerToken);

        String response =
                mockMvc.perform(
                        post("/api/v2/reports")
                                .header(
                                        "Authorization",
                                        bearer(ownerToken)
                                )
                                .contentType("application/json")
                                .content("""
                                        {
                                          "periodType": "MONTHLY"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reportId =
                objectMapper.readTree(response)
                        .path("data")
                        .path("reportId")
                        .asLong();

        String otherUserToken =
                createAuthenticatedUser();

        mockMvc.perform(
                get("/api/v2/reports/{reportId}", reportId)
                        .header(
                                "Authorization",
                                bearer(otherUserToken)
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
