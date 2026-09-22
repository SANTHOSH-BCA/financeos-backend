package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

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
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.insight.ReportChangeInsightService;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportingV2Service {

    private final CurrentUserService currentUserService;

    private final DefaultReportDataCollector reportDataCollector;
    private final InvestmentReportDataCollector investmentReportDataCollector;
    private final GoalReportDataCollector goalReportDataCollector;
    private final AssetLiabilityReportDataCollector assetLiabilityReportDataCollector;
    private final NetWorthHealthReportDataCollector netWorthHealthReportDataCollector;

    private final ReportComparisonDataCollector reportComparisonDataCollector;
    private final ReportChangeInsightService reportChangeInsightService;

    private final ReportSectionAssembler reportSectionAssembler;
    private final InvestmentReportSectionAssembler investmentReportSectionAssembler;
    private final GoalReportSectionAssembler goalReportSectionAssembler;
    private final AssetLiabilityReportSectionAssembler assetLiabilityReportSectionAssembler;
    private final NetWorthHealthReportSectionAssembler netWorthHealthReportSectionAssembler;
    private final ReportComparisonSectionAssembler reportComparisonSectionAssembler;

    private final FinancialReportV2Assembler financialReportV2Assembler;
    private final ReportSnapshotPersistenceService reportSnapshotPersistenceService;

    public ReportingV2Service(
            CurrentUserService currentUserService,
            DefaultReportDataCollector reportDataCollector,
            InvestmentReportDataCollector investmentReportDataCollector,
            GoalReportDataCollector goalReportDataCollector,
            AssetLiabilityReportDataCollector assetLiabilityReportDataCollector,
            NetWorthHealthReportDataCollector netWorthHealthReportDataCollector,
            ReportComparisonDataCollector reportComparisonDataCollector,
            ReportChangeInsightService reportChangeInsightService,
            ReportSectionAssembler reportSectionAssembler,
            InvestmentReportSectionAssembler investmentReportSectionAssembler,
            GoalReportSectionAssembler goalReportSectionAssembler,
            AssetLiabilityReportSectionAssembler assetLiabilityReportSectionAssembler,
            NetWorthHealthReportSectionAssembler netWorthHealthReportSectionAssembler,
            ReportComparisonSectionAssembler reportComparisonSectionAssembler,
            FinancialReportV2Assembler financialReportV2Assembler,
            ReportSnapshotPersistenceService reportSnapshotPersistenceService
    ) {
        this.currentUserService = currentUserService;
        this.reportDataCollector = reportDataCollector;
        this.investmentReportDataCollector =
                investmentReportDataCollector;
        this.goalReportDataCollector =
                goalReportDataCollector;
        this.assetLiabilityReportDataCollector =
                assetLiabilityReportDataCollector;
        this.netWorthHealthReportDataCollector =
                netWorthHealthReportDataCollector;
        this.reportComparisonDataCollector =
                reportComparisonDataCollector;
        this.reportChangeInsightService =
                reportChangeInsightService;
        this.reportSectionAssembler =
                reportSectionAssembler;
        this.investmentReportSectionAssembler =
                investmentReportSectionAssembler;
        this.goalReportSectionAssembler =
                goalReportSectionAssembler;
        this.assetLiabilityReportSectionAssembler =
                assetLiabilityReportSectionAssembler;
        this.netWorthHealthReportSectionAssembler =
                netWorthHealthReportSectionAssembler;
        this.reportComparisonSectionAssembler =
                reportComparisonSectionAssembler;
        this.financialReportV2Assembler =
                financialReportV2Assembler;
        this.reportSnapshotPersistenceService =
                reportSnapshotPersistenceService;
    }

    public FinancialReportV2Response generateReport(
            ReportPeriodResponse period
    ) {
        if (period == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null."
            );
        }

        User user = currentUserService.getCurrentUser();

        ReportDataContext reportData =
                reportDataCollector.collect(
                        user,
                        period
                );

        IncomeReportV2Response income =
                reportSectionAssembler.assembleIncome(
                        reportData
                );

        ExpenseReportV2Response expenses =
                reportSectionAssembler.assembleExpenses(
                        reportData
                );

        CashFlowReportV2Response cashFlow =
                reportSectionAssembler.assembleCashFlow(
                        reportData
                );

        ReportInvestmentData investmentData =
                investmentReportDataCollector.collect(
                        user,
                        period
                );

        InvestmentReportV2Response investments =
                investmentReportSectionAssembler.assemble(
                        investmentData
                );

        ReportGoalData goalData =
                goalReportDataCollector.collect(
                        user,
                        period
                );

        GoalReportV2Response goals =
                goalReportSectionAssembler.assemble(
                        goalData
                );

        ReportAssetData assetData =
                assetLiabilityReportDataCollector.collectAssets();

        ReportLiabilityData liabilityData =
                assetLiabilityReportDataCollector.collectLiabilities(
                        user,
                        period
                );

        AssetReportV2Response assets =
                assetLiabilityReportSectionAssembler.assembleAssets(
                        assetData
                );

        LiabilityReportV2Response liabilities =
                assetLiabilityReportSectionAssembler.assembleLiabilities(
                        liabilityData
                );

        ReportNetWorthData netWorthData =
                netWorthHealthReportDataCollector.collectNetWorth(
                        user,
                        period
                );

        ReportFinancialHealthData financialHealthData =
                netWorthHealthReportDataCollector.collectFinancialHealth(
                        user,
                        period
                );

        NetWorthReportV2Response netWorth =
                netWorthHealthReportSectionAssembler.assembleNetWorth(
                        netWorthData
                );

        FinancialHealthReportV2Response financialHealth =
                netWorthHealthReportSectionAssembler
                        .assembleFinancialHealth(
                                financialHealthData
                        );

        ReportComparisonData comparisonData =
                reportComparisonDataCollector.collect(
                        user,
                        period
                );

        ReportComparisonV2Response comparison =
                reportComparisonSectionAssembler.assemble(
                        comparisonData
                );

        List<ReportChangeInsightV2Response> changes =
                generateChangeInsights(
                        user,
                        comparisonData,
                        comparison
                );

        FinancialReportV2Response report =
                financialReportV2Assembler.assemble(
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

        return reportSnapshotPersistenceService.persist(
                user,
                report
        );
    }

    private List<ReportChangeInsightV2Response> generateChangeInsights(
            User user,
            ReportComparisonData comparisonData,
            ReportComparisonV2Response comparison
    ) {
        if (comparisonData == null
                || comparison == null
                || !comparison.isComparisonAvailable()
                || comparison.getCurrentPeriod() == null
                || comparison.getPreviousPeriod() == null) {

            return List.of();
        }

        BigDecimal currentIncome =
                comparisonData.getIncome() != null
                        ? comparisonData.getIncome().getCurrentValue()
                        : BigDecimal.ZERO;

        BigDecimal previousIncome =
                comparisonData.getIncome() != null
                        ? comparisonData.getIncome().getPreviousValue()
                        : BigDecimal.ZERO;

        BigDecimal currentExpenses =
                comparisonData.getExpenses() != null
                        ? comparisonData.getExpenses().getCurrentValue()
                        : BigDecimal.ZERO;

        BigDecimal previousExpenses =
                comparisonData.getExpenses() != null
                        ? comparisonData.getExpenses().getPreviousValue()
                        : BigDecimal.ZERO;

        BigDecimal currentSavings =
                comparisonData.getSavings() != null
                        ? comparisonData.getSavings().getCurrentValue()
                        : BigDecimal.ZERO;

        BigDecimal previousSavings =
                comparisonData.getSavings() != null
                        ? comparisonData.getSavings().getPreviousValue()
                        : BigDecimal.ZERO;

        return reportChangeInsightService.generateInsights(
                user,
                comparison.getCurrentPeriod(),
                comparison.getPreviousPeriod(),
                currentIncome,
                previousIncome,
                currentExpenses,
                previousExpenses,
                currentSavings,
                previousSavings
        );
    }
}
