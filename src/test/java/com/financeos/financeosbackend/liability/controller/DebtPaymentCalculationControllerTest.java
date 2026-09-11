package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtPaymentCalculationResponse;
import com.financeos.financeosbackend.liability.service.DebtPaymentCalculationService;
import com.financeos.financeosbackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DebtPaymentCalculationController.class)
class DebtPaymentCalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DebtPaymentCalculationService calculationService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void shouldReturnPaymentCalculation() throws Exception {

        DebtPaymentCalculationResponse response =
                new DebtPaymentCalculationResponse();

        response.setLiabilityId(10L);
        response.setOutstandingAmount(
                new BigDecimal("500000.00")
        );
        response.setPaymentAmount(
                new BigDecimal("25000.00")
        );
        response.setMonthlyPayment(
                new BigDecimal("25000.00")
        );
        response.setTotalPrincipalPaid(
                new BigDecimal("100000.00")
        );
        response.setTotalInterestPaid(
                new BigDecimal("20000.00")
        );
        response.setTotalPaid(
                new BigDecimal("120000.00")
        );
        response.setRemainingTenureMonths(24);

        when(calculationService.calculate(10L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v2/liabilities/10/payment-calculation")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.liabilityId").value(10))
                .andExpect(jsonPath("$.outstandingAmount")
                        .value(500000))
                .andExpect(jsonPath("$.monthlyPayment")
                        .value(25000))
                .andExpect(jsonPath("$.totalPrincipalPaid")
                        .value(100000))
                .andExpect(jsonPath("$.totalInterestPaid")
                        .value(20000))
                .andExpect(jsonPath("$.remainingTenureMonths")
                        .value(24));
    }
}