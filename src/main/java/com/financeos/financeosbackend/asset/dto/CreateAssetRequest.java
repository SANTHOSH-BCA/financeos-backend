package com.financeos.financeosbackend.asset.dto;

import com.financeos.financeosbackend.asset.enums.AssetType;
import com.financeos.financeosbackend.asset.enums.OwnershipType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateAssetRequest {

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Asset type is required")
    private AssetType assetType;

    @NotNull(message = "Total value is required")
    @Positive(message = "Total value must be greater than zero")
    private BigDecimal totalValue;

    @NotNull(message = "Ownership type is required")
    private OwnershipType ownershipType;

    @DecimalMin(value = "0.01", message = "Ownership percentage must be greater than zero")
    @DecimalMax(value = "100.00", message = "Ownership percentage cannot exceed 100")
    private BigDecimal ownershipPercentage;

    @NotNull(message = "Valuation date is required")
    private LocalDate valuationDate;

    public CreateAssetRequest() {
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

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }
}