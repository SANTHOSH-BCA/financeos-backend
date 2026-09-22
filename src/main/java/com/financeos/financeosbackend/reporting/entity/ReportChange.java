package com.financeos.financeosbackend.reporting.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_report_changes")
public class ReportChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private FinancialReport report;

    @Column(nullable = false, length = 100)
    private String metric;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal previousValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal changeAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal changePercentage;

    @Column(nullable = false, length = 30)
    private String direction;

    @Column(nullable = false, length = 30)
    private String significance;

    public ReportChange() {
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

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public BigDecimal getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(BigDecimal changePercentage) {
        this.changePercentage = changePercentage;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSignificance() {
        return significance;
    }

    public void setSignificance(String significance) {
        this.significance = significance;
    }
}