package com.financeos.financeosbackend.insurance.entity;

import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "policy_name", nullable = false, length = 150)
    private String policyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type", nullable = false, length = 30)
    private InsuranceType insuranceType;

    @Column(name = "provider", nullable = false, length = 150)
    private String provider;

    @Column(name = "policy_number", length = 100)
    private String policyNumber;

    @Column(name = "coverage_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "premium_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal premiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_frequency", nullable = false, length = 30)
    private PremiumFrequency premiumFrequency;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "next_premium_due_date")
    private LocalDate nextPremiumDueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private InsuranceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_payment_status", nullable = false, length = 30)
    private PremiumPaymentStatus premiumPaymentStatus;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public InsurancePolicy() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = InsuranceStatus.PENDING;
        }

        if (premiumPaymentStatus == null) {
            premiumPaymentStatus = PremiumPaymentStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public PremiumFrequency getPremiumFrequency() {
        return premiumFrequency;
    }

    public void setPremiumFrequency(PremiumFrequency premiumFrequency) {
        this.premiumFrequency = premiumFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getNextPremiumDueDate() {
        return nextPremiumDueDate;
    }

    public void setNextPremiumDueDate(LocalDate nextPremiumDueDate) {
        this.nextPremiumDueDate = nextPremiumDueDate;
    }

    public InsuranceStatus getStatus() {
        return status;
    }

    public void setStatus(InsuranceStatus status) {
        this.status = status;
    }

    public PremiumPaymentStatus getPremiumPaymentStatus() {
        return premiumPaymentStatus;
    }

    public void setPremiumPaymentStatus(PremiumPaymentStatus premiumPaymentStatus) {
        this.premiumPaymentStatus = premiumPaymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
