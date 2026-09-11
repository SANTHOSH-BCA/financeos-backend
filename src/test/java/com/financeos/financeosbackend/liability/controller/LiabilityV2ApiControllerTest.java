package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.LiabilityV2ApiResponse;
import com.financeos.financeosbackend.liability.service.LiabilityV2ApiService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(LiabilityV2ApiController.class)
class LiabilityV2ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LiabilityV2ApiService liabilityV2ApiService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getOverview_ShouldRequireAuthentication() throws Exception {

        mockMvc.perform(
                get("/api/v2/liabilities/overview")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void getOverview_ShouldReturnOverviewForAuthenticatedUser()
            throws Exception {

        LiabilityV2ApiResponse response =
                new LiabilityV2ApiResponse(
                        new BigDecimal("500000.00"),
                        new BigDecimal("15000.00"),
                        new BigDecimal("800000.00"),
                        new BigDecimal("700000.00"),
                        new BigDecimal("25.00"),
                        new BigDecimal("62.50"),
                        2,
                        "MODERATE",
                        "MODERATE_BURDEN",
                        "PROJECTABLE"
                );

        when(liabilityV2ApiService.getOverview())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/overview")
                                .with(user("test-user"))
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.totalOutstandingDebt")
                                .value(500000.00)
                )
                .andExpect(
                        jsonPath("$.totalMonthlyDebtPayment")
                                .value(15000.00)
                )
                .andExpect(
                        jsonPath("$.totalInvestmentValue")
                                .value(800000.00)
                )
                .andExpect(
                        jsonPath("$.netWorth")
                                .value(700000.00)
                )
                .andExpect(
                        jsonPath("$.activeLiabilityCount")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$.debtBurdenLevel")
                                .value("MODERATE")
                )
                .andExpect(
                        jsonPath("$.debtHealth")
                                .value("MODERATE_BURDEN")
                )
                .andExpect(
                        jsonPath("$.projectionStatus")
                                .value("PROJECTABLE")
                );
    }
}