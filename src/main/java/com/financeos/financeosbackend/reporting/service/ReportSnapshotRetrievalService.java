package com.financeos.financeosbackend.reporting.service;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.AssetAllocationV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentAllocationV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentHistoricalPerformanceV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeContributorV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportInvestmentHoldingV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportLiabilityItemV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportMetricChangeV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.FinancialSummarySnapshot;
import com.financeos.financeosbackend.reporting.entity.ReportAssetAllocation;
import com.financeos.financeosbackend.reporting.entity.ReportChange;
import com.financeos.financeosbackend.reporting.entity.ReportChangeContributor;
import com.financeos.financeosbackend.reporting.entity.ReportComparisonSnapshot;
import com.financeos.financeosbackend.reporting.entity.ReportExpenseCategory;
import com.financeos.financeosbackend.reporting.entity.ReportGoalSnapshot;
import com.financeos.financeosbackend.reporting.entity.ReportIncomeSource;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentAllocation;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentHistoricalPerformance;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentHolding;
import com.financeos.financeosbackend.reporting.entity.ReportLiabilitySnapshot;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportSnapshotRetrievalService {

    private final FinancialReportRepository financialReportRepository;
    private final FinancialSummarySnapshotRepository financialSummarySnapshotRepository;
    private final ReportIncomeSourceRepository reportIncomeSourceRepository;
    private final ReportExpenseCategoryRepository reportExpenseCategoryRepository;
    private final ReportInvestmentHoldingRepository reportInvestmentHoldingRepository;
    private final ReportGoalSnapshotRepository reportGoalSnapshotRepository;
    private final ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository;
    private final ReportAssetAllocationRepository reportAssetAllocationRepository;
    private final ReportChangeRepository reportChangeRepository;
    private final ReportInvestmentAllocationRepository reportInvestmentAllocationRepository;
    private final ReportInvestmentHistoricalPerformanceRepository reportInvestmentHistoricalPerformanceRepository;
    private final ReportComparisonSnapshotRepository reportComparisonSnapshotRepository;
    private final ReportChangeContributorRepository reportChangeContributorRepository;

    public ReportSnapshotRetrievalService(
            FinancialReportRepository financialReportRepository,
            FinancialSummarySnapshotRepository financialSummarySnapshotRepository,
            ReportIncomeSourceRepository reportIncomeSourceRepository,
            ReportExpenseCategoryRepository reportExpenseCategoryRepository,
            ReportInvestmentHoldingRepository reportInvestmentHoldingRepository,
            ReportGoalSnapshotRepository reportGoalSnapshotRepository,
            ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository,
            ReportAssetAllocationRepository reportAssetAllocationRepository,
            ReportChangeRepository reportChangeRepository,
            ReportInvestmentAllocationRepository reportInvestmentAllocationRepository,
            ReportInvestmentHistoricalPerformanceRepository reportInvestmentHistoricalPerformanceRepository,
            ReportComparisonSnapshotRepository reportComparisonSnapshotRepository,
            ReportChangeContributorRepository reportChangeContributorRepository
    ) {
        this.financialReportRepository = financialReportRepository;
        this.financialSummarySnapshotRepository =
                financialSummarySnapshotRepository;
        this.reportIncomeSourceRepository =
                reportIncomeSourceRepository;
        this.reportExpenseCategoryRepository =
                reportExpenseCategoryRepository;
        this.reportInvestmentHoldingRepository =
                reportInvestmentHoldingRepository;
        this.reportGoalSnapshotRepository =
                reportGoalSnapshotRepository;
        this.reportLiabilitySnapshotRepository =
                reportLiabilitySnapshotRepository;
        this.reportAssetAllocationRepository =
                reportAssetAllocationRepository;
        this.reportChangeRepository =
                reportChangeRepository;
        this.reportInvestmentAllocationRepository =
                reportInvestmentAllocationRepository;
        this.reportInvestmentHistoricalPerformanceRepository =
                reportInvestmentHistoricalPerformanceRepository;
        this.reportComparisonSnapshotRepository =
                reportComparisonSnapshotRepository;
        this.reportChangeContributorRepository =
                reportChangeContributorRepository;
    }

    @Transactional(readOnly = true)
    public FinancialReportV2Response getReport(
            User user,
            Long reportId
    ) {
        validate(user, reportId);

        FinancialReport report =
                financialReportRepository.findByIdAndUser(
                        reportId,
                        user
                ).orElseThrow(
                        () -> new IllegalArgumentException(
                                "Generated report not found"
                        )
                );

        FinancialSummarySnapshot summary =
                financialSummarySnapshotRepository
                        .findByReport(report)
                        .orElse(null);

        List<ReportIncomeSource> incomeSources =
                reportIncomeSourceRepository.findByReport(report);

        List<ReportExpenseCategory> expenseCategories =
                reportExpenseCategoryRepository.findByReport(report);

        List<ReportInvestmentHolding> investmentHoldings =
                reportInvestmentHoldingRepository.findByReport(report);

        List<ReportInvestmentAllocation> investmentAllocations =
                reportInvestmentAllocationRepository.findByReport(report);

        List<ReportInvestmentHistoricalPerformance>
                investmentHistoricalPerformance =
                reportInvestmentHistoricalPerformanceRepository
                        .findByReport(report);

        List<ReportGoalSnapshot> goalSnapshots =
                reportGoalSnapshotRepository.findByReport(report);

        List<ReportLiabilitySnapshot> liabilitySnapshots =
                reportLiabilitySnapshotRepository.findByReport(report);

        List<ReportAssetAllocation> assetAllocations =
                reportAssetAllocationRepository.findByReport(report);

        List<ReportChange> changes =
                reportChangeRepository.findByReport(report);

        Map<Long, List<ReportChangeContributor>> contributorsByChangeId =
                changes.stream()
                        .collect(
                                Collectors.toMap(
                                        ReportChange::getId,
                                        reportChangeContributorRepository::findByReportChange
                                )
                        );

        ReportComparisonSnapshot comparisonSnapshot =
                reportComparisonSnapshotRepository
                        .findByReport(report)
                        .orElse(null);

        FinancialReportV2Response response =
                new FinancialReportV2Response();

        response.setReportId(report.getId());

        response.setReportPeriod(
                new ReportPeriodResponse(
                        report.getPeriodType(),
                        report.getStartDate(),
                        report.getEndDate()
                )
        );

        response.setGeneratedAt(
                report.getGeneratedAt()
        );

        response.setReportStatus(
                "GENERATED"
        );

        response.setIncome(
                assembleIncome(summary, incomeSources)
        );

        response.setExpenses(
                assembleExpenses(summary, expenseCategories)
        );

        response.setCashFlow(
                assembleCashFlow(summary)
        );

        response.setInvestments(
                assembleInvestments(
                        summary,
                        investmentHoldings,
                        investmentAllocations,
                        investmentHistoricalPerformance
                )
        );

        response.setGoals(
                assembleGoals(summary, goalSnapshots)
        );

        response.setAssets(
                assembleAssets(summary, assetAllocations)
        );

        response.setLiabilities(
                assembleLiabilities(summary, liabilitySnapshots)
        );

        response.setNetWorth(
                assembleNetWorth(summary)
        );

        response.setFinancialHealth(
                assembleFinancialHealth(summary)
        );

        response.setComparison(
                assembleComparison(
                        report,
                        comparisonSnapshot
                )
        );

        response.setChanges(
                assembleChanges(
                        changes,
                        contributorsByChangeId
                )
        );

        return response;
    }

    private void validate(
            User user,
            Long reportId
    ) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (reportId == null) {
            throw new IllegalArgumentException(
                    "Report ID must not be null"
            );
        }
    }

    private IncomeReportV2Response assembleIncome(
            FinancialSummarySnapshot summary,
            List<ReportIncomeSource> sources
    ) {
        IncomeReportV2Response response =
                new IncomeReportV2Response();

        if (summary == null) {
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setTotalIncome(
                summary.getTotalIncome()
        );

        response.setRecurringIncome(
                summary.getRecurringIncome()
        );

        response.setIrregularIncome(
                summary.getIrregularIncome()
        );

        Map<String, BigDecimal> sourceBreakdown =
                sources.stream()
                        .collect(
                                Collectors.toMap(
                                        ReportIncomeSource::getSource,
                                        ReportIncomeSource::getAmount,
                                        BigDecimal::add
                                )
                        );

        response.setSourceBreakdown(
                sourceBreakdown
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private ExpenseReportV2Response assembleExpenses(
            FinancialSummarySnapshot summary,
            List<ReportExpenseCategory> categories
    ) {
        ExpenseReportV2Response response =
                new ExpenseReportV2Response();

        if (summary == null) {
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setTotalConfirmedExpenses(
                summary.getTotalExpense()
        );

        Map<String, BigDecimal> categoryBreakdown =
                categories.stream()
                        .collect(
                                Collectors.toMap(
                                        ReportExpenseCategory::getCategory,
                                        ReportExpenseCategory::getAmount,
                                        BigDecimal::add
                                )
                        );

        response.setCategoryBreakdown(
                categoryBreakdown
        );

        response.setHelpAmounts(
                summary.getHelpAmounts()
        );

        response.setConvertedHelpExpenses(
                summary.getConvertedHelpExpenses()
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private CashFlowReportV2Response assembleCashFlow(
            FinancialSummarySnapshot summary
    ) {
        CashFlowReportV2Response response =
                new CashFlowReportV2Response();

        if (summary == null) {
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setInflows(
                summary.getInflows()
        );

        response.setOutflows(
                summary.getOutflows()
        );

        response.setDebtPaymentOutflows(
                summary.getDebtPaymentOutflows()
        );

        response.setNetCashFlow(
                summary.getNetCashFlow()
        );

        response.setSavings(
                summary.getSavings()
        );

        response.setSavingsRate(
                summary.getSavingsRate()
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private InvestmentReportV2Response assembleInvestments(
            FinancialSummarySnapshot summary,
            List<ReportInvestmentHolding> holdings,
            List<ReportInvestmentAllocation> allocations,
            List<ReportInvestmentHistoricalPerformance>
                    historicalPerformance
    ) {
        InvestmentReportV2Response response =
                new InvestmentReportV2Response();

        if (summary == null) {
            response.setHoldings(new ArrayList<>());
            response.setAssetAllocation(new ArrayList<>());
            response.setBestPerformers(new ArrayList<>());
            response.setWorstPerformers(new ArrayList<>());
            response.setHistoricalPerformance(new ArrayList<>());
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        BigDecimal investedAmount =
                summary.getInvestmentValue()
                        .subtract(
                                summary.getInvestmentProfitLoss()
                        );

        response.setInvestedAmount(
                investedAmount
        );

        response.setPortfolioValue(
                summary.getInvestmentValue()
        );

        response.setProfitLoss(
                summary.getInvestmentProfitLoss()
        );

        response.setReturnPercentage(
                summary.getInvestmentReturn()
        );

        response.setValuationDate(
                summary.getInvestmentValuationDate() == null
                        ? null
                        : summary.getInvestmentValuationDate()
                        .toString()
        );

        List<ReportInvestmentHoldingV2Response>
                holdingResponses =
                holdings.stream()
                        .map(this::mapInvestmentHolding)
                        .toList();

        response.setHoldings(
                holdingResponses
        );

        List<InvestmentAllocationV2Response>
                allocationResponses =
                allocations.stream()
                        .map(this::mapInvestmentAllocation)
                        .toList();

        response.setAssetAllocation(
                allocationResponses
        );

        List<ReportInvestmentHoldingV2Response>
                bestPerformers =
                holdingResponses.stream()
                        .sorted(
                                (a, b) ->
                                        b.getReturnPercentage()
                                                .compareTo(
                                                        a.getReturnPercentage()
                                                )
                        )
                        .limit(5)
                        .toList();

        List<ReportInvestmentHoldingV2Response>
                worstPerformers =
                holdingResponses.stream()
                        .sorted(
                                (a, b) ->
                                        a.getReturnPercentage()
                                                .compareTo(
                                                        b.getReturnPercentage()
                                                )
                        )
                        .limit(5)
                        .toList();

        response.setBestPerformers(
                bestPerformers
        );

        response.setWorstPerformers(
                worstPerformers
        );

        response.setHistoricalPerformance(
                historicalPerformance.stream()
                        .map(this::mapInvestmentHistoricalPerformance)
                        .toList()
        );

        response.setMetadata(
                historicalPerformance.isEmpty()
                        ? historicalUnavailableMetadata()
                        : availableMetadata()
        );

        return response;
    }

    private ReportInvestmentHoldingV2Response mapInvestmentHolding(
            ReportInvestmentHolding snapshot
    ) {
        ReportInvestmentHoldingV2Response response =
                new ReportInvestmentHoldingV2Response();

        response.setInvestmentId(
                snapshot.getInvestmentId()
        );

        response.setInvestmentName(
                snapshot.getInvestmentName()
        );

        response.setInvestmentType(
                snapshot.getInvestmentType()
        );

        response.setInvestedAmount(
                snapshot.getInvestedAmount()
        );

        response.setCurrentValue(
                snapshot.getCurrentValue()
        );

        response.setProfitLoss(
                snapshot.getProfitLoss()
        );

        response.setReturnPercentage(
                snapshot.getReturnPercentage()
        );

        return response;
    }

    private InvestmentAllocationV2Response mapInvestmentAllocation(
            ReportInvestmentAllocation snapshot
    ) {
        InvestmentAllocationV2Response response =
                new InvestmentAllocationV2Response();

        response.setAssetType(
                snapshot.getAssetType()
        );

        response.setAmount(
                snapshot.getAmount()
        );

        response.setPercentage(
                snapshot.getPercentage()
        );

        return response;
    }

    private InvestmentHistoricalPerformanceV2Response
    mapInvestmentHistoricalPerformance(
            ReportInvestmentHistoricalPerformance snapshot
    ) {
        InvestmentHistoricalPerformanceV2Response response =
                new InvestmentHistoricalPerformanceV2Response();

        response.setValuationDate(
                snapshot.getValuationDate().toString()
        );

        response.setInvestedAmount(
                snapshot.getInvestedAmount()
        );

        response.setPortfolioValue(
                snapshot.getPortfolioValue()
        );

        response.setProfitLoss(
                snapshot.getProfitLoss()
        );

        response.setReturnPercentage(
                snapshot.getReturnPercentage()
        );

        return response;
    }

    private GoalReportV2Response assembleGoals(
            FinancialSummarySnapshot summary,
            List<ReportGoalSnapshot> snapshots
    ) {
        GoalReportV2Response response =
                new GoalReportV2Response();

        if (summary == null) {
            response.setGoals(
                    new ArrayList<>()
            );
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setTotalGoals(
                summary.getTotalGoals().intValue()
        );

        response.setCompletedGoals(
                summary.getCompletedGoals().intValue()
        );

        response.setAtRiskGoals(
                summary.getAtRiskGoals().intValue()
        );

        response.setOnTrackGoals(
                summary.getOnTrackGoals().intValue()
        );

        response.setGoals(
                snapshots.stream()
                        .map(this::mapGoalSnapshot)
                        .toList()
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private GoalReportV2Response.GoalItem mapGoalSnapshot(
            ReportGoalSnapshot snapshot
    ) {
        GoalReportV2Response.GoalItem item =
                new GoalReportV2Response.GoalItem();

        item.setGoalId(
                snapshot.getGoalId()
        );

        item.setGoalName(
                snapshot.getGoalName()
        );

        item.setTargetAmount(
                snapshot.getTargetAmount()
        );

        item.setCurrentAmount(
                snapshot.getCurrentAmount()
        );

        item.setRemainingAmount(
                snapshot.getRemainingAmount()
        );

        item.setContribution(
                snapshot.getContribution()
        );

        item.setTargetDate(
                snapshot.getTargetDate()
        );

        item.setProjectedCompletionDate(
                snapshot.getProjectedCompletionDate()
        );

        item.setStatus(
                snapshot.getStatus()
        );

        return item;
    }

    private AssetReportV2Response assembleAssets(
            FinancialSummarySnapshot summary,
            List<ReportAssetAllocation> allocations
    ) {
        AssetReportV2Response response =
                new AssetReportV2Response();

        BigDecimal recognizedAssets =
                summary != null && summary.getRecognizedAssets() != null
                        ? summary.getRecognizedAssets()
                        : BigDecimal.ZERO;

        BigDecimal liquidAssets =
                summary != null && summary.getLiquidAssets() != null
                        ? summary.getLiquidAssets()
                        : BigDecimal.ZERO;

        response.setRecognizedAssets(
                recognizedAssets
        );

        response.setLiquidAssets(
                liquidAssets
        );

        BigDecimal totalAllocationAmount =
                allocations.stream()
                        .map(ReportAssetAllocation::getAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        List<AssetAllocationV2Response>
                allocationResponses =
                allocations.stream()
                        .map(allocation -> {

                            BigDecimal amount =
                                    allocation.getAmount() != null
                                            ? allocation.getAmount()
                                            : BigDecimal.ZERO;

                            BigDecimal percentage =
                                    BigDecimal.ZERO;

                            if (totalAllocationAmount.compareTo(
                                    BigDecimal.ZERO
                            ) > 0) {

                                percentage =
                                        amount
                                                .divide(
                                                        totalAllocationAmount,
                                                        4,
                                                        RoundingMode.HALF_UP
                                                )
                                                .multiply(
                                                        BigDecimal.valueOf(100)
                                                )
                                                .setScale(
                                                        2,
                                                        RoundingMode.HALF_UP
                                                );
                            }

                            AssetAllocationV2Response dto =
                                    new AssetAllocationV2Response();

                            dto.setAssetType(
                                    allocation.getAssetType() != null
                                            ? allocation.getAssetType().name()
                                            : null
                            );

                            dto.setAmount(amount);
                            dto.setPercentage(percentage);

                            return dto;
                        })
                        .toList();

        response.setAllocation(
                allocationResponses
        );

        response.setMetadata(
                summary == null
                        ? historicalUnavailableMetadata()
                        : availableMetadata()
        );

        return response;
    }

    private LiabilityReportV2Response assembleLiabilities(
            FinancialSummarySnapshot summary,
            List<ReportLiabilitySnapshot> snapshots
    ) {
        LiabilityReportV2Response response =
                new LiabilityReportV2Response();

        if (summary == null) {
            response.setLiabilities(
                    new ArrayList<>()
            );
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setRecognizedLiabilities(
                summary.getRecognizedLiabilities()
        );

        response.setDebtPayments(
                BigDecimal.ZERO
        );

        response.setPrincipalPaid(
                BigDecimal.ZERO
        );

        response.setInterestPaid(
                BigDecimal.ZERO
        );

        response.setLiabilities(
                snapshots.stream()
                        .map(this::mapLiabilitySnapshot)
                        .toList()
        );

        for (ReportLiabilitySnapshot snapshot : snapshots) {
            response.setDebtPayments(
                    response.getDebtPayments()
                            .add(
                                    valueOrZero(
                                            snapshot.getDebtPayment()
                                    )
                            )
            );

            response.setPrincipalPaid(
                    response.getPrincipalPaid()
                            .add(
                                    valueOrZero(
                                            snapshot.getPrincipalPaid()
                                    )
                            )
            );

            response.setInterestPaid(
                    response.getInterestPaid()
                            .add(
                                    valueOrZero(
                                            snapshot.getInterestPaid()
                                    )
                            )
            );
        }

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private ReportLiabilityItemV2Response mapLiabilitySnapshot(
            ReportLiabilitySnapshot snapshot
    ) {
        ReportLiabilityItemV2Response response =
                new ReportLiabilityItemV2Response();

        response.setLiabilityId(
                snapshot.getLiabilityId()
        );

        response.setLiabilityName(
                snapshot.getLiabilityName()
        );

        response.setOutstandingAmount(
                snapshot.getOutstandingAmount()
        );

        response.setMonthlyPayment(
                snapshot.getMonthlyPayment()
        );

        response.setPrincipalPaid(
                snapshot.getPrincipalPaid()
        );

        response.setInterestPaid(
                snapshot.getInterestPaid()
        );

        response.setStatus(
                snapshot.getStatus()
        );

        return response;
    }

    private NetWorthReportV2Response assembleNetWorth(
            FinancialSummarySnapshot summary
    ) {
        NetWorthReportV2Response response =
                new NetWorthReportV2Response();

        if (summary == null) {
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setRecognizedAssets(
                summary.getRecognizedAssets()
        );

        response.setRecognizedLiabilities(
                summary.getRecognizedLiabilities()
        );

        response.setNetWorth(
                summary.getNetWorth()
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private FinancialHealthReportV2Response assembleFinancialHealth(
            FinancialSummarySnapshot summary
    ) {
        FinancialHealthReportV2Response response =
                new FinancialHealthReportV2Response();

        if (summary == null) {
            response.setMetadata(
                    historicalUnavailableMetadata()
            );
            return response;
        }

        response.setCashFlowHealth(
                summary.getFinancialHealthCashFlow()
        );

        response.setDebtHealth(
                summary.getFinancialHealthDebt()
        );

        response.setSavingsHealth(
                summary.getFinancialHealthSavings()
        );

        response.setInvestmentHealth(
                summary.getFinancialHealthInvestments()
        );

        response.setGoalHealth(
                summary.getFinancialHealthGoals()
        );

        response.setWealthHealth(
                summary.getFinancialHealthWealth()
        );

        response.setOverallStatus(
                summary.getFinancialHealthOverallStatus()
        );

        response.setMetadata(
                availableMetadata()
        );

        return response;
    }

    private ReportComparisonV2Response assembleComparison(
            FinancialReport report,
            ReportComparisonSnapshot snapshot
    ) {
        if (snapshot == null) {
            return null;
        }

        ReportComparisonV2Response response =
                new ReportComparisonV2Response();

        response.setComparisonAvailable(
                snapshot.isComparisonAvailable()
        );

        response.setCurrentPeriod(
                new ReportPeriodResponse(
                        report.getPeriodType(),
                        report.getStartDate(),
                        report.getEndDate()
                )
        );

        if (snapshot.getPreviousPeriodType() != null &&
                snapshot.getPreviousStartDate() != null &&
                snapshot.getPreviousEndDate() != null) {

            response.setPreviousPeriod(
                    new ReportPeriodResponse(
                            snapshot.getPreviousPeriodType(),
                            snapshot.getPreviousStartDate(),
                            snapshot.getPreviousEndDate()
                    )
            );
        }

        response.setIncome(
                mapMetric(
                        snapshot.getIncomeCurrentValue(),
                        snapshot.getIncomePreviousValue(),
                        snapshot.getIncomeAbsoluteChange(),
                        snapshot.getIncomePercentageChange()
                )
        );

        response.setExpenses(
                mapMetric(
                        snapshot.getExpensesCurrentValue(),
                        snapshot.getExpensesPreviousValue(),
                        snapshot.getExpensesAbsoluteChange(),
                        snapshot.getExpensesPercentageChange()
                )
        );

        response.setSavings(
                mapMetric(
                        snapshot.getSavingsCurrentValue(),
                        snapshot.getSavingsPreviousValue(),
                        snapshot.getSavingsAbsoluteChange(),
                        snapshot.getSavingsPercentageChange()
                )
        );

        response.setInvestments(
                mapMetric(
                        snapshot.getInvestmentsCurrentValue(),
                        snapshot.getInvestmentsPreviousValue(),
                        snapshot.getInvestmentsAbsoluteChange(),
                        snapshot.getInvestmentsPercentageChange()
                )
        );

        response.setNetWorth(
                mapMetric(
                        snapshot.getNetWorthCurrentValue(),
                        snapshot.getNetWorthPreviousValue(),
                        snapshot.getNetWorthAbsoluteChange(),
                        snapshot.getNetWorthPercentageChange()
                )
        );

        return response;
    }

    private ReportMetricChangeV2Response mapMetric(
            BigDecimal currentValue,
            BigDecimal previousValue,
            BigDecimal absoluteChange,
            BigDecimal percentageChange
    ) {
        if (currentValue == null &&
                previousValue == null &&
                absoluteChange == null &&
                percentageChange == null) {
            return null;
        }

        ReportMetricChangeV2Response response =
                new ReportMetricChangeV2Response();

        response.setCurrentValue(currentValue);
        response.setPreviousValue(previousValue);
        response.setAbsoluteChange(absoluteChange);
        response.setPercentageChange(percentageChange);

        return response;
    }

    private List<ReportChangeInsightV2Response> assembleChanges(
            List<ReportChange> snapshots,
            Map<Long, List<ReportChangeContributor>> contributorsByChangeId
    ) {
        if (snapshots == null) {
            return new ArrayList<>();
        }

        return snapshots.stream()
                .map(
                        snapshot ->
                                mapChange(
                                        snapshot,
                                        contributorsByChangeId.getOrDefault(
                                                snapshot.getId(),
                                                new ArrayList<>()
                                        )
                                )
                )
                .toList();
    }

    private ReportChangeInsightV2Response mapChange(
            ReportChange snapshot,
            List<ReportChangeContributor> contributors
    ) {
        ReportChangeInsightV2Response response =
                new ReportChangeInsightV2Response();

        response.setMetric(
                snapshot.getMetric()
        );

        response.setPreviousValue(
                snapshot.getPreviousValue()
        );

        response.setCurrentValue(
                snapshot.getCurrentValue()
        );

        response.setAbsoluteChange(
                snapshot.getChangeAmount()
        );

        response.setPercentageChange(
                snapshot.getChangePercentage()
        );

        response.setDirection(
                snapshot.getDirection()
        );

        response.setSignificant(
                "SIGNIFICANT".equalsIgnoreCase(
                        snapshot.getSignificance()
                )
        );

        response.setWhatChanged(
                buildWhatChanged(snapshot)
        );

        response.setContributors(
                contributors.stream()
                        .map(this::mapContributor)
                        .toList()
        );

        response.setWhyDidItChange(
                buildWhyDidItChange(
                        snapshot,
                        contributors
                )
        );

        return response;
    }

    private ReportChangeContributorV2Response mapContributor(
            ReportChangeContributor snapshot
    ) {
        ReportChangeContributorV2Response response =
                new ReportChangeContributorV2Response();

        response.setName(
                snapshot.getName()
        );

        response.setCurrentValue(
                snapshot.getCurrentValue()
        );

        response.setPreviousValue(
                snapshot.getPreviousValue()
        );

        response.setAbsoluteChange(
                snapshot.getAbsoluteChange()
        );

        response.setPercentageChange(
                snapshot.getPercentageChange()
        );

        return response;
    }

    private String buildWhatChanged(
            ReportChange snapshot
    ) {
        if ("UNCHANGED".equalsIgnoreCase(
                snapshot.getDirection()
        )) {
            return snapshot.getMetric()
                    + " remained unchanged.";
        }

        return snapshot.getMetric()
                + " "
                + snapshot.getDirection().toLowerCase()
                + " by "
                + snapshot.getChangeAmount()
                + " ("
                + snapshot.getChangePercentage()
                + "%).";
    }

    private String buildWhyDidItChange(
            ReportChange snapshot,
            List<ReportChangeContributor> contributors
    ) {
        if (contributors == null ||
                contributors.isEmpty()) {

            return "No persisted contributor details are available for this change.";
        }

        String contributorSummary =
                contributors.stream()
                        .map(
                                contributor ->
                                        contributor.getName()
                                                + " changed by "
                                                + contributor.getAbsoluteChange()
                        )
                        .collect(
                                Collectors.joining(", ")
                        );

        return "The change was primarily driven by: "
                + contributorSummary
                + ".";
    }

    private BigDecimal valueOrZero(
            BigDecimal value
    ) {
        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    private ReportSectionMetadata availableMetadata() {
        return new ReportSectionMetadata(
                ReportSectionStatus.AVAILABLE,
                "Generated report snapshot was retrieved successfully."
        );
    }

    private ReportSectionMetadata historicalUnavailableMetadata() {
        return new ReportSectionMetadata(
                ReportSectionStatus.HISTORICAL_DATA_UNAVAILABLE,
                "The persisted snapshot section is not available."
        );
    }
}