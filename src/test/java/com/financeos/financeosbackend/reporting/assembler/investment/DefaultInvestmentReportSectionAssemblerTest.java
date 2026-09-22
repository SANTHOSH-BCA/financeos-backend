package com.financeos.financeosbackend.reporting.assembler.investment;

import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentData;
import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentHoldingData;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultInvestmentReportSectionAssemblerTest {

    private final DefaultInvestmentReportSectionAssembler assembler =
            new DefaultInvestmentReportSectionAssembler();

    @Test
    void shouldAssembleInvestmentSummary() {

        ReportInvestmentHoldingData holding =
                new ReportInvestmentHoldingData();

        holding.setInvestmentId(1L);
        holding.setInvestmentName("Test Investment");
        holding.setInvestmentType("MUTUAL_FUND");
        holding.setInvestedAmount(new BigDecimal("10000"));
        holding.setCurrentValue(new BigDecimal("12000"));
        holding.setProfitLoss(new BigDecimal("2000"));
        holding.setReturnPercentage(new BigDecimal("20"));

        ReportInvestmentData data =
                new ReportInvestmentData();

        data.setInvestedAmount(new BigDecimal("10000"));
        data.setPortfolioValue(new BigDecimal("12000"));
        data.setProfitLoss(new BigDecimal("2000"));
        data.setReturnPercentage(new BigDecimal("20"));

        data.setAssetAllocation(
                new HashMap<>()
        );

        data.setHoldings(
                List.of(holding)
        );

        data.setHistoricalDataAvailable(true);

        InvestmentReportV2Response response =
                assembler.assemble(data);

        assertEquals(
                0,
                response.getInvestedAmount()
                        .compareTo(new BigDecimal("10000"))
        );

        assertEquals(
                0,
                response.getPortfolioValue()
                        .compareTo(new BigDecimal("12000"))
        );

        assertEquals(
                0,
                response.getProfitLoss()
                        .compareTo(new BigDecimal("2000"))
        );

        /*
         * Complete holdings must be preserved.
         * This list is used for immutable report snapshot persistence.
         */
        assertEquals(
                1,
                response.getHoldings().size()
        );

        assertEquals(
                1L,
                response.getHoldings()
                        .get(0)
                        .getInvestmentId()
        );

        assertEquals(
                "Test Investment",
                response.getHoldings()
                        .get(0)
                        .getInvestmentName()
        );

        assertEquals(
                "MUTUAL_FUND",
                response.getHoldings()
                        .get(0)
                        .getInvestmentType()
        );

        assertEquals(
                0,
                response.getHoldings()
                        .get(0)
                        .getInvestedAmount()
                        .compareTo(new BigDecimal("10000"))
        );

        assertEquals(
                0,
                response.getHoldings()
                        .get(0)
                        .getCurrentValue()
                        .compareTo(new BigDecimal("12000"))
        );

        assertEquals(
                0,
                response.getHoldings()
                        .get(0)
                        .getProfitLoss()
                        .compareTo(new BigDecimal("2000"))
        );

        assertEquals(
                0,
                response.getHoldings()
                        .get(0)
                        .getReturnPercentage()
                        .compareTo(new BigDecimal("20"))
        );

        /*
         * Best/worst performers remain presentation subsets.
         */
        assertEquals(
                1,
                response.getBestPerformers().size()
        );

        assertEquals(
                1,
                response.getWorstPerformers().size()
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkHistoricalDataUnavailable() {

        ReportInvestmentData data =
                new ReportInvestmentData();

        data.setInvestedAmount(
                new BigDecimal("10000")
        );

        data.setPortfolioValue(
                new BigDecimal("11000")
        );

        data.setProfitLoss(
                new BigDecimal("1000")
        );

        data.setReturnPercentage(
                new BigDecimal("10")
        );

        data.setAssetAllocation(
                new HashMap<>()
        );

        data.setHistoricalDataAvailable(false);

        InvestmentReportV2Response response =
                assembler.assemble(data);

        assertEquals(
                ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkNoDataWhenPortfolioIsEmpty() {

        ReportInvestmentData data =
                new ReportInvestmentData();

        data.setInvestedAmount(
                BigDecimal.ZERO
        );

        data.setPortfolioValue(
                BigDecimal.ZERO
        );

        data.setProfitLoss(
                BigDecimal.ZERO
        );

        data.setReturnPercentage(
                BigDecimal.ZERO
        );

        data.setAssetAllocation(
                new HashMap<>()
        );

        data.setHistoricalDataAvailable(false);

        InvestmentReportV2Response response =
                assembler.assemble(data);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldRejectNullData() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assembler.assemble(null)
                );

        assertEquals(
                "Investment report data must not be null",
                exception.getMessage()
        );
    }
}