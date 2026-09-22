package com.financeos.financeosbackend.reporting.assembler.comparison;

import com.financeos.financeosbackend.reporting.comparison.ReportComparisonData;
import com.financeos.financeosbackend.reporting.comparison.ReportMetricChangeData;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DefaultReportComparisonSectionAssemblerTest {

    private final DefaultReportComparisonSectionAssembler assembler =
            new DefaultReportComparisonSectionAssembler();

    @Test
    void shouldAssembleComparison() {
        ReportPeriodResponse current =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        ReportPeriodResponse previous =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31)
                );

        ReportMetricChangeData income =
                createMetric(
                        "50000",
                        "45000",
                        "5000",
                        "11.11"
                );

        ReportMetricChangeData expenses =
                createMetric(
                        "20000",
                        "18000",
                        "2000",
                        "11.11"
                );

        ReportMetricChangeData savings =
                createMetric(
                        "30000",
                        "27000",
                        "3000",
                        "11.11"
                );

        ReportComparisonData data =
                new ReportComparisonData();

        data.setComparisonAvailable(true);
        data.setCurrentPeriod(current);
        data.setPreviousPeriod(previous);
        data.setIncome(income);
        data.setExpenses(expenses);
        data.setSavings(savings);

        ReportComparisonV2Response result =
                assembler.assemble(data);

        assertNotNull(result);

        assertTrue(result.isComparisonAvailable());

        assertSame(current, result.getCurrentPeriod());
        assertSame(previous, result.getPreviousPeriod());

        assertNotNull(result.getIncome());

        assertEquals(
                new BigDecimal("50000"),
                result.getIncome().getCurrentValue()
        );

        assertEquals(
                new BigDecimal("45000"),
                result.getIncome().getPreviousValue()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getIncome().getAbsoluteChange()
        );

        assertEquals(
                new BigDecimal("11.11"),
                result.getIncome().getPercentageChange()
        );

        assertNotNull(result.getExpenses());
        assertNotNull(result.getSavings());

        assertNull(result.getInvestments());
        assertNull(result.getNetWorth());
    }

    @Test
    void shouldReturnUnavailableWhenDataIsNull() {
        ReportComparisonV2Response result =
                assembler.assemble(null);

        assertNotNull(result);
        assertFalse(result.isComparisonAvailable());
        assertNull(result.getCurrentPeriod());
        assertNull(result.getPreviousPeriod());
        assertNull(result.getIncome());
        assertNull(result.getExpenses());
    }

    @Test
    void shouldPreserveUnavailableHistoricalMetrics() {
        ReportComparisonData data =
                new ReportComparisonData();

        data.setComparisonAvailable(true);

        ReportMetricChangeData income =
                createMetric(
                        "50000",
                        "45000",
                        "5000",
                        "11.11"
                );

        data.setIncome(income);
        data.setInvestments(null);
        data.setNetWorth(null);

        ReportComparisonV2Response result =
                assembler.assemble(data);

        assertTrue(result.isComparisonAvailable());
        assertNotNull(result.getIncome());
        assertNull(result.getInvestments());
        assertNull(result.getNetWorth());
    }

    private ReportMetricChangeData createMetric(
            String current,
            String previous,
            String absoluteChange,
            String percentageChange
    ) {
        ReportMetricChangeData data =
                new ReportMetricChangeData();

        data.setCurrentValue(new BigDecimal(current));
        data.setPreviousValue(new BigDecimal(previous));
        data.setAbsoluteChange(new BigDecimal(absoluteChange));
        data.setPercentageChange(new BigDecimal(percentageChange));

        return data;
    }
}