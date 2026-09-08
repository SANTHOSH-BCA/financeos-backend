package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentRequest;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.InvestmentExperienceAssessment;
import com.financeos.financeosbackend.financialprofile.mapper.InvestmentExperienceAssessmentMapper;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.InvestmentExperienceAssessmentRepository;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.financialprofile.enums.AssessmentSource;

@Service
public class InvestmentExperienceAssessmentService {

    private final CurrentUserService currentUserService;
    private final FinancialProfileRepository financialProfileRepository;
    private final InvestmentExperienceAssessmentRepository assessmentRepository;
    private final InvestmentExperienceAssessmentMapper mapper;

    public InvestmentExperienceAssessmentService(
            CurrentUserService currentUserService,
            FinancialProfileRepository financialProfileRepository,
            InvestmentExperienceAssessmentRepository assessmentRepository,
            InvestmentExperienceAssessmentMapper mapper
    ) {
        this.currentUserService = currentUserService;
        this.financialProfileRepository = financialProfileRepository;
        this.assessmentRepository = assessmentRepository;
        this.mapper = mapper;
    }

    public InvestmentExperienceAssessmentResponse saveAssessment(
            InvestmentExperienceAssessmentRequest request
    ) {
        FinancialProfile profile =
                financialProfileRepository.findByUser(currentUserService.getCurrentUser())
                        .orElseThrow(() ->
                                new RuntimeException("Financial profile not found"));

        InvestmentExperienceAssessment assessment =
                assessmentRepository.findByFinancialProfile(profile)
                        .orElseGet(() -> mapper.toEntity(profile, request));

        assessment.setExperienceDuration(request.getExperienceDuration());
        assessment.setInstrumentTypes(request.getInstrumentTypes());
        assessment.setAssetClasses(request.getAssetClasses());
        assessment.setInvestmentConsistency(request.getInvestmentConsistency());
        assessment.setExposureLevel(request.getExposureLevel());
        assessment.setComplexityLevel(request.getComplexityLevel());
        assessment.setKnowledgeLevel(request.getKnowledgeLevel());
        assessment.setConfidenceLevel(request.getConfidenceLevel());
        assessment.setAssessmentSource(AssessmentSource.USER_PROVIDED);

        return mapper.toResponse(assessmentRepository.save(assessment));
    }

    public InvestmentExperienceAssessmentResponse getAssessment() {
        FinancialProfile profile =
                financialProfileRepository.findByUser(currentUserService.getCurrentUser())
                        .orElseThrow(() ->
                                new RuntimeException("Financial profile not found"));

        InvestmentExperienceAssessment assessment =
                assessmentRepository.findByFinancialProfile(profile)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Investment experience assessment not found"
                                ));

        return mapper.toResponse(assessment);
    }
}