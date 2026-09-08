package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_profile_emergency_fund")
public class EmergencyFundContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "financial_profile_id",
            nullable = false,
            unique = true
    )
    private FinancialProfile financialProfile;

    @Column(name = "preferred_coverage")
    private String preferredCoverage;

    @Column(name = "safety_level")
    private String safetyLevel;

    public EmergencyFundContext() {
    }

    public Long getId() {
        return id;
    }

    public FinancialProfile getFinancialProfile() {
        return financialProfile;
    }

    public void setFinancialProfile(FinancialProfile financialProfile) {
        this.financialProfile = financialProfile;
    }

    public String getPreferredCoverage() {
        return preferredCoverage;
    }

    public void setPreferredCoverage(String preferredCoverage) {
        this.preferredCoverage = preferredCoverage;
    }

    public String getSafetyLevel() {
        return safetyLevel;
    }

    public void setSafetyLevel(String safetyLevel) {
        this.safetyLevel = safetyLevel;
    }
}