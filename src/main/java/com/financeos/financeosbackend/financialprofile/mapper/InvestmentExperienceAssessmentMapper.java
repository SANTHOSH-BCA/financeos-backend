package com.financeos.financeosbackend.financialprofile.mapper;

import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentRequest;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.InvestmentExperienceAssessment;
import org.springframework.stereotype.Component;
import com.financeos.financeosbackend.financialprofile.enums.AssessmentSource;


@Component
public class InvestmentExperienceAssessmentMapper {

    public InvestmentExperienceAssessment toEntity(
            FinancialProfile financialProfile,
            InvestmentExperienceAssessmentRequest request
    ) {
        InvestmentExperienceAssessment assessment =
                new InvestmentExperienceAssessment();

        assessment.setFinancialProfile(financialProfile);
        assessment.setExperienceDuration(request.getExperienceDuration());
        assessment.setInstrumentTypes(request.getInstrumentTypes());
        assessment.setAssetClasses(request.getAssetClasses());
        assessment.setInvestmentConsistency(request.getInvestmentConsistency());
        assessment.setExposureLevel(request.getExposureLevel());
        assessment.setComplexityLevel(request.getComplexityLevel());
        assessment.setKnowledgeLevel(request.getKnowledgeLevel());
        assessment.setConfidenceLevel(request.getConfidenceLevel());

        return assessment;
    }

    public InvestmentExperienceAssessmentResponse toResponse(
            InvestmentExperienceAssessment assessment
    ) {
        return new InvestmentExperienceAssessmentResponse(
                assessment.getId(),
                assessment.getExperienceDuration(),
                assessment.getInstrumentTypes(),
                assessment.getAssetClasses(),
                assessment.getInvestmentConsistency(),
                assessment.getExposureLevel(),
                assessment.getComplexityLevel(),
                assessment.getKnowledgeLevel(),
                assessment.getConfidenceLevel(),
                assessment.getAssessmentSource(),
                assessment.getSystemClassification(),
                assessment.getUserClassification()
        );
    }
}