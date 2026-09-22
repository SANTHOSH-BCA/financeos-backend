package com.financeos.financeosbackend.reporting.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_report_change_contributors")
public class ReportChangeContributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_change_id", nullable = false)
    private ReportChange reportChange;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal previousValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal absoluteChange;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal percentageChange;

    public ReportChangeContributor() {
    }

    public Long getId() {
        return id;
    }

    public ReportChange getReportChange() {
        return reportChange;
    }

    public void setReportChange(ReportChange reportChange) {
        this.reportChange = reportChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    public BigDecimal getAbsoluteChange() {
        return absoluteChange;
    }

    public void setAbsoluteChange(BigDecimal absoluteChange) {
        this.absoluteChange = absoluteChange;
    }

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(BigDecimal percentageChange) {
        this.percentageChange = percentageChange;
    }
}