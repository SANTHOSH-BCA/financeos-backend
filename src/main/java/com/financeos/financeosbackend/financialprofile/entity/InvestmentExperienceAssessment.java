package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "investment_experience_assessments")
public class InvestmentExperienceAssessment {

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

    @Column(name = "experience_duration")
    private String experienceDuration;

    @Column(name = "instrument_types")
    private String instrumentTypes;

    @Column(name = "asset_classes")
    private String assetClasses;

    @Column(name = "investment_consistency")
    private String investmentConsistency;

    @Column(name = "exposure_level")
    private String exposureLevel;

    @Column(name = "complexity_level")
    private String complexityLevel;

    @Column(name = "knowledge_level")
    private String knowledgeLevel;

    @Column(name = "confidence_level")
    private String confidenceLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_source")
    private com.financeos.financeosbackend.financialprofile.enums.AssessmentSource assessmentSource;

    @Column(name = "system_classification")
    private String systemClassification;

    @Column(name = "user_classification")
    private String userClassification;

    public InvestmentExperienceAssessment() {
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

    public String getExperienceDuration() {
        return experienceDuration;
    }

    public void setExperienceDuration(String experienceDuration) {
        this.experienceDuration = experienceDuration;
    }

    public String getInstrumentTypes() {
        return instrumentTypes;
    }

    public void setInstrumentTypes(String instrumentTypes) {
        this.instrumentTypes = instrumentTypes;
    }

    public String getAssetClasses() {
        return assetClasses;
    }

    public void setAssetClasses(String assetClasses) {
        this.assetClasses = assetClasses;
    }

    public String getInvestmentConsistency() {
        return investmentConsistency;
    }

    public void setInvestmentConsistency(String investmentConsistency) {
        this.investmentConsistency = investmentConsistency;
    }

    public String getExposureLevel() {
        return exposureLevel;
    }

    public void setExposureLevel(String exposureLevel) {
        this.exposureLevel = exposureLevel;
    }

    public String getComplexityLevel() {
        return complexityLevel;
    }

    public void setComplexityLevel(String complexityLevel) {
        this.complexityLevel = complexityLevel;
    }

    public String getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(String knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public com.financeos.financeosbackend.financialprofile.enums.AssessmentSource getAssessmentSource() {
        return assessmentSource;
    }

    public void setAssessmentSource(
            com.financeos.financeosbackend.financialprofile.enums.AssessmentSource assessmentSource
    ) {
        this.assessmentSource = assessmentSource;
    }

    public String getSystemClassification() {
        return systemClassification;
    }

    public void setSystemClassification(String systemClassification) {
        this.systemClassification = systemClassification;
    }

    public String getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(String userClassification) {
        this.userClassification = userClassification;
    }
}