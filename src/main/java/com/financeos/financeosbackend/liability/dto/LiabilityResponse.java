package com.financeos.financeosbackend.liability.dto;

import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LiabilityResponse {

    private Long id;

    private String liabilityName;

    private LiabilityType liabilityType;

    private BigDecimal outstandingAmount;

    private ResponsibilityType responsibilityType;

    private BigDecimal responsibilityPercentage;

    private BigDecimal recognizedLiability;

    private boolean includedInNetWorth;

    private LocalDate valuationDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public LiabilityResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLiabilityName() {
        return liabilityName;
    }

    public void setLiabilityName(String liabilityName) {
        this.liabilityName = liabilityName;
    }

    public LiabilityType getLiabilityType() {
        return liabilityType;
    }

    public void setLiabilityType(LiabilityType liabilityType) {
        this.liabilityType = liabilityType;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public ResponsibilityType getResponsibilityType() {
        return responsibilityType;
    }

    public void setResponsibilityType(ResponsibilityType responsibilityType) {
        this.responsibilityType = responsibilityType;
    }

    public BigDecimal getResponsibilityPercentage() {
        return responsibilityPercentage;
    }

    public void setResponsibilityPercentage(BigDecimal responsibilityPercentage) {
        this.responsibilityPercentage = responsibilityPercentage;
    }

    public BigDecimal getRecognizedLiability() {
        return recognizedLiability;
    }

    public void setRecognizedLiability(BigDecimal recognizedLiability) {
        this.recognizedLiability = recognizedLiability;
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