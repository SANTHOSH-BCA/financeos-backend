package com.financeos.financeosbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportingIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should return financial report")
    void shouldReturnFinancialReport() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);

        mockMvc.perform(get("/api/reports/financial-summary")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalIncome").exists())
                .andExpect(jsonPath("$.data.totalExpense").exists())
                .andExpect(jsonPath("$.data.totalSavings").exists())
                .andExpect(jsonPath("$.data.netWorth").exists())
                .andExpect(jsonPath("$.data.financialHealth").exists());
    }

    @Test
    @DisplayName("Should download PDF report")
    void shouldDownloadPdfReport() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);

        MvcResult result = mockMvc.perform(get("/api/reports/download/pdf")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=FinanceOS-Report.pdf"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsByteArray().length > 0);
    }

    @Test
    @DisplayName("Should download Excel report")
    void shouldDownloadExcelReport() throws Exception {

        String token = createAuthenticatedUser();

        createIncome(token);
        createExpense(token);
        createInvestment(token);

        MvcResult result = mockMvc.perform(get("/api/reports/download/excel")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=FinanceOS-Report.xlsx"))
                .andExpect(header().string("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andReturn();

        assertTrue(result.getResponse().getContentAsByteArray().length > 0);
    }
}