package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.repository.FinancialReportRepository;
import com.financeos.financeosbackend.reporting.repository.FinancialSummarySnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportAssetAllocationRepository;
import com.financeos.financeosbackend.reporting.repository.ReportChangeRepository;
import com.financeos.financeosbackend.reporting.repository.ReportExpenseCategoryRepository;
import com.financeos.financeosbackend.reporting.repository.ReportGoalSnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportIncomeSourceRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentHoldingRepository;
import com.financeos.financeosbackend.reporting.repository.ReportLiabilitySnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentAllocationRepository;
import com.financeos.financeosbackend.reporting.repository.ReportInvestmentHistoricalPerformanceRepository;
import com.financeos.financeosbackend.reporting.repository.ReportComparisonSnapshotRepository;
import com.financeos.financeosbackend.reporting.repository.ReportChangeContributorRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportSnapshotPersistenceServiceTest {

    @Mock
    private FinancialReportRepository financialReportRepository;

    @Mock
    private FinancialSummarySnapshotRepository financialSummarySnapshotRepository;

    @Mock
    private ReportIncomeSourceRepository reportIncomeSourceRepository;

    @Mock
    private ReportExpenseCategoryRepository reportExpenseCategoryRepository;

    @Mock
    private ReportInvestmentHoldingRepository reportInvestmentHoldingRepository;

    @Mock
    private ReportGoalSnapshotRepository reportGoalSnapshotRepository;

    @Mock
    private ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository;

    @Mock
    private ReportAssetAllocationRepository reportAssetAllocationRepository;

    @Mock
    private ReportChangeRepository reportChangeRepository;

    @Mock
    private ReportInvestmentAllocationRepository reportInvestmentAllocationRepository;

    @Mock
    private ReportInvestmentHistoricalPerformanceRepository reportInvestmentHistoricalPerformanceRepository;

    @Mock
    private ReportComparisonSnapshotRepository reportComparisonSnapshotRepository;

    @Mock
    private ReportChangeContributorRepository reportChangeContributorRepository;

    @InjectMocks
    private ReportSnapshotPersistenceService persistenceService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void shouldPersistGeneratedReport() {

        FinancialReportV2Response response =
                createReportResponse();

        FinancialReport savedReport =
                new FinancialReport();

        savedReport.setId(100L);

        when(financialReportRepository.save(any(FinancialReport.class)))
                .thenReturn(savedReport);

        FinancialReportV2Response result =
                persistenceService.persist(user, response);

        assertNotNull(result);
        assertEquals(100L, result.getReportId());
        assertEquals("GENERATED", result.getReportStatus());

        verify(financialReportRepository, times(1))
                .save(any(FinancialReport.class));

        verify(financialSummarySnapshotRepository, times(1))
                .save(any());
    }

    @Test
    void shouldPersistAuthenticatedUserOnReport() {

        FinancialReportV2Response response =
                createReportResponse();

        FinancialReport savedReport =
                new FinancialReport();

        savedReport.setId(102L);

        when(financialReportRepository.save(any(FinancialReport.class)))
                .thenAnswer(invocation -> {

                    FinancialReport report =
                            invocation.getArgument(0);

                    report.setId(102L);

                    return report;
                });

        persistenceService.persist(user, response);

        ArgumentCaptor<FinancialReport> reportCaptor =
                ArgumentCaptor.forClass(FinancialReport.class);

        verify(financialReportRepository)
                .save(reportCaptor.capture());

        FinancialReport persistedReport =
                reportCaptor.getValue();

        assertEquals(user, persistedReport.getUser());
    }

    @Test
    void shouldPersistReportPeriod() {

        FinancialReportV2Response response =
                createReportResponse();

        when(financialReportRepository.save(any(FinancialReport.class)))
                .thenAnswer(invocation -> {

                    FinancialReport report =
                            invocation.getArgument(0);

                    report.setId(101L);

                    return report;
                });

        persistenceService.persist(user, response);

        verify(financialReportRepository)
                .save(any(FinancialReport.class));
    }

    @Test
    void shouldRejectNullUser() {

        FinancialReportV2Response response =
                createReportResponse();

        assertThrows(
                IllegalArgumentException.class,
                () -> persistenceService.persist(null, response)
        );
    }

    @Test
    void shouldRejectNullResponse() {

        assertThrows(
                IllegalArgumentException.class,
                () -> persistenceService.persist(user, null)
        );
    }

    @Test
    void shouldRejectResponseWithoutPeriod() {

        FinancialReportV2Response response =
                new FinancialReportV2Response();

        assertThrows(
                IllegalArgumentException.class,
                () -> persistenceService.persist(user, response)
        );
    }

    private FinancialReportV2Response createReportResponse() {

        FinancialReportV2Response response =
                new FinancialReportV2Response();

        ReportPeriodResponse period =
                new ReportPeriodResponse();

        period.setPeriodType(ReportPeriodType.MONTHLY);

        period.setStartDate(
                LocalDate.of(2026, 8, 1)
        );

        period.setEndDate(
                LocalDate.of(2026, 8, 31)
        );

        response.setReportPeriod(period);
        response.setGeneratedAt(LocalDateTime.now());

        IncomeReportV2Response income =
                new IncomeReportV2Response();

        income.setTotalIncome(
                new BigDecimal("50000.00")
        );

        response.setIncome(income);

        ExpenseReportV2Response expenses =
                new ExpenseReportV2Response();

        expenses.setTotalConfirmedExpenses(
                new BigDecimal("20000.00")
        );

        response.setExpenses(expenses);

        CashFlowReportV2Response cashFlow =
                new CashFlowReportV2Response();

        cashFlow.setInflows(
                new BigDecimal("50000.00")
        );

        cashFlow.setOutflows(
                new BigDecimal("20000.00")
        );

        cashFlow.setNetCashFlow(
                new BigDecimal("30000.00")
        );

        cashFlow.setSavings(
                new BigDecimal("30000.00")
        );

        response.setCashFlow(cashFlow);

        InvestmentReportV2Response investments =
                new InvestmentReportV2Response();

        investments.setInvestedAmount(
                new BigDecimal("100000.00")
        );

        investments.setPortfolioValue(
                new BigDecimal("110000.00")
        );

        investments.setProfitLoss(
                new BigDecimal("10000.00")
        );

        investments.setReturnPercentage(
                new BigDecimal("10.00")
        );

        response.setInvestments(investments);

        GoalReportV2Response goals =
                new GoalReportV2Response();

        goals.setTotalGoals(2);
        goals.setCompletedGoals(1);
        goals.setOnTrackGoals(1);
        goals.setAtRiskGoals(0);

        response.setGoals(goals);

        AssetReportV2Response assets =
                new AssetReportV2Response();

        assets.setRecognizedAssets(
                new BigDecimal("200000.00")
        );

        assets.setLiquidAssets(
                new BigDecimal("75000.00")
        );

        response.setAssets(assets);

        LiabilityReportV2Response liabilities =
                new LiabilityReportV2Response();

        liabilities.setRecognizedLiabilities(
                new BigDecimal("50000.00")
        );

        response.setLiabilities(liabilities);

        NetWorthReportV2Response netWorth =
                new NetWorthReportV2Response();

        netWorth.setRecognizedAssets(
                new BigDecimal("200000.00")
        );

        netWorth.setRecognizedLiabilities(
                new BigDecimal("50000.00")
        );

        netWorth.setNetWorth(
                new BigDecimal("150000.00")
        );

        response.setNetWorth(netWorth);

        FinancialHealthReportV2Response health =
                new FinancialHealthReportV2Response();

        health.setOverallStatus("POSITIVE");

        response.setFinancialHealth(health);

        return response;
    }
}