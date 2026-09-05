package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
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
class FinancialHealthDebtServiceTest {

    @Mock
    private NetWorthService netWorthService;

    @InjectMocks
    private FinancialHealthDebtService financialHealthDebtService;

    @Test
    void calculateDebtHealth_ShouldReturnNoDebt_WhenRecognizedLiabilitiesAreZero() {

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(BigDecimal.ZERO);

        DebtHealthResponse response =
                financialHealthDebtService.calculateDebtHealth();

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getRecognizedLiabilities());
        assertEquals("NO_DEBT", response.getStatus());
    }

    @Test
    void calculateDebtHealth_ShouldReturnDebtPresent_WhenRecognizedLiabilitiesExist() {

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("250000"));

        DebtHealthResponse response =
                financialHealthDebtService.calculateDebtHealth();

        assertNotNull(response);
        assertEquals(
                new BigDecimal("250000"),
                response.getRecognizedLiabilities()
        );
        assertEquals("DEBT_PRESENT", response.getStatus());
    }
}