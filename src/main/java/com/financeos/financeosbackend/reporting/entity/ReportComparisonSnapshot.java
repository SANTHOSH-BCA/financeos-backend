package com.financeos.financeosbackend.reporting.entity;

import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_report_comparison_snapshots")
public class ReportComparisonSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    private FinancialReport report;

    @Column(nullable = false)
    private boolean comparisonAvailable;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ReportPeriodType previousPeriodType;

    private LocalDate previousStartDate;

    private LocalDate previousEndDate;

    @Column(precision = 19, scale = 2)
    private BigDecimal incomeCurrentValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal incomePreviousValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal incomeAbsoluteChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal incomePercentageChange;

    @Column(precision = 19, scale = 2)
    private BigDecimal expensesCurrentValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal expensesPreviousValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal expensesAbsoluteChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal expensesPercentageChange;

    @Column(precision = 19, scale = 2)
    private BigDecimal savingsCurrentValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal savingsPreviousValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal savingsAbsoluteChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal savingsPercentageChange;

    @Column(precision = 19, scale = 2)
    private BigDecimal investmentsCurrentValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal investmentsPreviousValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal investmentsAbsoluteChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal investmentsPercentageChange;

    @Column(precision = 19, scale = 2)
    private BigDecimal netWorthCurrentValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal netWorthPreviousValue;

    @Column(precision = 19, scale = 2)
    private BigDecimal netWorthAbsoluteChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal netWorthPercentageChange;

    public ReportComparisonSnapshot() {
    }

    public Long getId() {
        return id;
    }

    public FinancialReport getReport() {
        return report;
    }

    public void setReport(FinancialReport report) {
        this.report = report;
    }

    public boolean isComparisonAvailable() {
        return comparisonAvailable;
    }

    public void setComparisonAvailable(boolean comparisonAvailable) {
        this.comparisonAvailable = comparisonAvailable;
    }

    public ReportPeriodType getPreviousPeriodType() {
        return previousPeriodType;
    }

    public void setPreviousPeriodType(ReportPeriodType previousPeriodType) {
        this.previousPeriodType = previousPeriodType;
    }

    public LocalDate getPreviousStartDate() {
        return previousStartDate;
    }

    public void setPreviousStartDate(LocalDate previousStartDate) {
        this.previousStartDate = previousStartDate;
    }

    public LocalDate getPreviousEndDate() {
        return previousEndDate;
    }

    public void setPreviousEndDate(LocalDate previousEndDate) {
        this.previousEndDate = previousEndDate;
    }

    public BigDecimal getIncomeCurrentValue() {
        return incomeCurrentValue;
    }

    public void setIncomeCurrentValue(BigDecimal incomeCurrentValue) {
        this.incomeCurrentValue = incomeCurrentValue;
    }

    public BigDecimal getIncomePreviousValue() {
        return incomePreviousValue;
    }

    public void setIncomePreviousValue(BigDecimal incomePreviousValue) {
        this.incomePreviousValue = incomePreviousValue;
    }

    public BigDecimal getIncomeAbsoluteChange() {
        return incomeAbsoluteChange;
    }

    public void setIncomeAbsoluteChange(BigDecimal incomeAbsoluteChange) {
        this.incomeAbsoluteChange = incomeAbsoluteChange;
    }

    public BigDecimal getIncomePercentageChange() {
        return incomePercentageChange;
    }

    public void setIncomePercentageChange(BigDecimal incomePercentageChange) {
        this.incomePercentageChange = incomePercentageChange;
    }

    public BigDecimal getExpensesCurrentValue() {
        return expensesCurrentValue;
    }

    public void setExpensesCurrentValue(BigDecimal expensesCurrentValue) {
        this.expensesCurrentValue = expensesCurrentValue;
    }

    public BigDecimal getExpensesPreviousValue() {
        return expensesPreviousValue;
    }

    public void setExpensesPreviousValue(BigDecimal expensesPreviousValue) {
        this.expensesPreviousValue = expensesPreviousValue;
    }

    public BigDecimal getExpensesAbsoluteChange() {
        return expensesAbsoluteChange;
    }

    public void setExpensesAbsoluteChange(BigDecimal expensesAbsoluteChange) {
        this.expensesAbsoluteChange = expensesAbsoluteChange;
    }

    public BigDecimal getExpensesPercentageChange() {
        return expensesPercentageChange;
    }

    public void setExpensesPercentageChange(BigDecimal expensesPercentageChange) {
        this.expensesPercentageChange = expensesPercentageChange;
    }

    public BigDecimal getSavingsCurrentValue() {
        return savingsCurrentValue;
    }

    public void setSavingsCurrentValue(BigDecimal savingsCurrentValue) {
        this.savingsCurrentValue = savingsCurrentValue;
    }

    public BigDecimal getSavingsPreviousValue() {
        return savingsPreviousValue;
    }

    public void setSavingsPreviousValue(BigDecimal savingsPreviousValue) {
        this.savingsPreviousValue = savingsPreviousValue;
    }

    public BigDecimal getSavingsAbsoluteChange() {
        return savingsAbsoluteChange;
    }

    public void setSavingsAbsoluteChange(BigDecimal savingsAbsoluteChange) {
        this.savingsAbsoluteChange = savingsAbsoluteChange;
    }

    public BigDecimal getSavingsPercentageChange() {
        return savingsPercentageChange;
    }

    public void setSavingsPercentageChange(BigDecimal savingsPercentageChange) {
        this.savingsPercentageChange = savingsPercentageChange;
    }

    public BigDecimal getInvestmentsCurrentValue() {
        return investmentsCurrentValue;
    }

    public void setInvestmentsCurrentValue(BigDecimal investmentsCurrentValue) {
        this.investmentsCurrentValue = investmentsCurrentValue;
    }

    public BigDecimal getInvestmentsPreviousValue() {
        return investmentsPreviousValue;
    }

    public void setInvestmentsPreviousValue(BigDecimal investmentsPreviousValue) {
        this.investmentsPreviousValue = investmentsPreviousValue;
    }

    public BigDecimal getInvestmentsAbsoluteChange() {
        return investmentsAbsoluteChange;
    }

    public void setInvestmentsAbsoluteChange(BigDecimal investmentsAbsoluteChange) {
        this.investmentsAbsoluteChange = investmentsAbsoluteChange;
    }

    public BigDecimal getInvestmentsPercentageChange() {
        return investmentsPercentageChange;
    }

    public void setInvestmentsPercentageChange(BigDecimal investmentsPercentageChange) {
        this.investmentsPercentageChange = investmentsPercentageChange;
    }

    public BigDecimal getNetWorthCurrentValue() {
        return netWorthCurrentValue;
    }

    public void setNetWorthCurrentValue(BigDecimal netWorthCurrentValue) {
        this.netWorthCurrentValue = netWorthCurrentValue;
    }

    public BigDecimal getNetWorthPreviousValue() {
        return netWorthPreviousValue;
    }

    public void setNetWorthPreviousValue(BigDecimal netWorthPreviousValue) {
        this.netWorthPreviousValue = netWorthPreviousValue;
    }

    public BigDecimal getNetWorthAbsoluteChange() {
        return netWorthAbsoluteChange;
    }

    public void setNetWorthAbsoluteChange(BigDecimal netWorthAbsoluteChange) {
        this.netWorthAbsoluteChange = netWorthAbsoluteChange;
    }

    public BigDecimal getNetWorthPercentageChange() {
        return netWorthPercentageChange;
    }

    public void setNetWorthPercentageChange(BigDecimal netWorthPercentageChange) {
        this.netWorthPercentageChange = netWorthPercentageChange;
    }
}