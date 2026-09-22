package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodRequest;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportPeriodServiceTest {

    private final ReportPeriodService service =
            new ReportPeriodService();

    @Test
    void shouldResolveValidWeeklyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.WEEKLY);
        request.setStartDate(LocalDate.of(2026, 8, 3));
        request.setEndDate(LocalDate.of(2026, 8, 9));

        ReportPeriodResponse response =
                service.resolvePeriod(request);

        assertEquals(
                ReportPeriodType.WEEKLY,
                response.getPeriodType()
        );

        assertEquals(
                LocalDate.of(2026, 8, 3),
                response.getStartDate()
        );

        assertEquals(
                LocalDate.of(2026, 8, 9),
                response.getEndDate()
        );
    }

    @Test
    void shouldResolveValidMonthlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.MONTHLY);
        request.setStartDate(LocalDate.of(2026, 8, 1));
        request.setEndDate(LocalDate.of(2026, 8, 31));

        ReportPeriodResponse response =
                service.resolvePeriod(request);

        assertEquals(
                ReportPeriodType.MONTHLY,
                response.getPeriodType()
        );
    }

    @Test
    void shouldResolveValidQuarterlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.QUARTERLY);
        request.setStartDate(LocalDate.of(2026, 7, 1));
        request.setEndDate(LocalDate.of(2026, 9, 30));

        ReportPeriodResponse response =
                service.resolvePeriod(request);

        assertEquals(
                ReportPeriodType.QUARTERLY,
                response.getPeriodType()
        );
    }

    @Test
    void shouldResolveValidYearlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.YEARLY);
        request.setStartDate(LocalDate.of(2026, 1, 1));
        request.setEndDate(LocalDate.of(2026, 12, 31));

        ReportPeriodResponse response =
                service.resolvePeriod(request);

        assertEquals(
                ReportPeriodType.YEARLY,
                response.getPeriodType()
        );
    }

    @Test
    void shouldResolveValidCustomPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.CUSTOM);
        request.setStartDate(LocalDate.of(2026, 8, 5));
        request.setEndDate(LocalDate.of(2026, 8, 20));

        ReportPeriodResponse response =
                service.resolvePeriod(request);

        assertEquals(
                ReportPeriodType.CUSTOM,
                response.getPeriodType()
        );
    }

    @Test
    void shouldRejectInvalidWeeklyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.WEEKLY);
        request.setStartDate(LocalDate.of(2026, 8, 4));
        request.setEndDate(LocalDate.of(2026, 8, 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectInvalidMonthlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.MONTHLY);
        request.setStartDate(LocalDate.of(2026, 8, 2));
        request.setEndDate(LocalDate.of(2026, 8, 31));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectInvalidQuarterlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.QUARTERLY);
        request.setStartDate(LocalDate.of(2026, 8, 1));
        request.setEndDate(LocalDate.of(2026, 10, 31));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectInvalidYearlyPeriod() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.YEARLY);
        request.setStartDate(LocalDate.of(2026, 2, 1));
        request.setEndDate(LocalDate.of(2027, 1, 31));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.CUSTOM);
        request.setStartDate(LocalDate.of(2026, 8, 20));
        request.setEndDate(LocalDate.of(2026, 8, 5));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectMissingDates() {

        ReportPeriodRequest request = new ReportPeriodRequest();

        request.setPeriodType(ReportPeriodType.CUSTOM);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(request)
        );
    }

    @Test
    void shouldRejectNullRequest() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.resolvePeriod(null)
        );
    }
}