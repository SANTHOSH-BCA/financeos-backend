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
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeContributorV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportInvestmentHoldingV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportMetricChangeV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportLiabilityItemV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ReportSnapshotPersistenceService {

    private final FinancialReportRepository financialReportRepository;
    private final FinancialSummarySnapshotRepository financialSummarySnapshotRepository;
    private final ReportIncomeSourceRepository reportIncomeSourceRepository;
    private final ReportExpenseCategoryRepository reportExpenseCategoryRepository;
    private final ReportInvestmentHoldingRepository reportInvestmentHoldingRepository;
    private final ReportGoalSnapshotRepository reportGoalSnapshotRepository;
    private final ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository;
    private final ReportAssetAllocationRepository reportAssetAllocationRepository;
    private final ReportChangeRepository reportChangeRepository;

    private final ReportInvestmentAllocationRepository
            reportInvestmentAllocationRepository;

    private final ReportInvestmentHistoricalPerformanceRepository
            reportInvestmentHistoricalPerformanceRepository;

    private final ReportComparisonSnapshotRepository
            reportComparisonSnapshotRepository;

    private final ReportChangeContributorRepository
            reportChangeContributorRepository;

    public ReportSnapshotPersistenceService(
            FinancialReportRepository financialReportRepository,
            FinancialSummarySnapshotRepository financialSummarySnapshotRepository,
            ReportIncomeSourceRepository reportIncomeSourceRepository,
            ReportExpenseCategoryRepository reportExpenseCategoryRepository,
            ReportInvestmentHoldingRepository reportInvestmentHoldingRepository,
            ReportGoalSnapshotRepository reportGoalSnapshotRepository,
            ReportLiabilitySnapshotRepository reportLiabilitySnapshotRepository,
            ReportAssetAllocationRepository reportAssetAllocationRepository,
            ReportChangeRepository reportChangeRepository,
            ReportInvestmentAllocationRepository
                    reportInvestmentAllocationRepository,
            ReportInvestmentHistoricalPerformanceRepository
                    reportInvestmentHistoricalPerformanceRepository,
            ReportComparisonSnapshotRepository
                    reportComparisonSnapshotRepository,
            ReportChangeContributorRepository
                    reportChangeContributorRepository
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

    @Transactional
    public FinancialReportV2Response persist(
            User user,
            FinancialReportV2Response response
    ) {
        validate(user, response);

        ReportPeriodResponse period =
                response.getReportPeriod();

        FinancialReport report =
                new FinancialReport();

        report.setUser(user);
        report.setPeriodType(period.getPeriodType());
        report.setStartDate(period.getStartDate());
        report.setEndDate(period.getEndDate());
        report.setGeneratedAt(response.getGeneratedAt());
        report.setSnapshotVersion(1);

        FinancialReport savedReport =
                financialReportRepository.save(report);

        persistSummary(savedReport, response);
        persistIncomeSources(savedReport, response.getIncome());
        persistExpenseCategories(savedReport, response.getExpenses());
        persistInvestmentHoldings(savedReport, response.getInvestments());
        persistGoals(savedReport, response.getGoals());
        persistLiabilities(savedReport, response.getLiabilities());
        persistAssetAllocation(savedReport, response.getAssets());

        persistChanges(
                savedReport,
                response.getChanges()
        );

        persistInvestmentAllocation(
                savedReport,
                response.getInvestments()
        );

        persistInvestmentHistoricalPerformance(
                savedReport,
                response.getInvestments()
        );

        persistComparison(
                savedReport,
                response.getComparison()
        );

        response.setReportId(savedReport.getId());
        response.setReportStatus("GENERATED");

        return response;
    }

    private void validate(
            User user,
            FinancialReportV2Response response
    ) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "User must not be null"
            );
        }

        if (response == null) {
            throw new IllegalArgumentException(
                    "Report response must not be null"
            );
        }

        if (response.getReportPeriod() == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null"
            );
        }
    }

    private void persistSummary(
            FinancialReport report,
            FinancialReportV2Response response
    ) {
        FinancialSummarySnapshot snapshot =
                new FinancialSummarySnapshot();

        IncomeReportV2Response income =
                response.getIncome();

        ExpenseReportV2Response expenses =
                response.getExpenses();

        CashFlowReportV2Response cashFlow =
                response.getCashFlow();

        InvestmentReportV2Response investments =
                response.getInvestments();

        GoalReportV2Response goals =
                response.getGoals();

        AssetReportV2Response assets =
                response.getAssets();

        LiabilityReportV2Response liabilities =
                response.getLiabilities();

        NetWorthReportV2Response netWorth =
                response.getNetWorth();

        FinancialHealthReportV2Response health =
                response.getFinancialHealth();

        snapshot.setReport(report);

        snapshot.setTotalIncome(
                valueOrZero(
                        income == null
                                ? null
                                : income.getTotalIncome()
                )
        );

        snapshot.setRecurringIncome(
                valueOrZero(
                        income == null
                                ? null
                                : income.getRecurringIncome()
                )
        );

        snapshot.setIrregularIncome(
                valueOrZero(
                        income == null
                                ? null
                                : income.getIrregularIncome()
                )
        );

        snapshot.setTotalExpense(
                valueOrZero(
                        expenses == null
                                ? null
                                : expenses.getTotalConfirmedExpenses()
                )
        );

        snapshot.setHelpAmounts(
                valueOrZero(
                        expenses == null
                                ? null
                                : expenses.getHelpAmounts()
                )
        );

        snapshot.setConvertedHelpExpenses(
                valueOrZero(
                        expenses == null
                                ? null
                                : expenses.getConvertedHelpExpenses()
                )
        );

        snapshot.setInflows(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getInflows()
                )
        );


        snapshot.setDebtPaymentOutflows(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getDebtPaymentOutflows()
                )
        );

        snapshot.setOutflows(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getOutflows()
                )
        );

        snapshot.setNetCashFlow(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getNetCashFlow()
                )
        );

        snapshot.setSavings(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getSavings()
                )
        );

        snapshot.setSavingsRate(
                valueOrZero(
                        cashFlow == null
                                ? null
                                : cashFlow.getSavingsRate()
                )
        );

        snapshot.setRecognizedAssets(
                valueOrZero(
                        assets == null
                                ? null
                                : assets.getRecognizedAssets()
                )
        );

        snapshot.setLiquidAssets(
                valueOrZero(
                        assets == null
                                ? null
                                : assets.getLiquidAssets()
                )
        );

        snapshot.setRecognizedLiabilities(
                valueOrZero(
                        liabilities == null
                                ? null
                                : liabilities.getRecognizedLiabilities()
                )
        );

        snapshot.setNetWorth(
                valueOrZero(
                        netWorth == null
                                ? null
                                : netWorth.getNetWorth()
                )
        );

        snapshot.setInvestmentValue(
                valueOrZero(
                        investments == null
                                ? null
                                : investments.getPortfolioValue()
                )
        );

        snapshot.setInvestmentProfitLoss(
                valueOrZero(
                        investments == null
                                ? null
                                : investments.getProfitLoss()
                )
        );

        snapshot.setInvestmentReturn(
                valueOrZero(
                        investments == null
                                ? null
                                : investments.getReturnPercentage()
                )
        );

        if (investments != null &&
                investments.getValuationDate() != null) {

            snapshot.setInvestmentValuationDate(
                    java.time.LocalDate.parse(
                            investments.getValuationDate()
                    )
            );
        }

        snapshot.setTotalGoals(
                goals == null
                        ? 0L
                        : (long) goals.getTotalGoals()
        );

        snapshot.setCompletedGoals(
                goals == null
                        ? 0L
                        : (long) goals.getCompletedGoals()
        );

        snapshot.setAtRiskGoals(
                goals == null
                        ? 0L
                        : (long) goals.getAtRiskGoals()
        );

        snapshot.setOnTrackGoals(
                goals == null
                        ? 0L
                        : (long) goals.getOnTrackGoals()
        );

        snapshot.setFinancialHealthCashFlow(
                health == null
                        ? null
                        : health.getCashFlowHealth()
        );

        snapshot.setFinancialHealthDebt(
                health == null
                        ? null
                        : health.getDebtHealth()
        );

        snapshot.setFinancialHealthSavings(
                health == null
                        ? null
                        : health.getSavingsHealth()
        );

        snapshot.setFinancialHealthInvestments(
                health == null
                        ? null
                        : health.getInvestmentHealth()
        );

        snapshot.setFinancialHealthGoals(
                health == null
                        ? null
                        : health.getGoalHealth()
        );

        snapshot.setFinancialHealthWealth(
                health == null
                        ? null
                        : health.getWealthHealth()
        );

        snapshot.setFinancialHealthOverallStatus(
                health == null
                        ? null
                        : health.getOverallStatus()
        );

        financialSummarySnapshotRepository.save(snapshot);
    }

    private void persistIncomeSources(
            FinancialReport report,
            IncomeReportV2Response income
    ) {
        if (income == null ||
                income.getSourceBreakdown() == null) {
            return;
        }

        for (Map.Entry<String, BigDecimal> entry :
                income.getSourceBreakdown().entrySet()) {

            ReportIncomeSource source =
                    new ReportIncomeSource();

            source.setReport(report);
            source.setSource(entry.getKey());
            source.setAmount(
                    valueOrZero(entry.getValue())
            );

            reportIncomeSourceRepository.save(source);
        }
    }

    private void persistExpenseCategories(
            FinancialReport report,
            ExpenseReportV2Response expenses
    ) {
        if (expenses == null ||
                expenses.getCategoryBreakdown() == null) {
            return;
        }

        for (Map.Entry<String, BigDecimal> entry :
                expenses.getCategoryBreakdown().entrySet()) {

            ReportExpenseCategory category =
                    new ReportExpenseCategory();

            category.setReport(report);
            category.setCategory(entry.getKey());
            category.setAmount(
                    valueOrZero(entry.getValue())
            );

            reportExpenseCategoryRepository.save(category);
        }
    }

    private void persistInvestmentHoldings(
            FinancialReport report,
            InvestmentReportV2Response investments
    ) {
        if (investments == null ||
                investments.getHoldings() == null) {
            return;
        }

        for (ReportInvestmentHoldingV2Response holding :
                investments.getHoldings()) {

            if (holding == null) {
                continue;
            }

            ReportInvestmentHolding snapshot =
                    new ReportInvestmentHolding();

            snapshot.setReport(report);

            snapshot.setInvestmentId(
                    holding.getInvestmentId()
            );

            snapshot.setInvestmentName(
                    holding.getInvestmentName()
            );

            snapshot.setInvestmentType(
                    holding.getInvestmentType()
            );

            snapshot.setInvestedAmount(
                    valueOrZero(
                            holding.getInvestedAmount()
                    )
            );

            snapshot.setCurrentValue(
                    valueOrZero(
                            holding.getCurrentValue()
                    )
            );

            snapshot.setProfitLoss(
                    valueOrZero(
                            holding.getProfitLoss()
                    )
            );

            snapshot.setReturnPercentage(
                    valueOrZero(
                            holding.getReturnPercentage()
                    )
            );

            reportInvestmentHoldingRepository.save(
                    snapshot
            );
        }
    }

    private void persistInvestmentAllocation(
            FinancialReport report,
            InvestmentReportV2Response investments
    ) {
        if (investments == null ||
                investments.getAssetAllocation() == null) {
            return;
        }

        for (InvestmentAllocationV2Response allocation :
                investments.getAssetAllocation()) {

            if (allocation == null ||
                    allocation.getAssetType() == null) {
                continue;
            }

            ReportInvestmentAllocation snapshot =
                    new ReportInvestmentAllocation();

            snapshot.setReport(report);

            snapshot.setAssetType(
                    allocation.getAssetType()
            );

            snapshot.setAmount(
                    valueOrZero(
                            allocation.getAmount()
                    )
            );

            snapshot.setPercentage(
                    valueOrZero(
                            allocation.getPercentage()
                    )
            );

            reportInvestmentAllocationRepository.save(
                    snapshot
            );
        }
    }

    private void persistInvestmentHistoricalPerformance(
            FinancialReport report,
            InvestmentReportV2Response investments
    ) {
        if (investments == null ||
                investments.getHistoricalPerformance() == null) {
            return;
        }

        for (InvestmentHistoricalPerformanceV2Response historical :
                investments.getHistoricalPerformance()) {

            if (historical == null ||
                    historical.getValuationDate() == null) {
                continue;
            }

            ReportInvestmentHistoricalPerformance snapshot =
                    new ReportInvestmentHistoricalPerformance();

            snapshot.setReport(report);

            snapshot.setValuationDate(
                    java.time.LocalDate.parse(
                            historical.getValuationDate()
                    )
            );

            snapshot.setInvestedAmount(
                    valueOrZero(
                            historical.getInvestedAmount()
                    )
            );

            snapshot.setPortfolioValue(
                    valueOrZero(
                            historical.getPortfolioValue()
                    )
            );

            snapshot.setProfitLoss(
                    valueOrZero(
                            historical.getProfitLoss()
                    )
            );

            snapshot.setReturnPercentage(
                    valueOrZero(
                            historical.getReturnPercentage()
                    )
            );

            reportInvestmentHistoricalPerformanceRepository.save(
                    snapshot
            );
        }
    }

    private void persistGoals(
            FinancialReport report,
            GoalReportV2Response goals
    ) {
        if (goals == null ||
                goals.getGoals() == null) {
            return;
        }

        for (GoalReportV2Response.GoalItem goal :
                goals.getGoals()) {

            if (goal == null) {
                continue;
            }

            ReportGoalSnapshot snapshot =
                    new ReportGoalSnapshot();

            snapshot.setReport(report);
            snapshot.setGoalId(goal.getGoalId());
            snapshot.setGoalName(goal.getGoalName());

            snapshot.setTargetAmount(
                    valueOrZero(
                            goal.getTargetAmount()
                    )
            );

            snapshot.setCurrentAmount(
                    valueOrZero(
                            goal.getCurrentAmount()
                    )
            );

            snapshot.setRemainingAmount(
                    valueOrZero(
                            goal.getRemainingAmount()
                    )
            );

            snapshot.setContribution(
                    valueOrZero(
                            goal.getContribution()
                    )
            );

            snapshot.setTargetDate(
                    goal.getTargetDate()
            );

            snapshot.setProjectedCompletionDate(
                    goal.getProjectedCompletionDate()
            );

            snapshot.setStatus(
                    goal.getStatus()
            );

            reportGoalSnapshotRepository.save(
                    snapshot
            );
        }
    }

    private void persistLiabilities(
            FinancialReport report,
            LiabilityReportV2Response liabilities
    ) {
        if (liabilities == null ||
                liabilities.getLiabilities() == null) {
            return;
        }

        for (ReportLiabilityItemV2Response liability :
                liabilities.getLiabilities()) {

            if (liability == null) {
                continue;
            }

            ReportLiabilitySnapshot snapshot =
                    new ReportLiabilitySnapshot();

            snapshot.setReport(report);

            snapshot.setLiabilityId(
                    liability.getLiabilityId()
            );

            snapshot.setLiabilityName(
                    liability.getLiabilityName()
            );

            snapshot.setOutstandingAmount(
                    valueOrZero(
                            liability.getOutstandingAmount()
                    )
            );

            snapshot.setMonthlyPayment(
                    valueOrZero(
                            liability.getMonthlyPayment()
                    )
            );

            snapshot.setPrincipalPaid(
                    valueOrZero(
                            liability.getPrincipalPaid()
                    )
            );

            snapshot.setInterestPaid(
                    valueOrZero(
                            liability.getInterestPaid()
                    )
            );

            snapshot.setDebtPayment(
                    valueOrZero(
                            liability.getPrincipalPaid()
                    ).add(
                            valueOrZero(
                                    liability.getInterestPaid()
                            )
                    )
            );

            snapshot.setStatus(
                    liability.getStatus()
            );

            reportLiabilitySnapshotRepository.save(
                    snapshot
            );
        }
    }

    private void persistComparison(
            FinancialReport report,
            ReportComparisonV2Response comparison
    ) {
        if (comparison == null) {
            return;
        }

        ReportComparisonSnapshot snapshot =
                new ReportComparisonSnapshot();

        snapshot.setReport(report);

        snapshot.setComparisonAvailable(
                comparison.isComparisonAvailable()
        );

        if (comparison.getPreviousPeriod() != null) {

            snapshot.setPreviousPeriodType(
                    comparison.getPreviousPeriod().getPeriodType()
            );

            snapshot.setPreviousStartDate(
                    comparison.getPreviousPeriod().getStartDate()
            );

            snapshot.setPreviousEndDate(
                    comparison.getPreviousPeriod().getEndDate()
            );
        }

        persistMetric(
                comparison.getIncome(),
                snapshot,
                "INCOME"
        );

        persistMetric(
                comparison.getExpenses(),
                snapshot,
                "EXPENSES"
        );

        persistMetric(
                comparison.getSavings(),
                snapshot,
                "SAVINGS"
        );

        persistMetric(
                comparison.getInvestments(),
                snapshot,
                "INVESTMENTS"
        );

        persistMetric(
                comparison.getNetWorth(),
                snapshot,
                "NET_WORTH"
        );

        reportComparisonSnapshotRepository.save(snapshot);
    }

    private void persistMetric(
            ReportMetricChangeV2Response metric,
            ReportComparisonSnapshot snapshot,
            String metricName
    ) {
        if (metric == null) {
            return;
        }

        switch (metricName) {

            case "INCOME" -> {
                snapshot.setIncomeCurrentValue(
                        valueOrZero(metric.getCurrentValue())
                );
                snapshot.setIncomePreviousValue(
                        valueOrZero(metric.getPreviousValue())
                );
                snapshot.setIncomeAbsoluteChange(
                        valueOrZero(metric.getAbsoluteChange())
                );
                snapshot.setIncomePercentageChange(
                        valueOrZero(metric.getPercentageChange())
                );
            }

            case "EXPENSES" -> {
                snapshot.setExpensesCurrentValue(
                        valueOrZero(metric.getCurrentValue())
                );
                snapshot.setExpensesPreviousValue(
                        valueOrZero(metric.getPreviousValue())
                );
                snapshot.setExpensesAbsoluteChange(
                        valueOrZero(metric.getAbsoluteChange())
                );
                snapshot.setExpensesPercentageChange(
                        valueOrZero(metric.getPercentageChange())
                );
            }

            case "SAVINGS" -> {
                snapshot.setSavingsCurrentValue(
                        valueOrZero(metric.getCurrentValue())
                );
                snapshot.setSavingsPreviousValue(
                        valueOrZero(metric.getPreviousValue())
                );
                snapshot.setSavingsAbsoluteChange(
                        valueOrZero(metric.getAbsoluteChange())
                );
                snapshot.setSavingsPercentageChange(
                        valueOrZero(metric.getPercentageChange())
                );
            }

            case "INVESTMENTS" -> {
                snapshot.setInvestmentsCurrentValue(
                        valueOrZero(metric.getCurrentValue())
                );
                snapshot.setInvestmentsPreviousValue(
                        valueOrZero(metric.getPreviousValue())
                );
                snapshot.setInvestmentsAbsoluteChange(
                        valueOrZero(metric.getAbsoluteChange())
                );
                snapshot.setInvestmentsPercentageChange(
                        valueOrZero(metric.getPercentageChange())
                );
            }

            case "NET_WORTH" -> {
                snapshot.setNetWorthCurrentValue(
                        valueOrZero(metric.getCurrentValue())
                );
                snapshot.setNetWorthPreviousValue(
                        valueOrZero(metric.getPreviousValue())
                );
                snapshot.setNetWorthAbsoluteChange(
                        valueOrZero(metric.getAbsoluteChange())
                );
                snapshot.setNetWorthPercentageChange(
                        valueOrZero(metric.getPercentageChange())
                );
            }

            default -> {
                // Ignore unsupported metric names.
            }
        }
    }

    private void persistAssetAllocation(
            FinancialReport report,
            AssetReportV2Response assets
    ) {
        if (assets == null ||
                assets.getAllocation() == null) {
            return;
        }

        for (AssetAllocationV2Response allocation :
                assets.getAllocation()) {

            if (allocation == null ||
                    allocation.getAssetType() == null) {
                continue;
            }

            ReportAssetAllocation snapshot =
                    new ReportAssetAllocation();

            snapshot.setReport(report);

            snapshot.setAssetType(
                    com.financeos.financeosbackend.asset.enums.AssetType
                            .valueOf(allocation.getAssetType())
            );

            snapshot.setAmount(
                    valueOrZero(
                            allocation.getAmount()
                    )
            );

            reportAssetAllocationRepository.save(
                    snapshot
            );
        }
    }

    private void persistChanges(
            FinancialReport report,
            List<ReportChangeInsightV2Response> changes
    ) {
        if (changes == null) {
            return;
        }

        for (ReportChangeInsightV2Response change :
                changes) {

            if (change == null) {
                continue;
            }

            ReportChange snapshot =
                    new ReportChange();

            snapshot.setReport(report);

            snapshot.setMetric(
                    change.getMetric()
            );

            snapshot.setPreviousValue(
                    valueOrZero(
                            change.getPreviousValue()
                    )
            );

            snapshot.setCurrentValue(
                    valueOrZero(
                            change.getCurrentValue()
                    )
            );

            snapshot.setChangeAmount(
                    valueOrZero(
                            change.getAbsoluteChange()
                    )
            );

            snapshot.setChangePercentage(
                    valueOrZero(
                            change.getPercentageChange()
                    )
            );

            snapshot.setDirection(
                    change.getDirection()
            );

            snapshot.setSignificance(
                    change.isSignificant()
                            ? "SIGNIFICANT"
                            : "NORMAL"
            );

            ReportChange savedChange =
                    reportChangeRepository.save(snapshot);

            persistChangeContributors(
                    savedChange,
                    change.getContributors()
            );
        }
    }

    private void persistChangeContributors(
            ReportChange reportChange,
            List<ReportChangeContributorV2Response> contributors
    ) {
        if (contributors == null) {
            return;
        }

        for (ReportChangeContributorV2Response contributor :
                contributors) {

            if (contributor == null) {
                continue;
            }

            ReportChangeContributor snapshot =
                    new ReportChangeContributor();

            snapshot.setReportChange(reportChange);

            snapshot.setName(
                    contributor.getName()
            );

            snapshot.setCurrentValue(
                    valueOrZero(
                            contributor.getCurrentValue()
                    )
            );

            snapshot.setPreviousValue(
                    valueOrZero(
                            contributor.getPreviousValue()
                    )
            );

            snapshot.setAbsoluteChange(
                    valueOrZero(
                            contributor.getAbsoluteChange()
                    )
            );

            snapshot.setPercentageChange(
                    valueOrZero(
                            contributor.getPercentageChange()
                    )
            );

            reportChangeContributorRepository.save(snapshot);
        }
    }

    private BigDecimal valueOrZero(
            BigDecimal value
    ) {
        return value == null
                ? BigDecimal.ZERO
                : value;
    }
}