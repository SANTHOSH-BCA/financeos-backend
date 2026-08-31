package com.financeos.financeosbackend.asset.dto;

import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssetResponse {

    private Long id;
    private String assetName;
    private AssetType assetType;
    private BigDecimal totalValue;
    private OwnershipType ownershipType;
    private BigDecimal ownershipPercentage;
    private BigDecimal recognizedValue;
    private boolean includedInNetWorth;
    private LocalDate valuationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AssetResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public BigDecimal getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(BigDecimal ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }

    public BigDecimal getRecognizedValue() {
        return recognizedValue;
    }

    public void setRecognizedValue(BigDecimal recognizedValue) {
        this.recognizedValue = recognizedValue;
    }

    public boolean isIncludedInNetWorth() {
        return includedInNetWorth;
    }

    public void setIncludedInNetWorth(boolean includedInNetWorth) {
        this.includedInNetWorth = includedInNetWorth;
    }

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}