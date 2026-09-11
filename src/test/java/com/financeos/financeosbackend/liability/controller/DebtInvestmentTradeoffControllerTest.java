package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.liability.service.DebtInvestmentTradeoffService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DebtInvestmentTradeoffController.class)
class DebtInvestmentTradeoffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DebtInvestmentTradeoffService debtInvestmentTradeoffService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getDebtInvestmentTradeoff_ShouldReturn200()
            throws Exception {

        DebtInvestmentTradeoffResponse response =
                new DebtInvestmentTradeoffResponse(
                        new BigDecimal("500000.00"),
                        new BigDecimal("1000000.00"),
                        new BigDecimal("50.00"),
                        "LOW",
                        "Recognized outstanding debt of 500000.00 is 50.00% of the current investment value of 1000000.00."
                );

        when(debtInvestmentTradeoffService.getTradeoff())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/debt-investment-tradeoff")
                                .with(user("test-user"))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.totalOutstandingDebt")
                                .value(500000.00)
                )
                .andExpect(
                        jsonPath("$.totalInvestmentValue")
                                .value(1000000.00)
                )
                .andExpect(
                        jsonPath("$.debtToInvestmentRatio")
                                .value(50.00)
                )
                .andExpect(
                        jsonPath("$.tradeoffLevel")
                                .value("LOW")
                )
                .andExpect(
                        jsonPath("$.explanation")
                                .exists()
                );
    }

    @Test
    void getDebtInvestmentTradeoff_ShouldReturnUnknown_WhenInvestmentValueUnavailable()
            throws Exception {

        DebtInvestmentTradeoffResponse response =
                new DebtInvestmentTradeoffResponse(
                        new BigDecimal("500000.00"),
                        BigDecimal.ZERO.setScale(2),
                        null,
                        "UNKNOWN",
                        "Investment value is unavailable or zero, so the debt-investment trade-off cannot be assessed."
                );

        when(debtInvestmentTradeoffService.getTradeoff())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/debt-investment-tradeoff")
                                .with(user("test-user"))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.totalOutstandingDebt")
                                .value(500000.00)
                )
                .andExpect(
                        jsonPath("$.totalInvestmentValue")
                                .value(0.00)
                )
                .andExpect(
                        jsonPath("$.debtToInvestmentRatio")
                                .doesNotExist()
                )
                .andExpect(
                        jsonPath("$.tradeoffLevel")
                                .value("UNKNOWN")
                );
    }
}