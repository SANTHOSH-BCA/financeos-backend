package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultReportChangeInsightAssemblerTest {

    private final DefaultReportChangeInsightAssembler assembler =
            new DefaultReportChangeInsightAssembler();

    @Test
    void shouldBuildIncreaseInsight() {

        ReportChangeData data = new ReportChangeData();

        data.setMetric("Expenses");
        data.setCurrentValue(new BigDecimal("11400"));
        data.setPreviousValue(new BigDecimal("10000"));
        data.setAbsoluteChange(new BigDecimal("1400"));
        data.setPercentageChange(new BigDecimal("14"));
        data.setDirection("INCREASED");
        data.setSignificant(true);

        ReportChangeContributorData travel =
                new ReportChangeContributorData();

        travel.setName("Travel");
        travel.setCurrentValue(new BigDecimal("3000"));
        travel.setPreviousValue(new BigDecimal("2000"));
        travel.setAbsoluteChange(new BigDecimal("1000"));
        travel.setPercentageChange(new BigDecimal("50"));

        data.setContributors(List.of(travel));

        ReportChangeInsightV2Response result =
                assembler.assemble(data);

        assertEquals(
                "Expenses increased by 1400.00 (14.00%).",
                result.getWhatChanged()
        );

        assertEquals(
                "Travel increased by 1000.00.",
                result.getWhyDidItChange()
        );

        assertEquals(
                1,
                result.getContributors().size()
        );

        assertEquals(
                "Travel",
                result.getContributors().get(0).getName()
        );
    }

    @Test
    void shouldBuildDecreaseInsight() {

        ReportChangeData data = new ReportChangeData();

        data.setMetric("Savings");
        data.setCurrentValue(new BigDecimal("9000"));
        data.setPreviousValue(new BigDecimal("10000"));
        data.setAbsoluteChange(new BigDecimal("-1000"));
        data.setPercentageChange(new BigDecimal("-10"));
        data.setDirection("DECREASED");
        data.setSignificant(true);

        ReportChangeInsightV2Response result =
                assembler.assemble(data);

        assertEquals(
                "Savings decreased by 1000.00 (10.00%).",
                result.getWhatChanged()
        );
    }

    @Test
    void shouldHandleUnchangedMetric() {

        ReportChangeData data = new ReportChangeData();

        data.setMetric("Income");
        data.setCurrentValue(new BigDecimal("50000"));
        data.setPreviousValue(new BigDecimal("50000"));
        data.setAbsoluteChange(BigDecimal.ZERO);
        data.setPercentageChange(BigDecimal.ZERO);
        data.setDirection("UNCHANGED");

        ReportChangeInsightV2Response result =
                assembler.assemble(data);

        assertEquals(
                "Income remained unchanged.",
                result.getWhatChanged()
        );
    }

    @Test
    void shouldHandleUnavailablePercentageChange() {

        ReportChangeData data = new ReportChangeData();

        data.setMetric("Savings");
        data.setCurrentValue(BigDecimal.ZERO);
        data.setPreviousValue(new BigDecimal("-7300"));
        data.setAbsoluteChange(new BigDecimal("7300"));
        data.setPercentageChange(null);
        data.setDirection("INCREASED");
        data.setSignificant(false);

        ReportChangeInsightV2Response result =
                assembler.assemble(data);

        assertEquals(
                "Savings increased by 7300.00 (percentage change unavailable).",
                result.getWhatChanged()
        );

        assertNull(
                result.getPercentageChange()
        );

        assertFalse(
                result.isSignificant()
        );
    }

    @Test
    void shouldNotInventReasonWhenContributorsUnavailable() {

        ReportChangeData data = new ReportChangeData();

        data.setMetric("Expenses");
        data.setCurrentValue(new BigDecimal("12000"));
        data.setPreviousValue(new BigDecimal("10000"));
        data.setAbsoluteChange(new BigDecimal("2000"));
        data.setPercentageChange(new BigDecimal("20"));
        data.setDirection("INCREASED");
        data.setContributors(List.of());

        ReportChangeInsightV2Response result =
                assembler.assemble(data);

        assertEquals(
                "The available report data does not identify a specific contributor.",
                result.getWhyDidItChange()
        );
    }

    @Test
    void shouldReturnNullForNullData() {

        assertNull(
                assembler.assemble(null)
        );
    }
}
