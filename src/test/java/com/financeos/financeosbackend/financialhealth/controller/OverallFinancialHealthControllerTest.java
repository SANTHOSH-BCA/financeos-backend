package com.financeos.financeosbackend.financialhealth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.filter.JwtAuthenticationFilter;
import com.financeos.financeosbackend.financialhealth.dto.response.FinancialHealthProtectionResponse;
import com.financeos.financeosbackend.financialhealth.service.OverallFinancialHealthService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OverallFinancialHealthController.class)
@AutoConfigureMockMvc(addFilters = false)
class OverallFinancialHealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OverallFinancialHealthService overallFinancialHealthService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getProtectionHealth_ShouldReturnProtectionIndicators() throws Exception {
        FinancialHealthProtectionResponse response =
                new FinancialHealthProtectionResponse(
                        "RECORDED",
                        "GAP_IDENTIFIED",
                        "RECORDED",
                        "REVIEW_RECOMMENDED",
                        "HEALTH: Health policy recorded. LIFE: No life policy recorded.",
                        "YES",
                        "LIFE_INSURANCE",
                        "TERM_INSURANCE"
                );

        when(overallFinancialHealthService.calculateProtectionHealthIndicator())
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/financial-health/protection")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.healthProtectionStatus").value("RECORDED"))
                .andExpect(jsonPath("$.data.lifeProtectionStatus").value("GAP_IDENTIFIED"))
                .andExpect(jsonPath("$.data.vehicleProtectionStatus").value("RECORDED"))
                .andExpect(jsonPath("$.data.overallProtectionStatus").value("REVIEW_RECOMMENDED"))
                .andExpect(jsonPath("$.data.explanation").isNotEmpty());
    }
}