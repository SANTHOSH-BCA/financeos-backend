package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_profile_responsibility")
public class FinancialResponsibilityContext {

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

    @Column(name = "responsibility_level")
    private String responsibilityLevel;

    @Column(name = "dependents_count")
    private Integer dependentsCount;

    @Column(name = "dependent_context", length = 1000)
    private String dependentContext;

    public FinancialResponsibilityContext() {
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

    public String getResponsibilityLevel() {
        return responsibilityLevel;
    }

    public void setResponsibilityLevel(String responsibilityLevel) {
        this.responsibilityLevel = responsibilityLevel;
    }

    public Integer getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(Integer dependentsCount) {
        this.dependentsCount = dependentsCount;
    }

    public String getDependentContext() {
        return dependentContext;
    }

    public void setDependentContext(String dependentContext) {
        this.dependentContext = dependentContext;
    }
}