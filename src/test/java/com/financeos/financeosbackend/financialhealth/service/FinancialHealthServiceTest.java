package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthServiceTest {

    @Mock
    private CashFlowService cashFlowService;

    @InjectMocks
    private FinancialHealthService financialHealthService;

    @Test
    void calculateCashFlowHealth_ShouldReturnPositive_WhenNetCashFlowIsPositive() {

        when(cashFlowService.calculateInflows())
                .thenReturn(new BigDecimal("100000"));

        when(cashFlowService.calculateOutflows())
                .thenReturn(new BigDecimal("70000"));

        when(cashFlowService.calculateNetCashFlow())
                .thenReturn(new BigDecimal("30000"));

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("30.00"));

        CashFlowHealthResponse response =
                financialHealthService.calculateCashFlowHealth();

        assertNotNull(response);
        assertEquals(new BigDecimal("100000"), response.getInflows());
        assertEquals(new BigDecimal("70000"), response.getOutflows());
        assertEquals(new BigDecimal("30000"), response.getNetCashFlow());
        assertEquals(new BigDecimal("30.00"), response.getSavingsRate());
        assertEquals("POSITIVE", response.getStatus());
    }

    @Test
    void calculateCashFlowHealth_ShouldReturnNeutral_WhenNetCashFlowIsZero() {

        when(cashFlowService.calculateInflows())
                .thenReturn(new BigDecimal("100000"));

        when(cashFlowService.calculateOutflows())
                .thenReturn(new BigDecimal("100000"));

        when(cashFlowService.calculateNetCashFlow())
                .thenReturn(BigDecimal.ZERO);

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(BigDecimal.ZERO);

        CashFlowHealthResponse response =
                financialHealthService.calculateCashFlowHealth();

        assertEquals(BigDecimal.ZERO, response.getNetCashFlow());
        assertEquals("NEUTRAL", response.getStatus());
    }

    @Test
    void calculateCashFlowHealth_ShouldReturnNegative_WhenNetCashFlowIsNegative() {

        when(cashFlowService.calculateInflows())
                .thenReturn(new BigDecimal("50000"));

        when(cashFlowService.calculateOutflows())
                .thenReturn(new BigDecimal("60000"));

        when(cashFlowService.calculateNetCashFlow())
                .thenReturn(new BigDecimal("-10000"));

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("-20.00"));

        CashFlowHealthResponse response =
                financialHealthService.calculateCashFlowHealth();

        assertEquals(new BigDecimal("-10000"), response.getNetCashFlow());
        assertEquals("NEGATIVE", response.getStatus());
    }
}