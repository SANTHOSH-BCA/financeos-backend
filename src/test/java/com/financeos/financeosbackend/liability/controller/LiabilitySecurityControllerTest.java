package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.service.DebtBurdenService;
import com.financeos.financeosbackend.liability.service.DebtFinancialFutureService;
import com.financeos.financeosbackend.liability.service.DebtFinancialHealthService;
import com.financeos.financeosbackend.liability.service.DebtInvestmentTradeoffService;
import com.financeos.financeosbackend.liability.service.DebtNetWorthImpactService;
import com.financeos.financeosbackend.liability.service.LiabilityV2ApiService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LiabilityV2ApiController.class)
@Import(LiabilityV2ApiController.class)
class LiabilitySecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LiabilityV2ApiService liabilityV2ApiService;

    @MockitoBean
    private DebtBurdenService debtBurdenService;

    @MockitoBean
    private DebtInvestmentTradeoffService debtInvestmentTradeoffService;

    @MockitoBean
    private DebtNetWorthImpactService debtNetWorthImpactService;

    @MockitoBean
    private DebtFinancialHealthService debtFinancialHealthService;

    @MockitoBean
    private DebtFinancialFutureService debtFinancialFutureService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void overview_ShouldRequireAuthentication() throws Exception {

        mockMvc.perform(
                get("/api/v2/liabilities/overview")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void overview_ShouldAllowAuthenticatedUser() throws Exception {

        mockMvc.perform(
                get("/api/v2/liabilities/overview")
                        .with(user("test-user"))
        ).andExpect(status().isOk());
    }
}