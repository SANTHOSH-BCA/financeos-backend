package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.reporting.assembler.FinancialReportV2Assembler;
import com.financeos.financeosbackend.reporting.assembler.ReportSectionAssembler;
import com.financeos.financeosbackend.reporting.assembler.comparison.ReportComparisonSectionAssembler;
import com.financeos.financeosbackend.reporting.assembler.goal.GoalReportSectionAssembler;
import com.financeos.financeosbackend.reporting.assembler.health.NetWorthHealthReportSectionAssembler;
import com.financeos.financeosbackend.reporting.assembler.investment.InvestmentReportSectionAssembler;
import com.financeos.financeosbackend.reporting.assembler.position.AssetLiabilityReportSectionAssembler;
import com.financeos.financeosbackend.reporting.collector.DefaultReportDataCollector;
import com.financeos.financeosbackend.reporting.collector.ReportDataContext;
import com.financeos.financeosbackend.reporting.collector.goal.GoalReportDataCollector;
import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalData;
import com.financeos.financeosbackend.reporting.collector.health.NetWorthHealthReportDataCollector;
import com.financeos.financeosbackend.reporting.collector.health.ReportFinancialHealthData;
import com.financeos.financeosbackend.reporting.collector.health.ReportNetWorthData;
import com.financeos.financeosbackend.reporting.collector.investment.InvestmentReportDataCollector;
import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentData;
import com.financeos.financeosbackend.reporting.collector.position.AssetLiabilityReportDataCollector;
import com.financeos.financeosbackend.reporting.collector.position.ReportAssetData;
import com.financeos.financeosbackend.reporting.collector.position.ReportLiabilityData;
import com.financeos.financeosbackend.reporting.comparison.ReportComparisonData;
import com.financeos.financeosbackend.reporting.comparison.ReportComparisonDataCollector;
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
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.insight.ReportChangeInsightService;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReportingV2ServiceTest {

    private CurrentUserService currentUserService;

    private DefaultReportDataCollector reportDataCollector;
    private InvestmentReportDataCollector investmentReportDataCollector;
    private GoalReportDataCollector goalReportDataCollector;
    private AssetLiabilityReportDataCollector assetLiabilityReportDataCollector;
    private NetWorthHealthReportDataCollector netWorthHealthReportDataCollector;

    private ReportComparisonDataCollector reportComparisonDataCollector;
    private ReportChangeInsightService reportChangeInsightService;

    private ReportSectionAssembler reportSectionAssembler;
    private InvestmentReportSectionAssembler investmentReportSectionAssembler;
    private GoalReportSectionAssembler goalReportSectionAssembler;
    private AssetLiabilityReportSectionAssembler assetLiabilityReportSectionAssembler;
    private NetWorthHealthReportSectionAssembler netWorthHealthReportSectionAssembler;
    private ReportComparisonSectionAssembler reportComparisonSectionAssembler;

    private FinancialReportV2Assembler financialReportV2Assembler;
    private ReportSnapshotPersistenceService reportSnapshotPersistenceService;

    private ReportingV2Service reportingV2Service;

    @BeforeEach
    void setUp() {
        currentUserService = mock(CurrentUserService.class);

        reportDataCollector = mock(DefaultReportDataCollector.class);
        investmentReportDataCollector =
                mock(InvestmentReportDataCollector.class);
        goalReportDataCollector =
                mock(GoalReportDataCollector.class);
        assetLiabilityReportDataCollector =
                mock(AssetLiabilityReportDataCollector.class);
        netWorthHealthReportDataCollector =
                mock(NetWorthHealthReportDataCollector.class);

        reportComparisonDataCollector =
                mock(ReportComparisonDataCollector.class);
        reportChangeInsightService =
                mock(ReportChangeInsightService.class);

        reportSectionAssembler =
                mock(ReportSectionAssembler.class);
        investmentReportSectionAssembler =
                mock(InvestmentReportSectionAssembler.class);
        goalReportSectionAssembler =
                mock(GoalReportSectionAssembler.class);
        assetLiabilityReportSectionAssembler =
                mock(AssetLiabilityReportSectionAssembler.class);
        netWorthHealthReportSectionAssembler =
                mock(NetWorthHealthReportSectionAssembler.class);
        reportComparisonSectionAssembler =
                mock(ReportComparisonSectionAssembler.class);

        financialReportV2Assembler =
                mock(FinancialReportV2Assembler.class);

        reportSnapshotPersistenceService =
                mock(ReportSnapshotPersistenceService.class);

        reportingV2Service = new ReportingV2Service(
                currentUserService,
                reportDataCollector,
                investmentReportDataCollector,
                goalReportDataCollector,
                assetLiabilityReportDataCollector,
                netWorthHealthReportDataCollector,
                reportComparisonDataCollector,
                reportChangeInsightService,
                reportSectionAssembler,
                investmentReportSectionAssembler,
                goalReportSectionAssembler,
                assetLiabilityReportSectionAssembler,
                netWorthHealthReportSectionAssembler,
                reportComparisonSectionAssembler,
                financialReportV2Assembler,
                reportSnapshotPersistenceService
        );
    }

    @Test
    void shouldRejectNullPeriod() {
        assertThrows(
                IllegalArgumentException.class,
                () -> reportingV2Service.generateReport(null)
        );

        verifyNoInteractions(
                currentUserService,
                reportDataCollector,
                financialReportV2Assembler,
                reportSnapshotPersistenceService
        );
    }

    @Test
    void shouldOrchestrateAllReportSections() {

        User user = mock(User.class);

        ReportPeriodResponse period =
                mock(ReportPeriodResponse.class);

        ReportDataContext reportData =
                mock(ReportDataContext.class);

        ReportInvestmentData investmentData =
                mock(ReportInvestmentData.class);

        ReportGoalData goalData =
                mock(ReportGoalData.class);

        ReportAssetData assetData =
                mock(ReportAssetData.class);

        ReportLiabilityData liabilityData =
                mock(ReportLiabilityData.class);

        ReportNetWorthData netWorthData =
                mock(ReportNetWorthData.class);

        ReportFinancialHealthData financialHealthData =
                mock(ReportFinancialHealthData.class);

        ReportComparisonData comparisonData =
                mock(ReportComparisonData.class);

        IncomeReportV2Response income =
                mock(IncomeReportV2Response.class);

        ExpenseReportV2Response expenses =
                mock(ExpenseReportV2Response.class);

        CashFlowReportV2Response cashFlow =
                mock(CashFlowReportV2Response.class);

        InvestmentReportV2Response investments =
                mock(InvestmentReportV2Response.class);

        GoalReportV2Response goals =
                mock(GoalReportV2Response.class);

        AssetReportV2Response assets =
                mock(AssetReportV2Response.class);

        LiabilityReportV2Response liabilities =
                mock(LiabilityReportV2Response.class);

        NetWorthReportV2Response netWorth =
                mock(NetWorthReportV2Response.class);

        FinancialHealthReportV2Response financialHealth =
                mock(FinancialHealthReportV2Response.class);

        ReportComparisonV2Response comparison =
                mock(ReportComparisonV2Response.class);

        FinancialReportV2Response finalResponse =
                mock(FinancialReportV2Response.class);

        List<ReportChangeInsightV2Response> changes =
                List.of();

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(reportDataCollector.collect(user, period))
                .thenReturn(reportData);

        when(reportSectionAssembler.assembleIncome(reportData))
                .thenReturn(income);

        when(reportSectionAssembler.assembleExpenses(reportData))
                .thenReturn(expenses);

        when(reportSectionAssembler.assembleCashFlow(reportData))
                .thenReturn(cashFlow);

        when(investmentReportDataCollector.collect(user, period))
                .thenReturn(investmentData);

        when(investmentReportSectionAssembler.assemble(investmentData))
                .thenReturn(investments);

        when(goalReportDataCollector.collect(user, period))
                .thenReturn(goalData);

        when(goalReportSectionAssembler.assemble(goalData))
                .thenReturn(goals);

        when(assetLiabilityReportDataCollector.collectAssets())
                .thenReturn(assetData);

        when(assetLiabilityReportDataCollector.collectLiabilities(
                user,
                period
        )).thenReturn(liabilityData);

        when(assetLiabilityReportSectionAssembler.assembleAssets(assetData))
                .thenReturn(assets);

        when(assetLiabilityReportSectionAssembler.assembleLiabilities(
                liabilityData
        )).thenReturn(liabilities);

        when(netWorthHealthReportDataCollector.collectNetWorth(
                user,
                period
        )).thenReturn(netWorthData);

        when(netWorthHealthReportDataCollector.collectFinancialHealth(
                user,
                period
        )).thenReturn(financialHealthData);

        when(netWorthHealthReportSectionAssembler.assembleNetWorth(
                netWorthData
        )).thenReturn(netWorth);

        when(netWorthHealthReportSectionAssembler.assembleFinancialHealth(
                financialHealthData
        )).thenReturn(financialHealth);

        when(reportComparisonDataCollector.collect(
                user,
                period
        )).thenReturn(comparisonData);

        when(reportComparisonSectionAssembler.assemble(
                comparisonData
        )).thenReturn(comparison);

        when(comparison.isComparisonAvailable())
                .thenReturn(false);

        when(financialReportV2Assembler.assemble(
                period,
                income,
                expenses,
                cashFlow,
                investments,
                goals,
                assets,
                liabilities,
                netWorth,
                financialHealth,
                comparison,
                changes
        )).thenReturn(finalResponse);

        when(reportSnapshotPersistenceService.persist(
                user,
                finalResponse
        )).thenReturn(finalResponse);

        FinancialReportV2Response result =
                reportingV2Service.generateReport(period);

        assertSame(finalResponse, result);

        verify(currentUserService)
                .getCurrentUser();

        verify(reportDataCollector)
                .collect(user, period);

        verify(reportSectionAssembler)
                .assembleIncome(reportData);

        verify(reportSectionAssembler)
                .assembleExpenses(reportData);

        verify(reportSectionAssembler)
                .assembleCashFlow(reportData);

        verify(investmentReportDataCollector)
                .collect(user, period);

        verify(investmentReportSectionAssembler)
                .assemble(investmentData);

        verify(goalReportDataCollector)
                .collect(user, period);

        verify(goalReportSectionAssembler)
                .assemble(goalData);

        verify(assetLiabilityReportDataCollector)
                .collectAssets();

        verify(assetLiabilityReportDataCollector)
                .collectLiabilities(
                        user,
                        period
                );

        verify(assetLiabilityReportSectionAssembler)
                .assembleAssets(assetData);

        verify(assetLiabilityReportSectionAssembler)
                .assembleLiabilities(
                        liabilityData
                );

        verify(netWorthHealthReportDataCollector)
                .collectNetWorth(
                        user,
                        period
                );

        verify(netWorthHealthReportDataCollector)
                .collectFinancialHealth(
                        user,
                        period
                );

        verify(netWorthHealthReportSectionAssembler)
                .assembleNetWorth(
                        netWorthData
                );

        verify(netWorthHealthReportSectionAssembler)
                .assembleFinancialHealth(
                        financialHealthData
                );

        verify(reportComparisonDataCollector)
                .collect(
                        user,
                        period
                );

        verify(reportComparisonSectionAssembler)
                .assemble(
                        comparisonData
                );

        verify(financialReportV2Assembler)
                .assemble(
                        period,
                        income,
                        expenses,
                        cashFlow,
                        investments,
                        goals,
                        assets,
                        liabilities,
                        netWorth,
                        financialHealth,
                        comparison,
                        changes
                );

        verify(reportSnapshotPersistenceService)
                .persist(
                        user,
                        finalResponse
                );

        verifyNoInteractions(
                reportChangeInsightService
        );
    }
}