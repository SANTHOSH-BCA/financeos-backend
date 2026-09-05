package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.cashflow.service.CashFlowService;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthSavingsServiceTest {

    @Mock
    private CashFlowService cashFlowService;

    @InjectMocks
    private FinancialHealthSavingsService financialHealthSavingsService;

    @Test
    void calculateSavingsHealth_ShouldReturnPositive_WhenSavingsIsPositive() {

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("20000"));

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("20.00"));

        SavingsHealthResponse response =
                financialHealthSavingsService.calculateSavingsHealth();

        assertEquals(new BigDecimal("20000"), response.getSavings());
        assertEquals(new BigDecimal("20.00"), response.getSavingsRate());
        assertEquals("POSITIVE", response.getStatus());
    }

    @Test
    void calculateSavingsHealth_ShouldReturnNeutral_WhenSavingsIsZero() {

        when(cashFlowService.calculateSavings())
                .thenReturn(BigDecimal.ZERO);

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(BigDecimal.ZERO);

        SavingsHealthResponse response =
                financialHealthSavingsService.calculateSavingsHealth();

        assertEquals(BigDecimal.ZERO, response.getSavings());
        assertEquals(BigDecimal.ZERO, response.getSavingsRate());
        assertEquals("NEUTRAL", response.getStatus());
    }

    @Test
    void calculateSavingsHealth_ShouldReturnNegative_WhenSavingsIsNegative() {

        when(cashFlowService.calculateSavings())
                .thenReturn(new BigDecimal("-5000"));

        when(cashFlowService.calculateSavingsRate())
                .thenReturn(new BigDecimal("-5.00"));

        SavingsHealthResponse response =
                financialHealthSavingsService.calculateSavingsHealth();

        assertEquals(new BigDecimal("-5000"), response.getSavings());
        assertEquals(new BigDecimal("-5.00"), response.getSavingsRate());
        assertEquals("NEGATIVE", response.getStatus());
    }
}