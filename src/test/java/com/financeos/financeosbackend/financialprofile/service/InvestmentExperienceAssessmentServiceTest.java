package com.financeos.financeosbackend.financialprofile.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentRequest;
import com.financeos.financeosbackend.financialprofile.dto.InvestmentExperienceAssessmentResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.InvestmentExperienceAssessment;
import com.financeos.financeosbackend.financialprofile.enums.AssessmentSource;
import com.financeos.financeosbackend.financialprofile.mapper.InvestmentExperienceAssessmentMapper;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;
import com.financeos.financeosbackend.financialprofile.repository.InvestmentExperienceAssessmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentExperienceAssessmentServiceTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private FinancialProfileRepository financialProfileRepository;

    @Mock
    private InvestmentExperienceAssessmentRepository assessmentRepository;

    @Mock
    private InvestmentExperienceAssessmentMapper mapper;

    @InjectMocks
    private InvestmentExperienceAssessmentService service;

    @Test
    void shouldSaveUserProvidedAssessment() {

        User user = new User();
        FinancialProfile profile = new FinancialProfile();

        InvestmentExperienceAssessmentRequest request =
                new InvestmentExperienceAssessmentRequest();

        request.setExperienceDuration("3-5 years");
        request.setInstrumentTypes("Mutual Funds, Stocks");
        request.setAssetClasses("Equity, Debt");
        request.setInvestmentConsistency("Regular");
        request.setExposureLevel("Moderate");
        request.setComplexityLevel("Intermediate");
        request.setKnowledgeLevel("Intermediate");
        request.setConfidenceLevel("High");

        InvestmentExperienceAssessment assessment =
                new InvestmentExperienceAssessment();

        InvestmentExperienceAssessmentResponse response =
                new InvestmentExperienceAssessmentResponse();

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(financialProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));

        when(assessmentRepository.findByFinancialProfile(profile))
                .thenReturn(Optional.of(assessment));

        when(assessmentRepository.save(assessment))
                .thenReturn(assessment);

        when(mapper.toResponse(assessment))
                .thenReturn(response);

        InvestmentExperienceAssessmentResponse result =
                service.saveAssessment(request);

        assertNotNull(result);
        verify(assessmentRepository).save(assessment);
        assertEquals(
                AssessmentSource.USER_PROVIDED,
                assessment.getAssessmentSource()
        );
        assertEquals(
                "3-5 years",
                assessment.getExperienceDuration()
        );
        assertEquals(
                "Mutual Funds, Stocks",
                assessment.getInstrumentTypes()
        );
    }
}