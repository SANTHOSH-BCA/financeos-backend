package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthWealthServiceTest {

    @Mock
    private NetWorthService netWorthService;

    @InjectMocks
    private FinancialHealthWealthService financialHealthWealthService;

    @Test
    void calculateWealthHealth_ShouldReturnPositive_WhenNetWorthIsPositive() {

        when(netWorthService.calculateRecognizedAssets())
                .thenReturn(new BigDecimal("500000"));

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("200000"));

        when(netWorthService.calculateNetWorth())
                .thenReturn(new BigDecimal("300000"));

        WealthHealthResponse response =
                financialHealthWealthService.calculateWealthHealth();

        assertEquals(
                new BigDecimal("500000"),
                response.getRecognizedAssets()
        );
        assertEquals(
                new BigDecimal("200000"),
                response.getRecognizedLiabilities()
        );
        assertEquals(
                new BigDecimal("300000"),
                response.getNetWorth()
        );
        assertEquals("POSITIVE", response.getStatus());
    }

    @Test
    void calculateWealthHealth_ShouldReturnNeutral_WhenNetWorthIsZero() {

        when(netWorthService.calculateRecognizedAssets())
                .thenReturn(new BigDecimal("200000"));

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("200000"));

        when(netWorthService.calculateNetWorth())
                .thenReturn(BigDecimal.ZERO);

        WealthHealthResponse response =
                financialHealthWealthService.calculateWealthHealth();

        assertEquals("NEUTRAL", response.getStatus());
    }

    @Test
    void calculateWealthHealth_ShouldReturnNegative_WhenNetWorthIsNegative() {

        when(netWorthService.calculateRecognizedAssets())
                .thenReturn(new BigDecimal("100000"));

        when(netWorthService.calculateRecognizedLiabilities())
                .thenReturn(new BigDecimal("150000"));

        when(netWorthService.calculateNetWorth())
                .thenReturn(new BigDecimal("-50000"));

        WealthHealthResponse response =
                financialHealthWealthService.calculateWealthHealth();

        assertEquals("NEGATIVE", response.getStatus());
    }
}