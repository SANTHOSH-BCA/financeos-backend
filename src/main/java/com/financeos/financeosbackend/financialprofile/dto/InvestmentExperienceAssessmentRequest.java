package com.financeos.financeosbackend.financialprofile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentExperienceAssessmentRequest {

    private String experienceDuration;

    private String instrumentTypes;

    private String assetClasses;

    private String investmentConsistency;

    private String exposureLevel;

    private String complexityLevel;

    private String knowledgeLevel;

    private String confidenceLevel;
}