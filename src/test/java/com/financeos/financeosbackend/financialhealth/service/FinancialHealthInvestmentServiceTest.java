package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthInvestmentServiceTest {

    @Mock
    private InvestmentService investmentService;

    @InjectMocks
    private FinancialHealthInvestmentService financialHealthInvestmentService;

    @Test
    void calculateInvestmentHealth_ShouldReturnPositive_WhenProfitIsPositive() {

        InvestmentPerformanceResponse performance =
                new InvestmentPerformanceResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("115000"),
                        new BigDecimal("15000"),
                        new BigDecimal("15.00"),
                        4L
                );

        when(investmentService.getPortfolioPerformance())
                .thenReturn(performance);

        InvestmentHealthResponse response =
                financialHealthInvestmentService.calculateInvestmentHealth();

        assertEquals(new BigDecimal("100000"), response.getTotalInvestedAmount());
        assertEquals(new BigDecimal("115000"), response.getCurrentPortfolioValue());
        assertEquals(new BigDecimal("15000"), response.getTotalProfitLoss());
        assertEquals(new BigDecimal("15.00"), response.getReturnPercentage());
        assertEquals("POSITIVE", response.getStatus());
    }

    @Test
    void calculateInvestmentHealth_ShouldReturnNeutral_WhenProfitIsZero() {

        InvestmentPerformanceResponse performance =
                new InvestmentPerformanceResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("100000"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        4L
                );

        when(investmentService.getPortfolioPerformance())
                .thenReturn(performance);

        InvestmentHealthResponse response =
                financialHealthInvestmentService.calculateInvestmentHealth();

        assertEquals("NEUTRAL", response.getStatus());
    }

    @Test
    void calculateInvestmentHealth_ShouldReturnNegative_WhenProfitIsNegative() {

        InvestmentPerformanceResponse performance =
                new InvestmentPerformanceResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("90000"),
                        new BigDecimal("-10000"),
                        new BigDecimal("-10.00"),
                        4L
                );

        when(investmentService.getPortfolioPerformance())
                .thenReturn(performance);

        InvestmentHealthResponse response =
                financialHealthInvestmentService.calculateInvestmentHealth();

        assertEquals("NEGATIVE", response.getStatus());
    }

    @Test
    void calculateInvestmentHealth_ShouldReturnNoInvestments_WhenNothingIsInvested() {

        InvestmentPerformanceResponse performance =
                new InvestmentPerformanceResponse(
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        0L
                );

        when(investmentService.getPortfolioPerformance())
                .thenReturn(performance);

        InvestmentHealthResponse response =
                financialHealthInvestmentService.calculateInvestmentHealth();

        assertEquals("NO_INVESTMENTS", response.getStatus());
    }
}