package com.financeos.financeosbackend.reporting.entity;

import com.financeos.financeosbackend.asset.enums.AssetType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_report_asset_allocations")
public class ReportAssetAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private FinancialReport report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AssetType assetType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    public ReportAssetAllocation() {
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

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}