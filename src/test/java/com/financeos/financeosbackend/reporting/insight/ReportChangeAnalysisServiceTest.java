package com.financeos.financeosbackend.reporting.insight;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportChangeAnalysisServiceTest {

    private final ReportChangeAnalysisService service =
            new ReportChangeAnalysisService();

    @Test
    void shouldDetectIncrease() {
        ReportChangeData result =
                service.analyzeMetric(
                        "Expenses",
                        new BigDecimal("11400"),
                        new BigDecimal("10000")
                );

        assertEquals("Expenses", result.getMetric());
        assertEquals(
                new BigDecimal("11400.00"),
                result.getCurrentValue()
        );
        assertEquals(
                new BigDecimal("10000.00"),
                result.getPreviousValue()
        );
        assertEquals(
                new BigDecimal("1400.00"),
                result.getAbsoluteChange()
        );
        assertEquals(
                new BigDecimal("14.00"),
                result.getPercentageChange()
        );
        assertEquals("INCREASED", result.getDirection());
        assertTrue(result.isSignificant());
    }

    @Test
    void shouldDetectDecrease() {
        ReportChangeData result =
                service.analyzeMetric(
                        "Savings",
                        new BigDecimal("9400"),
                        new BigDecimal("10000")
                );

        assertEquals(
                new BigDecimal("-600.00"),
                result.getAbsoluteChange()
        );

        assertEquals(
                new BigDecimal("-6.00"),
                result.getPercentageChange()
        );

        assertEquals(
                "DECREASED",
                result.getDirection()
        );

        assertTrue(result.isSignificant());
    }

    @Test
    void shouldDetectUnchangedMetric() {
        ReportChangeData result =
                service.analyzeMetric(
                        "Income",
                        new BigDecimal("50000"),
                        new BigDecimal("50000")
                );

        assertEquals(
                BigDecimal.ZERO.setScale(2),
                result.getAbsoluteChange()
        );

        assertEquals(
                BigDecimal.ZERO.setScale(2),
                result.getPercentageChange()
        );

        assertEquals(
                "UNCHANGED",
                result.getDirection()
        );

        assertFalse(result.isSignificant());
    }

    @Test
    void shouldHandleZeroPreviousValue() {
        ReportChangeData result =
                service.analyzeMetric(
                        "Income",
                        new BigDecimal("50000"),
                        BigDecimal.ZERO
                );

        assertEquals(
                new BigDecimal("50000.00"),
                result.getAbsoluteChange()
        );

        assertEquals(
                0,
                result.getPercentageChange().compareTo(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldTreatNegativePreviousValuePercentageAsUnavailable() {
        ReportChangeData result =
                service.analyzeMetric(
                        "Savings",
                        BigDecimal.ZERO,
                        new BigDecimal("-7300")
                );

        assertEquals(
                new BigDecimal("7300.00"),
                result.getAbsoluteChange()
        );

        assertNull(result.getPercentageChange());

        assertEquals(
                "INCREASED",
                result.getDirection()
        );

        assertFalse(result.isSignificant());
    }

    @Test
    void shouldIdentifyMajorContributors() {
        Map<String, BigDecimal> previous =
                Map.of(
                        "Travel", new BigDecimal("10000"),
                        "Food", new BigDecimal("5000"),
                        "Shopping", new BigDecimal("4000")
                );

        Map<String, BigDecimal> current =
                Map.of(
                        "Travel", new BigDecimal("13200"),
                        "Food", new BigDecimal("5600"),
                        "Shopping", new BigDecimal("4720")
                );

        List<ReportChangeContributorData> result =
                service.identifyContributors(
                        current,
                        previous
                );

        assertEquals(3, result.size());

        assertEquals(
                "Travel",
                result.get(0).getName()
        );

        assertEquals(
                new BigDecimal("3200.00"),
                result.get(0).getAbsoluteChange()
        );

        assertEquals(
                "Shopping",
                result.get(1).getName()
        );

        assertEquals(
                "Food",
                result.get(2).getName()
        );
    }

    @Test
    void shouldIncludeNewContributor() {
        Map<String, BigDecimal> previous =
                Map.of(
                        "Food", new BigDecimal("5000")
                );

        Map<String, BigDecimal> current =
                Map.of(
                        "Food", new BigDecimal("5500"),
                        "Travel", new BigDecimal("3000")
                );

        List<ReportChangeContributorData> result =
                service.identifyContributors(
                        current,
                        previous
                );

        assertEquals(2, result.size());

        assertEquals(
                "Travel",
                result.get(0).getName()
        );

        assertEquals(
                new BigDecimal("3000.00"),
                result.get(0).getAbsoluteChange()
        );
    }

    @Test
    void shouldTreatNegativePreviousContributorPercentageAsUnavailable() {
        Map<String, BigDecimal> previous =
                Map.of(
                        "Savings", new BigDecimal("-7300")
                );

        Map<String, BigDecimal> current =
                Map.of(
                        "Savings", BigDecimal.ZERO
                );

        List<ReportChangeContributorData> result =
                service.identifyContributors(
                        current,
                        previous
                );

        assertEquals(1, result.size());

        assertEquals(
                new BigDecimal("7300.00"),
                result.get(0).getAbsoluteChange()
        );

        assertNull(
                result.get(0).getPercentageChange()
        );
    }
}
