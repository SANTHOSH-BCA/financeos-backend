package com.financeos.financeosbackend.reporting.comparison;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ReportComparisonServiceTest {

    private final ReportComparisonService service =
            new ReportComparisonService();

    @Test
    void shouldCalculatePositiveBaselinePercentage() {
        ReportMetricChangeData result =
                service.calculateChange(
                        new BigDecimal("120"),
                        new BigDecimal("100")
                );

        assertEquals(
                new BigDecimal("20.00"),
                result.getAbsoluteChange()
        );

        assertEquals(
                new BigDecimal("20.00"),
                result.getPercentageChange()
        );
    }

    @Test
    void shouldKeepZeroPercentageWhenPreviousValueIsZero() {
        ReportMetricChangeData result =
                service.calculateChange(
                        new BigDecimal("5000"),
                        BigDecimal.ZERO
                );

        assertEquals(
                new BigDecimal("5000.00"),
                result.getAbsoluteChange()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getPercentageChange()
        );
    }

    @Test
    void shouldTreatNegativePreviousValuePercentageAsUnavailable() {
        ReportMetricChangeData result =
                service.calculateChange(
                        BigDecimal.ZERO,
                        new BigDecimal("-7300")
                );

        assertEquals(
                new BigDecimal("7300.00"),
                result.getAbsoluteChange()
        );

        assertNull(result.getPercentageChange());
    }

    @Test
    void shouldHandleNullValuesAsZero() {
        ReportMetricChangeData result =
                service.calculateChange(
                        null,
                        null
                );

        assertEquals(
                BigDecimal.ZERO.setScale(2),
                result.getAbsoluteChange()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getPercentageChange()
        );
    }
}
