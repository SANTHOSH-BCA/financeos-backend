package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialFutureResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialHealthResponse;
import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.liability.dto.DebtNetWorthImpactResponse;
import com.financeos.financeosbackend.liability.dto.LiabilityV2ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LiabilityV2ApiServiceTest {

    @Mock
    private DebtBurdenService debtBurdenService;

    @Mock
    private DebtInvestmentTradeoffService debtInvestmentTradeoffService;

    @Mock
    private DebtNetWorthImpactService debtNetWorthImpactService;

    @Mock
    private DebtFinancialHealthService debtFinancialHealthService;

    @Mock
    private DebtFinancialFutureService debtFinancialFutureService;

    @InjectMocks
    private LiabilityV2ApiService service;

    @Test
    void getOverview_ShouldAggregateLiabilityEngines() {

        DebtBurdenResponse burden =
                new DebtBurdenResponse();

        burden.setTotalOutstanding(
                new BigDecimal("500000.00")
        );
        burden.setTotalMonthlyPayment(
                new BigDecimal("15000.00")
        );
        burden.setDebtPaymentRatio(
                new BigDecimal("25.00")
        );
        burden.setActiveLiabilityCount(2);
        burden.setBurdenLevel("MODERATE");

        DebtInvestmentTradeoffResponse tradeoff =
                new DebtInvestmentTradeoffResponse(
                        new BigDecimal("500000.00"),
                        new BigDecimal("800000.00"),
                        new BigDecimal("62.50"),
                        "MODERATE",
                        "Debt and investment position are being compared analytically."
                );

        DebtNetWorthImpactResponse netWorth =
                new DebtNetWorthImpactResponse(
                        new BigDecimal("1200000.00"),
                        BigDecimal.ZERO,
                        new BigDecimal("500000.00"),
                        new BigDecimal("700000.00"),
                        new BigDecimal("41.67"),
                        "Recognized liabilities reduce net worth."
                );

        DebtFinancialHealthResponse health =
                new DebtFinancialHealthResponse(
                        new BigDecimal("500000.00"),
                        new BigDecimal("15000.00"),
                        new BigDecimal("25.00"),
                        2,
                        0,
                        new BigDecimal("100000.00"),
                        new BigDecimal("20000.00"),
                        new BigDecimal("120000.00"),
                        "MODERATE_BURDEN",
                        "Debt payments represent a moderate share of current income."
                );

        DebtFinancialFutureResponse future =
                new DebtFinancialFutureResponse(
                        new BigDecimal("500000.00"),
                        new BigDecimal("15000.00"),
                        34,
                        new BigDecimal("510000.00"),
                        "PROJECTABLE",
                        "Future debt cash flow can be projected."
                );

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(burden);

        when(debtInvestmentTradeoffService.getTradeoff())
                .thenReturn(tradeoff);

        when(debtNetWorthImpactService.getImpact())
                .thenReturn(netWorth);

        when(debtFinancialHealthService.getDebtFinancialHealth())
                .thenReturn(health);

        when(debtFinancialFutureService.getDebtFinancialFuture())
                .thenReturn(future);

        LiabilityV2ApiResponse response =
                service.getOverview();

        assertEquals(
                new BigDecimal("500000.00"),
                response.getTotalOutstandingDebt()
        );

        assertEquals(
                new BigDecimal("15000.00"),
                response.getTotalMonthlyDebtPayment()
        );

        assertEquals(
                new BigDecimal("800000.00"),
                response.getTotalInvestmentValue()
        );

        assertEquals(
                new BigDecimal("700000.00"),
                response.getNetWorth()
        );

        assertEquals(
                new BigDecimal("25.00"),
                response.getDebtPaymentRatio()
        );

        assertEquals(
                new BigDecimal("62.50"),
                response.getDebtToInvestmentRatio()
        );

        assertEquals(
                2,
                response.getActiveLiabilityCount()
        );

        assertEquals(
                "MODERATE",
                response.getDebtBurdenLevel()
        );

        assertEquals(
                "MODERATE_BURDEN",
                response.getDebtHealth()
        );

        assertEquals(
                "PROJECTABLE",
                response.getProjectionStatus()
        );
    }
}