package com.financeos.financeosbackend.reporting.controller;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodRequest;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.reporting.service.ReportPeriodService;
import com.financeos.financeosbackend.reporting.service.ReportSnapshotRetrievalService;
import com.financeos.financeosbackend.reporting.service.ReportingV2Service;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ReportingV2ControllerTest {

    private CurrentUserService currentUserService;
    private ReportPeriodService reportPeriodService;
    private ReportingV2Service reportingV2Service;
    private ReportSnapshotRetrievalService reportSnapshotRetrievalService;

    private ReportingV2Controller controller;

    @BeforeEach
    void setUp() {

        currentUserService =
                mock(CurrentUserService.class);

        reportPeriodService =
                mock(ReportPeriodService.class);

        reportingV2Service =
                mock(ReportingV2Service.class);

        reportSnapshotRetrievalService =
                mock(ReportSnapshotRetrievalService.class);

        controller =
                new ReportingV2Controller(
                        currentUserService,
                        reportPeriodService,
                        reportingV2Service,
                        reportSnapshotRetrievalService
                );
    }

    @Test
    void shouldGenerateReport() {

        ReportPeriodRequest request =
                new ReportPeriodRequest();

        request.setPeriodType(
                ReportPeriodType.MONTHLY
        );

        request.setStartDate(
                LocalDate.of(2026, 8, 1)
        );

        request.setEndDate(
                LocalDate.of(2026, 8, 31)
        );

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        FinancialReportV2Response report =
                new FinancialReportV2Response();

        when(
                reportPeriodService.resolvePeriod(request)
        ).thenReturn(period);

        when(
                reportingV2Service.generateReport(period)
        ).thenReturn(report);

        var result =
                controller.generateReport(request);

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertTrue(
                result.getBody().isSuccess()
        );

        assertEquals(
                HttpStatus.OK.value(),
                result.getBody().getStatus()
        );

        assertEquals(
                "Financial report generated successfully",
                result.getBody().getMessage()
        );

        assertSame(
                report,
                result.getBody().getData()
        );

        verify(
                reportPeriodService,
                times(1)
        ).resolvePeriod(request);

        verify(
                reportingV2Service,
                times(1)
        ).generateReport(period);

        verifyNoInteractions(
                currentUserService,
                reportSnapshotRetrievalService
        );
    }

    @Test
    void shouldGenerateCustomPeriodReport() {

        ReportPeriodRequest request =
                new ReportPeriodRequest();

        request.setPeriodType(
                ReportPeriodType.CUSTOM
        );

        request.setStartDate(
                LocalDate.of(2026, 8, 10)
        );

        request.setEndDate(
                LocalDate.of(2026, 8, 25)
        );

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.CUSTOM,
                        LocalDate.of(2026, 8, 10),
                        LocalDate.of(2026, 8, 25)
                );

        FinancialReportV2Response report =
                new FinancialReportV2Response();

        when(
                reportPeriodService.resolvePeriod(request)
        ).thenReturn(period);

        when(
                reportingV2Service.generateReport(period)
        ).thenReturn(report);

        var result =
                controller.generateReport(request);

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                report,
                result.getBody().getData()
        );

        verify(
                reportPeriodService
        ).resolvePeriod(request);

        verify(
                reportingV2Service
        ).generateReport(period);
    }

    @Test
    void shouldPropagatePeriodResolutionException() {

        ReportPeriodRequest request =
                new ReportPeriodRequest();

        request.setPeriodType(
                ReportPeriodType.CUSTOM
        );

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Start date and end date are required"
                );

        when(
                reportPeriodService.resolvePeriod(request)
        ).thenThrow(exception);

        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.generateReport(request)
                );

        assertSame(
                exception,
                thrown
        );

        verify(
                reportPeriodService
        ).resolvePeriod(request);

        verifyNoInteractions(
                reportingV2Service
        );
    }

    @Test
    void shouldRetrieveGeneratedReport() {

        User user =
                mock(User.class);

        FinancialReportV2Response report =
                new FinancialReportV2Response();

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                reportSnapshotRetrievalService.getReport(
                        user,
                        1L
                )
        ).thenReturn(report);

        var result =
                controller.getReport(1L);

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertTrue(
                result.getBody().isSuccess()
        );

        assertEquals(
                HttpStatus.OK.value(),
                result.getBody().getStatus()
        );

        assertEquals(
                "Financial report retrieved successfully",
                result.getBody().getMessage()
        );

        assertSame(
                report,
                result.getBody().getData()
        );

        verify(
                currentUserService,
                times(1)
        ).getCurrentUser();

        verify(
                reportSnapshotRetrievalService,
                times(1)
        ).getReport(
                user,
                1L
        );

        verifyNoInteractions(
                reportPeriodService,
                reportingV2Service
        );
    }

    @Test
    void shouldPropagateMissingReportException() {

        User user =
                mock(User.class);

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Generated report not found"
                );

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                reportSnapshotRetrievalService.getReport(
                        user,
                        999L
                )
        ).thenThrow(exception);

        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.getReport(999L)
                );

        assertSame(
                exception,
                thrown
        );

        verify(
                currentUserService
        ).getCurrentUser();

        verify(
                reportSnapshotRetrievalService
        ).getReport(
                user,
                999L
        );
    }

    @Test
    void shouldPropagateNullUserException() {

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(null);

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "User must not be null"
                );

        when(
                reportSnapshotRetrievalService.getReport(
                        null,
                        1L
                )
        ).thenThrow(exception);

        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.getReport(1L)
                );

        assertSame(
                exception,
                thrown
        );

        verify(
                currentUserService
        ).getCurrentUser();

        verify(
                reportSnapshotRetrievalService
        ).getReport(
                null,
                1L
        );
    }

    @Test
    void shouldPropagateNullReportIdException() {

        User user =
                mock(User.class);

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Report ID must not be null"
                );

        when(
                reportSnapshotRetrievalService.getReport(
                        user,
                        null
                )
        ).thenThrow(exception);

        IllegalArgumentException thrown =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> controller.getReport(null)
                );

        assertSame(
                exception,
                thrown
        );

        verify(
                currentUserService
        ).getCurrentUser();

        verify(
                reportSnapshotRetrievalService
        ).getReport(
                user,
                null
        );
    }
}