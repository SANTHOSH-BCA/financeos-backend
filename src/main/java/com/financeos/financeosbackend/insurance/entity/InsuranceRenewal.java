package com.financeos.financeosbackend.insurance.entity;

import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_renewals")
public class InsuranceRenewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(name = "previous_start_date", nullable = false)
    private LocalDate previousStartDate;

    @Column(name = "previous_expiry_date")
    private LocalDate previousExpiryDate;

    @Column(name = "new_start_date", nullable = false)
    private LocalDate newStartDate;

    @Column(name = "new_expiry_date")
    private LocalDate newExpiryDate;

    @Column(name = "previous_coverage_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal previousCoverageAmount;

    @Column(name = "new_coverage_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal newCoverageAmount;

    @Column(name = "previous_premium_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal previousPremiumAmount;

    @Column(name = "new_premium_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal newPremiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_premium_frequency", nullable = false, length = 30)
    private PremiumFrequency previousPremiumFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_premium_frequency", nullable = false, length = 30)
    private PremiumFrequency newPremiumFrequency;

    @Column(name = "renewed_at", nullable = false, updatable = false)
    private LocalDateTime renewedAt;

    public InsuranceRenewal() {
    }

    @PrePersist
    protected void onCreate() {
        if (renewedAt == null) {
            renewedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public LocalDate getPreviousStartDate() {
        return previousStartDate;
    }

    public void setPreviousStartDate(LocalDate previousStartDate) {
        this.previousStartDate = previousStartDate;
    }

    public LocalDate getPreviousExpiryDate() {
        return previousExpiryDate;
    }

    public void setPreviousExpiryDate(LocalDate previousExpiryDate) {
        this.previousExpiryDate = previousExpiryDate;
    }

    public LocalDate getNewStartDate() {
        return newStartDate;
    }

    public void setNewStartDate(LocalDate newStartDate) {
        this.newStartDate = newStartDate;
    }

    public LocalDate getNewExpiryDate() {
        return newExpiryDate;
    }

    public void setNewExpiryDate(LocalDate newExpiryDate) {
        this.newExpiryDate = newExpiryDate;
    }

    public BigDecimal getPreviousCoverageAmount() {
        return previousCoverageAmount;
    }

    public void setPreviousCoverageAmount(BigDecimal previousCoverageAmount) {
        this.previousCoverageAmount = previousCoverageAmount;
    }

    public BigDecimal getNewCoverageAmount() {
        return newCoverageAmount;
    }

    public void setNewCoverageAmount(BigDecimal newCoverageAmount) {
        this.newCoverageAmount = newCoverageAmount;
    }

    public BigDecimal getPreviousPremiumAmount() {
        return previousPremiumAmount;
    }

    public void setPreviousPremiumAmount(BigDecimal previousPremiumAmount) {
        this.previousPremiumAmount = previousPremiumAmount;
    }

    public BigDecimal getNewPremiumAmount() {
        return newPremiumAmount;
    }

    public void setNewPremiumAmount(BigDecimal newPremiumAmount) {
        this.newPremiumAmount = newPremiumAmount;
    }

    public PremiumFrequency getPreviousPremiumFrequency() {
        return previousPremiumFrequency;
    }

    public void setPreviousPremiumFrequency(PremiumFrequency previousPremiumFrequency) {
        this.previousPremiumFrequency = previousPremiumFrequency;
    }

    public PremiumFrequency getNewPremiumFrequency() {
        return newPremiumFrequency;
    }

    public void setNewPremiumFrequency(PremiumFrequency newPremiumFrequency) {
        this.newPremiumFrequency = newPremiumFrequency;
    }

    public LocalDateTime getRenewedAt() {
        return renewedAt;
    }
}