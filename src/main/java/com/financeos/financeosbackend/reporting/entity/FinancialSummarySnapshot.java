package com.financeos.financeosbackend.reporting.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_report_summary_snapshots")
public class FinancialSummarySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    private FinancialReport report;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalIncome;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal recurringIncome;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal irregularIncome;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalExpense;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal helpAmounts;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal convertedHelpExpenses;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal inflows;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal outflows;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal debtPaymentOutflows;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal netCashFlow;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal savings;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal savingsRate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal recognizedAssets;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal liquidAssets;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal recognizedLiabilities;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal netWorth;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal investmentValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal investmentProfitLoss;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal investmentReturn;

    @Column(nullable = true)
    private java.time.LocalDate investmentValuationDate;

    @Column(nullable = false)
    private Long totalGoals;

    @Column(nullable = false)
    private Long completedGoals;

    @Column(nullable = false)
    private Long atRiskGoals;

    @Column(nullable = false)
    private Long onTrackGoals;

    @Column(nullable = true, length = 50)
    private String financialHealthCashFlow;

    @Column(nullable = true, length = 50)
    private String financialHealthDebt;

    @Column(nullable = true, length = 50)
    private String financialHealthSavings;

    @Column(nullable = true, length = 50)
    private String financialHealthInvestments;

    @Column(nullable = true, length = 50)
    private String financialHealthGoals;

    @Column(nullable = true, length = 50)
    private String financialHealthWealth;

    @Column(nullable = false, length = 50)
    private String financialHealthOverallStatus;

    public FinancialSummarySnapshot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinancialReport getReport() {
        return report;
    }

    public void setReport(FinancialReport report) {
        this.report = report;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getRecurringIncome() {
        return recurringIncome;
    }

    public void setRecurringIncome(BigDecimal recurringIncome) {
        this.recurringIncome = recurringIncome;
    }

    public BigDecimal getIrregularIncome() {
        return irregularIncome;
    }

    public void setIrregularIncome(BigDecimal irregularIncome) {
        this.irregularIncome = irregularIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getHelpAmounts() {
        return helpAmounts;
    }

    public void setHelpAmounts(BigDecimal helpAmounts) {
        this.helpAmounts = helpAmounts;
    }

    public BigDecimal getConvertedHelpExpenses() {
        return convertedHelpExpenses;
    }

    public void setConvertedHelpExpenses(BigDecimal convertedHelpExpenses) {
        this.convertedHelpExpenses = convertedHelpExpenses;
    }

    public BigDecimal getInflows() {
        return inflows;
    }

    public void setInflows(BigDecimal inflows) {
        this.inflows = inflows;
    }

    public BigDecimal getOutflows() {
        return outflows;
    }

    public void setOutflows(BigDecimal outflows) {
        this.outflows = outflows;
    }

    public BigDecimal getDebtPaymentOutflows() {
        return debtPaymentOutflows;
    }

    public void setDebtPaymentOutflows(BigDecimal debtPaymentOutflows) {
        this.debtPaymentOutflows = debtPaymentOutflows;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }

    public BigDecimal getRecognizedAssets() {
        return recognizedAssets;
    }

    public void setRecognizedAssets(BigDecimal recognizedAssets) {
        this.recognizedAssets = recognizedAssets;
    }

    public BigDecimal getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(BigDecimal liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(BigDecimal recognizedLiabilities) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getInvestmentValue() {
        return investmentValue;
    }

    public void setInvestmentValue(BigDecimal investmentValue) {
        this.investmentValue = investmentValue;
    }

    public BigDecimal getInvestmentProfitLoss() {
        return investmentProfitLoss;
    }

    public void setInvestmentProfitLoss(BigDecimal investmentProfitLoss) {
        this.investmentProfitLoss = investmentProfitLoss;
    }

    public BigDecimal getInvestmentReturn() {
        return investmentReturn;
    }

    public void setInvestmentReturn(BigDecimal investmentReturn) {
        this.investmentReturn = investmentReturn;
    }

    public java.time.LocalDate getInvestmentValuationDate() {
        return investmentValuationDate;
    }

    public void setInvestmentValuationDate(
            java.time.LocalDate investmentValuationDate) {
        this.investmentValuationDate = investmentValuationDate;
    }

    public Long getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Long totalGoals) {
        this.totalGoals = totalGoals;
    }

    public Long getCompletedGoals() {
        return completedGoals;
    }

    public void setCompletedGoals(Long completedGoals) {
        this.completedGoals = completedGoals;
    }

    public Long getAtRiskGoals() {
        return atRiskGoals;
    }

    public void setAtRiskGoals(Long atRiskGoals) {
        this.atRiskGoals = atRiskGoals;
    }

    public Long getOnTrackGoals() {
        return onTrackGoals;
    }

    public void setOnTrackGoals(Long onTrackGoals) {
        this.onTrackGoals = onTrackGoals;
    }

    public String getFinancialHealthCashFlow() {
        return financialHealthCashFlow;
    }

    public void setFinancialHealthCashFlow(String financialHealthCashFlow) {
        this.financialHealthCashFlow = financialHealthCashFlow;
    }

    public String getFinancialHealthDebt() {
        return financialHealthDebt;
    }

    public void setFinancialHealthDebt(String financialHealthDebt) {
        this.financialHealthDebt = financialHealthDebt;
    }

    public String getFinancialHealthSavings() {
        return financialHealthSavings;
    }

    public void setFinancialHealthSavings(String financialHealthSavings) {
        this.financialHealthSavings = financialHealthSavings;
    }

    public String getFinancialHealthInvestments() {
        return financialHealthInvestments;
    }

    public void setFinancialHealthInvestments(String financialHealthInvestments) {
        this.financialHealthInvestments = financialHealthInvestments;
    }

    public String getFinancialHealthGoals() {
        return financialHealthGoals;
    }

    public void setFinancialHealthGoals(String financialHealthGoals) {
        this.financialHealthGoals = financialHealthGoals;
    }

    public String getFinancialHealthWealth() {
        return financialHealthWealth;
    }

    public void setFinancialHealthWealth(String financialHealthWealth) {
        this.financialHealthWealth = financialHealthWealth;
    }

    public String getFinancialHealthOverallStatus() {
        return financialHealthOverallStatus;
    }

    public void setFinancialHealthOverallStatus(String financialHealthOverallStatus) {
        this.financialHealthOverallStatus = financialHealthOverallStatus;
    }
}