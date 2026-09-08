package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.financeos.financeosbackend.financialprofile.enums.AssessmentSource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentExperienceAssessmentResponse {

    private Long id;
    private String experienceDuration;
    private String instrumentTypes;
    private String assetClasses;
    private String investmentConsistency;
    private String exposureLevel;
    private String complexityLevel;
    private String knowledgeLevel;
    private String confidenceLevel;
    private AssessmentSource assessmentSource;
    private String systemClassification;
    private String userClassification;
}