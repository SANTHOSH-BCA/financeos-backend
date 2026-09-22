package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodRequest;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class ReportPeriodService {

    public ReportPeriodResponse resolvePeriod(
            ReportPeriodRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Report period request is required"
            );
        }

        ReportPeriodType periodType = request.getPeriodType();

        if (periodType == null) {
            throw new IllegalArgumentException(
                    "Report period type is required"
            );
        }

        return switch (periodType) {

            case CUSTOM ->
                    resolveCustomPeriod(request);

            case WEEKLY ->
                    resolveStandardPeriod(
                            periodType,
                            request.getStartDate(),
                            request.getEndDate(),
                            this::validateWeeklyPeriod
                    );

            case MONTHLY ->
                    resolveStandardPeriod(
                            periodType,
                            request.getStartDate(),
                            request.getEndDate(),
                            this::validateMonthlyPeriod
                    );

            case QUARTERLY ->
                    resolveStandardPeriod(
                            periodType,
                            request.getStartDate(),
                            request.getEndDate(),
                            this::validateQuarterlyPeriod
                    );

            case YEARLY ->
                    resolveStandardPeriod(
                            periodType,
                            request.getStartDate(),
                            request.getEndDate(),
                            this::validateYearlyPeriod
                    );
        };
    }

    private ReportPeriodResponse resolveCustomPeriod(
            ReportPeriodRequest request
    ) {

        validateDates(
                request.getStartDate(),
                request.getEndDate()
        );

        return new ReportPeriodResponse(
                ReportPeriodType.CUSTOM,
                request.getStartDate(),
                request.getEndDate()
        );
    }

    private ReportPeriodResponse resolveStandardPeriod(
            ReportPeriodType periodType,
            LocalDate startDate,
            LocalDate endDate,
            PeriodValidator validator
    ) {

        if (startDate == null && endDate == null) {
            return resolveCurrentStandardPeriod(
                    periodType
            );
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Both start date and end date are required when specifying a standard report period"
            );
        }

        validateDates(
                startDate,
                endDate
        );

        validator.validate(
                startDate,
                endDate
        );

        return new ReportPeriodResponse(
                periodType,
                startDate,
                endDate
        );
    }

    private ReportPeriodResponse resolveCurrentStandardPeriod(
            ReportPeriodType periodType
    ) {

        LocalDate today = LocalDate.now();

        return switch (periodType) {

            case WEEKLY -> {

                LocalDate startDate =
                        today.with(
                                TemporalAdjusters.previousOrSame(
                                        DayOfWeek.MONDAY
                                )
                        );

                LocalDate endDate =
                        startDate.plusDays(6);

                yield new ReportPeriodResponse(
                        ReportPeriodType.WEEKLY,
                        startDate,
                        endDate
                );
            }

            case MONTHLY -> {

                LocalDate startDate =
                        today.withDayOfMonth(1);

                LocalDate endDate =
                        today.with(
                                TemporalAdjusters.lastDayOfMonth()
                        );

                yield new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        startDate,
                        endDate
                );
            }

            case QUARTERLY -> {

                int currentMonth =
                        today.getMonthValue();

                int quarterStartMonth =
                        ((currentMonth - 1) / 3) * 3 + 1;

                LocalDate startDate =
                        LocalDate.of(
                                today.getYear(),
                                quarterStartMonth,
                                1
                        );

                LocalDate endDate =
                        startDate
                                .plusMonths(3)
                                .minusDays(1);

                yield new ReportPeriodResponse(
                        ReportPeriodType.QUARTERLY,
                        startDate,
                        endDate
                );
            }

            case YEARLY -> {

                LocalDate startDate =
                        LocalDate.of(
                                today.getYear(),
                                1,
                                1
                        );

                LocalDate endDate =
                        LocalDate.of(
                                today.getYear(),
                                12,
                                31
                        );

                yield new ReportPeriodResponse(
                        ReportPeriodType.YEARLY,
                        startDate,
                        endDate
                );
            }

            case CUSTOM ->
                    throw new IllegalArgumentException(
                            "Custom report periods require start date and end date"
                    );
        };
    }

    private void validateWeeklyPeriod(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate.getDayOfWeek() != DayOfWeek.MONDAY
                || endDate.getDayOfWeek() != DayOfWeek.SUNDAY
                || !endDate.equals(startDate.plusDays(6))) {

            throw new IllegalArgumentException(
                    "Weekly report period must start on Monday " +
                            "and end on Sunday"
            );
        }
    }

    private void validateMonthlyPeriod(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (!startDate.equals(
                startDate.withDayOfMonth(1))
                || !endDate.equals(
                startDate.with(TemporalAdjusters.lastDayOfMonth()))) {

            throw new IllegalArgumentException(
                    "Monthly report period must cover a complete month"
            );
        }
    }

    private void validateQuarterlyPeriod(
            LocalDate startDate,
            LocalDate endDate
    ) {

        int month =
                startDate.getMonthValue();

        boolean validQuarterStart =
                month == 1
                        || month == 4
                        || month == 7
                        || month == 10;

        if (!validQuarterStart
                || startDate.getDayOfMonth() != 1
                || !endDate.equals(
                startDate.plusMonths(3).minusDays(1))) {

            throw new IllegalArgumentException(
                    "Quarterly report period must cover a complete quarter"
            );
        }
    }

    private void validateYearlyPeriod(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate.getMonthValue() != 1
                || startDate.getDayOfMonth() != 1
                || !endDate.equals(
                startDate.with(
                        TemporalAdjusters.lastDayOfYear()
                ))) {

            throw new IllegalArgumentException(
                    "Yearly report period must cover a complete year"
            );
        }
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date"
            );
        }
    }

    @FunctionalInterface
    private interface PeriodValidator {

        void validate(
                LocalDate startDate,
                LocalDate endDate
        );
    }
}