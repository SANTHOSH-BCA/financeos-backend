package com.financeos.financeosbackend.reporting.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_report_liability_snapshots")
public class ReportLiabilitySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private FinancialReport report;

    @Column(nullable = false)
    private Long liabilityId;

    @Column(nullable = false, length = 150)
    private String liabilityName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalPaid;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal interestPaid;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal debtPayment;

    @Column(nullable = false, length = 50)
    private String status;

    public ReportLiabilitySnapshot() {
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

    public Long getLiabilityId() {
        return liabilityId;
    }

    public void setLiabilityId(Long liabilityId) {
        this.liabilityId = liabilityId;
    }

    public String getLiabilityName() {
        return liabilityName;
    }

    public void setLiabilityName(String liabilityName) {
        this.liabilityName = liabilityName;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public BigDecimal getDebtPayment() {
        return debtPayment;
    }

    public void setDebtPayment(BigDecimal debtPayment) {
        this.debtPayment = debtPayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}