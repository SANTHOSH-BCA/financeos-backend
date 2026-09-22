package com.financeos.financeosbackend.reporting.comparison;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class ReportComparisonService {

    public ReportPeriodResponse resolvePreviousPeriod(
            ReportPeriodResponse currentPeriod
    ) {
        if (currentPeriod == null
                || currentPeriod.getStartDate() == null
                || currentPeriod.getEndDate() == null
                || currentPeriod.getPeriodType() == null) {

            throw new IllegalArgumentException(
                    "Current report period must be valid."
            );
        }

        LocalDate start = currentPeriod.getStartDate();
        LocalDate end = currentPeriod.getEndDate();

        return switch (currentPeriod.getPeriodType()) {

            case WEEKLY -> new ReportPeriodResponse(
                    ReportPeriodType.WEEKLY,
                    start.minusWeeks(1),
                    end.minusWeeks(1)
            );

            case MONTHLY -> new ReportPeriodResponse(
                    ReportPeriodType.MONTHLY,
                    start.minusMonths(1),
                    start.minusMonths(1)
                            .withDayOfMonth(
                                    start.minusMonths(1)
                                            .lengthOfMonth()
                            )
            );

            case QUARTERLY -> {
                LocalDate previousStart = start.minusMonths(3);
                LocalDate previousEnd =
                        previousStart.plusMonths(2)
                                .withDayOfMonth(
                                        previousStart.plusMonths(2)
                                                .lengthOfMonth()
                                );

                yield new ReportPeriodResponse(
                        ReportPeriodType.QUARTERLY,
                        previousStart,
                        previousEnd
                );
            }

            case YEARLY -> new ReportPeriodResponse(
                    ReportPeriodType.YEARLY,
                    start.minusYears(1),
                    end.minusYears(1)
            );

            case CUSTOM -> {
                long days = end.toEpochDay() - start.toEpochDay() + 1;

                LocalDate previousEnd = start.minusDays(1);
                LocalDate previousStart =
                        previousEnd.minusDays(days - 1);

                yield new ReportPeriodResponse(
                        ReportPeriodType.CUSTOM,
                        previousStart,
                        previousEnd
                );
            }
        };
    }

    public ReportMetricChangeData calculateChange(
            BigDecimal currentValue,
            BigDecimal previousValue
    ) {
        BigDecimal current =
                currentValue == null
                        ? BigDecimal.ZERO
                        : currentValue;

        BigDecimal previous =
                previousValue == null
                        ? BigDecimal.ZERO
                        : previousValue;

        BigDecimal absoluteChange =
                current.subtract(previous);

        BigDecimal percentageChange = calculatePercentageChange(
                absoluteChange,
                previous
        );

        ReportMetricChangeData result =
                new ReportMetricChangeData();

        result.setCurrentValue(current);
        result.setPreviousValue(previous);
        result.setAbsoluteChange(
                absoluteChange.setScale(2, RoundingMode.HALF_UP)
        );
        result.setPercentageChange(percentageChange);

        return result;
    }

    private BigDecimal calculatePercentageChange(
            BigDecimal absoluteChange,
            BigDecimal previousValue
    ) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        if (previousValue.compareTo(BigDecimal.ZERO) < 0) {
            return null;
        }

        return absoluteChange
                .divide(
                        previousValue,
                        6,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
