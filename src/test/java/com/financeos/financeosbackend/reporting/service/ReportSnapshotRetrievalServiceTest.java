package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.FinancialSummarySnapshot;
import com.financeos.financeosbackend.reporting.repository.FinancialReportRepository;
import com.financeos.financeosbackend.reporting.repository.FinancialSummarySnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportAssetAllocationRepository;
import com.financeos.financeosbackend.reporting.repository.ReportChangeContributorRepository;
import com.financeos.financeosbackend.reporting.repository.ReportChangeRepository;
import com.financeos.financeosbackend.reporting.repository.ReportComparisonSnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportExpenseCategoryRepository;
import com.financeos.financeosbackend.reporting.repository.ReportGoalSnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportIncomeSourceRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentAllocationRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentHistoricalPerformanceRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentHoldingRepository;
import com.financeos.financeosbackend.reporting.repository.ReportLiabilitySnapshotRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.financeos.financeosbackend.reporting.repository.ReportChangeContributorRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReportSnapshotRetrievalServiceTest {

    private FinancialReportRepository financialReportRepository;
    private FinancialSummarySnapshotRepository financialSummarySnapshotRepository;
    private ReportIncomeSourceRepository reportIncomeSourceRepository;
    private ReportExpenseCategoryRepository reportExpenseCategoryRepository;
    private ReportInvestmentHoldingRepository reportInvestmentHoldingRepository;
    private ReportInvestmentAllocationRepository reportInvestmentAllocationRepository;
    private ReportInvestmentHistoricalPerformanceRepository reportInvestmentHistoricalPerformanceRepository;
    private ReportGoalSnapshotRepository reportGoalSnapshotRepository;
    private ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository;
    private ReportAssetAllocationRepository reportAssetAllocationRepository;
    private ReportChangeRepository reportChangeRepository;
    private ReportComparisonSnapshotRepository reportComparisonSnapshotRepository;
    private ReportChangeContributorRepository reportChangeContributorRepository;

    private ReportSnapshotRetrievalService service;

    @BeforeEach
    void setUp() {
        financialReportRepository =
                mock(FinancialReportRepository.class);

        financialSummarySnapshotRepository =
                mock(FinancialSummarySnapshotRepository.class);

        reportIncomeSourceRepository =
                mock(ReportIncomeSourceRepository.class);

        reportExpenseCategoryRepository =
                mock(ReportExpenseCategoryRepository.class);

        reportInvestmentHoldingRepository =
                mock(ReportInvestmentHoldingRepository.class);

        reportInvestmentAllocationRepository =
                mock(ReportInvestmentAllocationRepository.class);

        reportInvestmentHistoricalPerformanceRepository =
                mock(ReportInvestmentHistoricalPerformanceRepository.class);

        reportGoalSnapshotRepository =
                mock(ReportGoalSnapshotRepository.class);

        reportLiabilitySnapshotRepository =
                mock(ReportLiabilitySnapshotRepository.class);

        reportAssetAllocationRepository =
                mock(ReportAssetAllocationRepository.class);

        reportChangeRepository =
                mock(ReportChangeRepository.class);

        reportComparisonSnapshotRepository =
                mock(ReportComparisonSnapshotRepository.class);

        reportChangeContributorRepository =
                mock(ReportChangeContributorRepository.class);

        reportChangeContributorRepository =
                mock(ReportChangeContributorRepository.class);

        service = new ReportSnapshotRetrievalService(
                financialReportRepository,
                financialSummarySnapshotRepository,
                reportIncomeSourceRepository,
                reportExpenseCategoryRepository,
                reportInvestmentHoldingRepository,
                reportGoalSnapshotRepository,
                reportLiabilitySnapshotRepository,
                reportAssetAllocationRepository,
                reportChangeRepository,
                reportInvestmentAllocationRepository,
                reportInvestmentHistoricalPerformanceRepository,
                reportComparisonSnapshotRepository,
                reportChangeContributorRepository
        );
    }

    @Test
    void shouldRejectNullUser() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getReport(null, 1L)
        );

        verifyNoInteractions(
                financialReportRepository
        );
    }

    @Test
    void shouldRejectNullReportId() {

        User user = mock(User.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getReport(user, null)
        );

        verifyNoInteractions(
                financialReportRepository
        );
    }

    @Test
    void shouldRejectMissingReport() {

        User user = mock(User.class);

        when(
                financialReportRepository.findByIdAndUser(
                        1L,
                        user
                )
        ).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.getReport(user, 1L)
                );

        assertEquals(
                "Generated report not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldRetrievePersistedReportSnapshot() {

        User user = mock(User.class);

        FinancialReport report =
                new FinancialReport();

        report.setId(1L);
        report.setUser(user);
        report.setPeriodType(
                com.financeos.financeosbackend.reporting.enums.ReportPeriodType.MONTHLY
        );
        report.setStartDate(
                LocalDate.of(2026, 8, 1)
        );
        report.setEndDate(
                LocalDate.of(2026, 8, 31)
        );
        report.setGeneratedAt(
                LocalDateTime.of(
                        2026,
                        9,
                        1,
                        10,
                        30
                )
        );
        report.setSnapshotVersion(1);

        FinancialSummarySnapshot summary =
                new FinancialSummarySnapshot();

        summary.setReport(report);
        summary.setTotalIncome(
                new BigDecimal("50000")
        );
        summary.setTotalExpense(
                new BigDecimal("20000")
        );
        summary.setInflows(
                new BigDecimal("50000")
        );
        summary.setOutflows(
                new BigDecimal("20000")
        );
        summary.setNetCashFlow(
                new BigDecimal("30000")
        );
        summary.setSavings(
                new BigDecimal("30000")
        );
        summary.setSavingsRate(
                new BigDecimal("60")
        );
        summary.setRecognizedAssets(
                new BigDecimal("500000")
        );
        summary.setLiquidAssets(
                new BigDecimal("100000")
        );
        summary.setRecognizedLiabilities(
                new BigDecimal("200000")
        );
        summary.setNetWorth(
                new BigDecimal("300000")
        );
        summary.setInvestmentValue(
                new BigDecimal("150000")
        );
        summary.setInvestmentProfitLoss(
                new BigDecimal("15000")
        );
        summary.setInvestmentReturn(
                new BigDecimal("10")
        );
        summary.setTotalGoals(3L);
        summary.setCompletedGoals(1L);
        summary.setAtRiskGoals(1L);
        summary.setOnTrackGoals(1L);
        summary.setFinancialHealthOverallStatus(
                "POSITIVE"
        );

        when(
                financialReportRepository.findByIdAndUser(
                        1L,
                        user
                )
        ).thenReturn(Optional.of(report));

        when(
                financialSummarySnapshotRepository.findByReport(
                        report
                )
        ).thenReturn(Optional.of(summary));

        when(
                reportIncomeSourceRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportExpenseCategoryRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportInvestmentHoldingRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportInvestmentAllocationRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportInvestmentHistoricalPerformanceRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportGoalSnapshotRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportLiabilitySnapshotRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportAssetAllocationRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportChangeRepository.findByReport(
                        report
                )
        ).thenReturn(List.of());

        when(
                reportComparisonSnapshotRepository.findByReport(
                        report
                )
        ).thenReturn(Optional.empty());

        FinancialReportV2Response response =
                service.getReport(
                        user,
                        1L
                );

        assertNotNull(response);

        assertEquals(
                1L,
                response.getReportId()
        );

        assertEquals(
                "GENERATED",
                response.getReportStatus()
        );

        assertEquals(
                LocalDate.of(2026, 8, 1),
                response.getReportPeriod()
                        .getStartDate()
        );

        assertEquals(
                LocalDate.of(2026, 8, 31),
                response.getReportPeriod()
                        .getEndDate()
        );

        assertEquals(
                0,
                response.getIncome()
                        .getTotalIncome()
                        .compareTo(
                                new BigDecimal("50000")
                        )
        );

        assertEquals(
                0,
                response.getExpenses()
                        .getTotalConfirmedExpenses()
                        .compareTo(
                                new BigDecimal("20000")
                        )
        );

        assertEquals(
                0,
                response.getCashFlow()
                        .getNetCashFlow()
                        .compareTo(
                                new BigDecimal("30000")
                        )
        );

        assertEquals(
                0,
                response.getNetWorth()
                        .getNetWorth()
                        .compareTo(
                                new BigDecimal("300000")
                        )
        );

        assertEquals(
                0,
                response.getInvestments()
                        .getPortfolioValue()
                        .compareTo(
                                new BigDecimal("150000")
                        )
        );

        assertEquals(
                "POSITIVE",
                response.getFinancialHealth()
                        .getOverallStatus()
        );

        verify(
                financialReportRepository
        ).findByIdAndUser(
                1L,
                user
        );

        verify(
                financialSummarySnapshotRepository
        ).findByReport(report);

        verify(
                reportIncomeSourceRepository
        ).findByReport(report);

        verify(
                reportExpenseCategoryRepository
        ).findByReport(report);

        verify(
                reportInvestmentHoldingRepository
        ).findByReport(report);

        verify(
                reportInvestmentAllocationRepository
        ).findByReport(report);

        verify(
                reportInvestmentHistoricalPerformanceRepository
        ).findByReport(report);

        verify(
                reportGoalSnapshotRepository
        ).findByReport(report);

        verify(
                reportLiabilitySnapshotRepository
        ).findByReport(report);

        verify(
                reportAssetAllocationRepository
        ).findByReport(report);

        verify(
                reportChangeRepository
        ).findByReport(report);

        verify(
                reportComparisonSnapshotRepository
        ).findByReport(report);

        verifyNoMoreInteractions(
                financialReportRepository,
                financialSummarySnapshotRepository,
                reportIncomeSourceRepository,
                reportExpenseCategoryRepository,
                reportInvestmentHoldingRepository,
                reportInvestmentAllocationRepository,
                reportInvestmentHistoricalPerformanceRepository,
                reportGoalSnapshotRepository,
                reportLiabilitySnapshotRepository,
                reportAssetAllocationRepository,
                reportChangeRepository,
                reportComparisonSnapshotRepository,
                reportChangeContributorRepository
        );
    }

    @Test
    void shouldRejectAccessToReportOwnedByAnotherUser() {

        User authenticatedUser = mock(User.class);

        when(
                financialReportRepository.findByIdAndUser(
                        1L,
                        authenticatedUser
                )
        ).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.getReport(
                                authenticatedUser,
                                1L
                        )
                );

        assertEquals(
                "Generated report not found",
                exception.getMessage()
        );

        verify(
                financialReportRepository
        ).findByIdAndUser(
                1L,
                authenticatedUser
        );

        verifyNoInteractions(
                financialSummarySnapshotRepository,
                reportIncomeSourceRepository,
                reportExpenseCategoryRepository,
                reportInvestmentHoldingRepository,
                reportInvestmentAllocationRepository,
                reportInvestmentHistoricalPerformanceRepository,
                reportGoalSnapshotRepository,
                reportLiabilitySnapshotRepository,
                reportAssetAllocationRepository,
                reportChangeRepository,
                reportComparisonSnapshotRepository,
                reportChangeContributorRepository
        );
    }
}