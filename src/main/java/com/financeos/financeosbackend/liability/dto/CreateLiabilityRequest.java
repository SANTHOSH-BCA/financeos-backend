package com.financeos.financeosbackend.liability.dto;

import com.financeos.financeosbackend.liability.enums.InterestType;
import com.financeos.financeosbackend.liability.enums.LiabilityType;
import com.financeos.financeosbackend.liability.enums.PaymentFrequency;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateLiabilityRequest {

    @NotBlank(message = "Liability name is required")
    private String liabilityName;

    @NotNull(message = "Liability type is required")
    private LiabilityType liabilityType;

    @NotNull(message = "Outstanding amount is required")
    @Positive(message = "Outstanding amount must be greater than zero")
    private BigDecimal outstandingAmount;

    @Positive(message = "Original amount must be greater than zero")
    private BigDecimal originalAmount;

    private String lender;

    @DecimalMin(value = "0.00", message = "Interest rate cannot be negative")
    private BigDecimal interestRate;

    private InterestType interestType;

    @Positive(message = "Payment amount must be greater than zero")
    private BigDecimal paymentAmount;

    private PaymentFrequency paymentFrequency;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextPaymentDate;

    @NotNull(message = "Responsibility type is required")
    private ResponsibilityType responsibilityType;


    @DecimalMin(value = "0.00", message = "Responsibility percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Responsibility percentage cannot exceed 100")
    private BigDecimal responsibilityPercentage;

    @NotNull(message = "Valuation date is required")
    private LocalDate valuationDate;

    public CreateLiabilityRequest() {
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

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public InterestType getInterestType() {
        return interestType;
    }

    public void setInterestType(InterestType interestType) {
        this.interestType = interestType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentFrequency getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
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

    public LocalDate getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }
}