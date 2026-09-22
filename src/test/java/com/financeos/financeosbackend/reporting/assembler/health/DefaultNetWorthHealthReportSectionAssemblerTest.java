package com.financeos.financeosbackend.reporting.assembler.health;

import com.financeos.financeosbackend.reporting.collector.health.ReportFinancialHealthData;
import com.financeos.financeosbackend.reporting.collector.health.ReportNetWorthData;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNetWorthHealthReportSectionAssemblerTest {

    private final DefaultNetWorthHealthReportSectionAssembler assembler =
            new DefaultNetWorthHealthReportSectionAssembler();

    @Test
    void shouldAssembleNetWorth() {
        ReportNetWorthData data = new ReportNetWorthData();

        data.setRecognizedAssets(
                new BigDecimal("100000")
        );

        data.setRecognizedLiabilities(
                new BigDecimal("40000")
        );

        data.setNetWorth(
                new BigDecimal("60000")
        );

        data.setHistoricalDataAvailable(false);

        NetWorthReportV2Response result =
                assembler.assembleNetWorth(data);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("100000"),
                result.getRecognizedAssets()
        );

        assertEquals(
                new BigDecimal("40000"),
                result.getRecognizedLiabilities()
        );

        assertEquals(
                new BigDecimal("60000"),
                result.getNetWorth()
        );

        assertEquals(
                ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldAssembleFinancialHealth() {
        ReportFinancialHealthData data =
                new ReportFinancialHealthData();

        data.setCashFlowHealth("POSITIVE");
        data.setDebtHealth("DEBT_PRESENT");
        data.setSavingsHealth("POSITIVE");
        data.setInvestmentHealth("POSITIVE");
        data.setGoalHealth("ON_TRACK");
        data.setWealthHealth("POSITIVE");
        data.setOverallStatus("HEALTHY");

        data.setHistoricalDataAvailable(false);

        FinancialHealthReportV2Response result =
                assembler.assembleFinancialHealth(data);

        assertNotNull(result);

        assertEquals(
                "POSITIVE",
                result.getCashFlowHealth()
        );

        assertEquals(
                "DEBT_PRESENT",
                result.getDebtHealth()
        );

        assertEquals(
                "POSITIVE",
                result.getSavingsHealth()
        );

        assertEquals(
                "POSITIVE",
                result.getInvestmentHealth()
        );

        assertEquals(
                "ON_TRACK",
                result.getGoalHealth()
        );

        assertEquals(
                "POSITIVE",
                result.getWealthHealth()
        );

        assertEquals(
                "HEALTHY",
                result.getOverallStatus()
        );

        assertEquals(
                ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldReturnNoDataForNullNetWorthData() {
        NetWorthReportV2Response result =
                assembler.assembleNetWorth(null);

        assertNotNull(result);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldReturnNoDataForNullFinancialHealthData() {
        FinancialHealthReportV2Response result =
                assembler.assembleFinancialHealth(null);

        assertNotNull(result);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkHistoricalNetWorthAvailableWhenProvided() {
        ReportNetWorthData data = new ReportNetWorthData();

        data.setRecognizedAssets(
                new BigDecimal("100000")
        );

        data.setRecognizedLiabilities(
                new BigDecimal("30000")
        );

        data.setNetWorth(
                new BigDecimal("70000")
        );

        data.setHistoricalDataAvailable(true);

        NetWorthReportV2Response result =
                assembler.assembleNetWorth(data);

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                result.getMetadata().getStatus()
        );
    }
}