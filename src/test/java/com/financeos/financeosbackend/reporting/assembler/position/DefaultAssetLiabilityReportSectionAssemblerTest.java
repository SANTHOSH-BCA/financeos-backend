package com.financeos.financeosbackend.reporting.assembler.position;

import com.financeos.financeosbackend.reporting.collector.position.ReportAssetData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityItemData;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAssetLiabilityReportSectionAssemblerTest {

    private final DefaultAssetLiabilityReportSectionAssembler assembler =
            new DefaultAssetLiabilityReportSectionAssembler();

    @Test
    void shouldAssembleAssets() {
        ReportAssetData data = new ReportAssetData();

        data.setRecognizedAssets(new BigDecimal("100000"));
        data.setLiquidAssets(new BigDecimal("40000"));

        data.setAllocation(Map.of(
                "CASH", new BigDecimal("10000"),
                "BANK_ACCOUNT", new BigDecimal("30000"),
                "GOLD", new BigDecimal("60000")
        ));

        AssetReportV2Response result =
                assembler.assembleAssets(data);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("100000"),
                result.getRecognizedAssets()
        );

        assertEquals(
                new BigDecimal("40000"),
                result.getLiquidAssets()
        );

        assertEquals(3, result.getAllocation().size());

        assertEquals(
                "GOLD",
                result.getAllocation().get(0).getAssetType()
        );

        assertEquals(
                new BigDecimal("60.00"),
                result.getAllocation().get(0).getPercentage()
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldAssembleLiabilities() {
        ReportLiabilityData data = new ReportLiabilityData();

        data.setRecognizedLiabilities(
                new BigDecimal("500000")
        );

        data.setDebtPayments(
                new BigDecimal("15000")
        );

        data.setPrincipalPaid(
                new BigDecimal("10000")
        );

        data.setInterestPaid(
                new BigDecimal("5000")
        );

        ReportLiabilityItemData item =
                new ReportLiabilityItemData();

        item.setLiabilityId(1L);
        item.setLiabilityName("Home Loan");
        item.setOutstandingAmount(
                new BigDecimal("500000")
        );
        item.setMonthlyPayment(
                new BigDecimal("15000")
        );
        item.setPrincipalPaid(
                new BigDecimal("10000")
        );
        item.setInterestPaid(
                new BigDecimal("5000")
        );
        item.setStatus("ACTIVE");

        data.setLiabilities(List.of(item));

        LiabilityReportV2Response result =
                assembler.assembleLiabilities(data);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("500000"),
                result.getRecognizedLiabilities()
        );

        assertEquals(
                new BigDecimal("15000"),
                result.getDebtPayments()
        );

        assertEquals(
                new BigDecimal("10000"),
                result.getPrincipalPaid()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getInterestPaid()
        );

        assertEquals(1, result.getLiabilities().size());

        assertEquals(
                "Home Loan",
                result.getLiabilities()
                        .get(0)
                        .getLiabilityName()
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                result.getMetadata().getStatus()
        );
    }

    @Test
    void shouldReturnNoDataForNullAssetData() {
        AssetReportV2Response result =
                assembler.assembleAssets(null);

        assertNotNull(result);
        assertEquals(
                ReportSectionStatus.NO_DATA,
                result.getMetadata().getStatus()
        );
        assertTrue(result.getAllocation().isEmpty());
    }

    @Test
    void shouldReturnNoDataForNullLiabilityData() {
        LiabilityReportV2Response result =
                assembler.assembleLiabilities(null);

        assertNotNull(result);
        assertEquals(
                ReportSectionStatus.NO_DATA,
                result.getMetadata().getStatus()
        );
        assertTrue(result.getLiabilities().isEmpty());
    }

    @Test
    void shouldReturnNoDataWhenThereAreNoRecognizedLiabilities() {
        ReportLiabilityData data = new ReportLiabilityData();

        data.setRecognizedLiabilities(BigDecimal.ZERO);
        data.setDebtPayments(BigDecimal.ZERO);
        data.setPrincipalPaid(BigDecimal.ZERO);
        data.setInterestPaid(BigDecimal.ZERO);
        data.setLiabilities(List.of());

        LiabilityReportV2Response result =
                assembler.assembleLiabilities(data);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                result.getMetadata().getStatus()
        );
    }
}