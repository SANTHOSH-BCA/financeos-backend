package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_profile_protection")
public class ProtectionContext {

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

    @Column(name = "family_responsibility")
    private String familyResponsibility;

    @Column(name = "protection_priority")
    private String protectionPriority;

    @Column(name = "protection_preference")
    private String protectionPreference;

    public ProtectionContext() {
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

    public String getFamilyResponsibility() {
        return familyResponsibility;
    }

    public void setFamilyResponsibility(String familyResponsibility) {
        this.familyResponsibility = familyResponsibility;
    }

    public String getProtectionPriority() {
        return protectionPriority;
    }

    public void setProtectionPriority(String protectionPriority) {
        this.protectionPriority = protectionPriority;
    }

    public String getProtectionPreference() {
        return protectionPreference;
    }

    public void setProtectionPreference(String protectionPreference) {
        this.protectionPreference = protectionPreference;
    }
}