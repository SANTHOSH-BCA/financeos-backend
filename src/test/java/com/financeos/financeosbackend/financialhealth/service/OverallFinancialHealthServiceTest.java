package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.CashFlowHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.DebtHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.InvestmentHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.OverallFinancialHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.SavingsHealthResponse;
import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverallFinancialHealthServiceTest {

    @Mock
    private FinancialHealthService financialHealthService;

    @Mock
    private FinancialHealthDebtService financialHealthDebtService;

    @Mock
    private FinancialHealthSavingsService financialHealthSavingsService;

    @Mock
    private FinancialHealthInvestmentService financialHealthInvestmentService;

    @Mock
    private FinancialHealthGoalService financialHealthGoalService;

    @Mock
    private FinancialHealthWealthService financialHealthWealthService;

    @InjectMocks
    private OverallFinancialHealthService overallFinancialHealthService;

    @Test
    void calculateOverallFinancialHealth_ShouldReturnHealthy_WhenCoreIndicatorsAreHealthy() {

        when(financialHealthService.calculateCashFlowHealth())
                .thenReturn(new CashFlowHealthResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("70000"),
                        new BigDecimal("30000"),
                        new BigDecimal("30.00"),
                        "POSITIVE"
                ));

        when(financialHealthDebtService.calculateDebtHealth())
                .thenReturn(new DebtHealthResponse(
                        BigDecimal.ZERO,
                        "NO_DEBT"
                ));

        when(financialHealthSavingsService.calculateSavingsHealth())
                .thenReturn(new SavingsHealthResponse(
                        new BigDecimal("30000"),
                        new BigDecimal("30.00"),
                        "POSITIVE"
                ));

        when(financialHealthInvestmentService.calculateInvestmentHealth())
                .thenReturn(new InvestmentHealthResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("115000"),
                        new BigDecimal("15000"),
                        new BigDecimal("15.00"),
                        "POSITIVE"
                ));

        when(financialHealthGoalService.calculateGoalHealth())
                .thenReturn(new GoalHealthResponse(
                        2L,
                        0L,
                        0L,
                        2L,
                        new BigDecimal("150000"),
                        new BigDecimal("60000"),
                        "ON_TRACK"
                ));

        when(financialHealthWealthService.calculateWealthHealth())
                .thenReturn(new WealthHealthResponse(
                        new BigDecimal("500000"),
                        new BigDecimal("200000"),
                        new BigDecimal("300000"),
                        "POSITIVE"
                ));

        OverallFinancialHealthResponse response =
                overallFinancialHealthService.calculateOverallFinancialHealth();

        assertEquals("POSITIVE", response.getCashFlowStatus());
        assertEquals("NO_DEBT", response.getDebtStatus());
        assertEquals("POSITIVE", response.getSavingsStatus());
        assertEquals("POSITIVE", response.getInvestmentStatus());
        assertEquals("ON_TRACK", response.getGoalStatus());
        assertEquals("POSITIVE", response.getWealthStatus());
        assertEquals("HEALTHY", response.getOverallStatus());
    }

    @Test
    void calculateOverallFinancialHealth_ShouldReturnNeedsAttention_WhenCashFlowIsNegative() {

        when(financialHealthService.calculateCashFlowHealth())
                .thenReturn(new CashFlowHealthResponse(
                        new BigDecimal("50000"),
                        new BigDecimal("60000"),
                        new BigDecimal("-10000"),
                        new BigDecimal("-20.00"),
                        "NEGATIVE"
                ));

        when(financialHealthDebtService.calculateDebtHealth())
                .thenReturn(new DebtHealthResponse(
                        new BigDecimal("100000"),
                        "DEBT_PRESENT"
                ));

        when(financialHealthSavingsService.calculateSavingsHealth())
                .thenReturn(new SavingsHealthResponse(
                        new BigDecimal("-10000"),
                        new BigDecimal("-20.00"),
                        "NEGATIVE"
                ));

        when(financialHealthInvestmentService.calculateInvestmentHealth())
                .thenReturn(new InvestmentHealthResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("90000"),
                        new BigDecimal("-10000"),
                        new BigDecimal("-10.00"),
                        "NEGATIVE"
                ));

        when(financialHealthGoalService.calculateGoalHealth())
                .thenReturn(new GoalHealthResponse(
                        1L,
                        0L,
                        1L,
                        0L,
                        new BigDecimal("100000"),
                        new BigDecimal("20000"),
                        "AT_RISK"
                ));

        when(financialHealthWealthService.calculateWealthHealth())
                .thenReturn(new WealthHealthResponse(
                        new BigDecimal("200000"),
                        new BigDecimal("300000"),
                        new BigDecimal("-100000"),
                        "NEGATIVE"
                ));

        OverallFinancialHealthResponse response =
                overallFinancialHealthService.calculateOverallFinancialHealth();

        assertEquals("NEGATIVE", response.getCashFlowStatus());
        assertEquals("DEBT_PRESENT", response.getDebtStatus());
        assertEquals("NEGATIVE", response.getSavingsStatus());
        assertEquals("NEGATIVE", response.getInvestmentStatus());
        assertEquals("AT_RISK", response.getGoalStatus());
        assertEquals("NEGATIVE", response.getWealthStatus());
        assertEquals("NEEDS_ATTENTION", response.getOverallStatus());
    }

    @Test
    void calculateOverallFinancialHealth_ShouldReturnStable_WhenIndicatorsAreMixed() {

        when(financialHealthService.calculateCashFlowHealth())
                .thenReturn(new CashFlowHealthResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("90000"),
                        new BigDecimal("10000"),
                        new BigDecimal("10.00"),
                        "POSITIVE"
                ));

        when(financialHealthDebtService.calculateDebtHealth())
                .thenReturn(new DebtHealthResponse(
                        new BigDecimal("200000"),
                        "DEBT_PRESENT"
                ));

        when(financialHealthSavingsService.calculateSavingsHealth())
                .thenReturn(new SavingsHealthResponse(
                        new BigDecimal("10000"),
                        new BigDecimal("10.00"),
                        "POSITIVE"
                ));

        when(financialHealthInvestmentService.calculateInvestmentHealth())
                .thenReturn(new InvestmentHealthResponse(
                        new BigDecimal("100000"),
                        new BigDecimal("100000"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        "NEUTRAL"
                ));

        when(financialHealthGoalService.calculateGoalHealth())
                .thenReturn(new GoalHealthResponse(
                        2L,
                        1L,
                        0L,
                        1L,
                        new BigDecimal("150000"),
                        new BigDecimal("100000"),
                        "ON_TRACK"
                ));

        when(financialHealthWealthService.calculateWealthHealth())
                .thenReturn(new WealthHealthResponse(
                        new BigDecimal("500000"),
                        new BigDecimal("400000"),
                        new BigDecimal("100000"),
                        "POSITIVE"
                ));

        OverallFinancialHealthResponse response =
                overallFinancialHealthService.calculateOverallFinancialHealth();

        assertEquals("HEALTHY", response.getOverallStatus());
    }
}