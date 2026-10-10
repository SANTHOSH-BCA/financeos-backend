package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.response.FinancialHealthProtectionResponse;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;
import com.financeos.financeosbackend.financialprofile.service.ProtectionContextService;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import com.financeos.financeosbackend.insurance.service.InsurancePolicyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialHealthProtectionServiceTest {

    @Mock
    private InsurancePolicyService insurancePolicyService;

    @Mock
    private ProtectionContextService protectionContextService;

    @InjectMocks
    private FinancialHealthProtectionService financialHealthProtectionService;

    @Test
    void shouldReturnRecordedWhenAllCategoriesAreRecorded() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "RECORDED", 1, "Health policy recorded."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded."),
                new ProtectionAnalysisItem("VEHICLE", "RECORDED", 1, "Vehicle policy recorded.")
        ));
        when(protectionContextService.findCurrentUserContext()).thenReturn(Optional.empty());

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertEquals("RECORDED", response.healthProtectionStatus());
        assertEquals("RECORDED", response.lifeProtectionStatus());
        assertEquals("RECORDED", response.vehicleProtectionStatus());
        assertEquals("RECORDED", response.overallProtectionStatus());
        assertTrue(response.explanation().contains("HEALTH: Health policy recorded."));
        assertNull(response.familyResponsibility());
        assertNull(response.protectionPriority());
        assertNull(response.protectionPreference());

        verify(insurancePolicyService).getProtectionAnalysis();
        verify(protectionContextService).findCurrentUserContext();
    }

    @Test
    void shouldRecommendReviewWhenProtectionGapExists() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "GAP_IDENTIFIED", 0, "No health policy recorded."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded."),
                new ProtectionAnalysisItem("VEHICLE", "RECORDED", 1, "Vehicle policy recorded.")
        ));
        when(protectionContextService.findCurrentUserContext()).thenReturn(Optional.empty());

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertEquals("GAP_IDENTIFIED", response.healthProtectionStatus());
        assertEquals("REVIEW_RECOMMENDED", response.overallProtectionStatus());
    }

    @Test
    void shouldRequireInformationReviewWhenPolicyDataIsIncomplete() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "INCOMPLETE_DATA", 1, "Review health policy data."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded."),
                new ProtectionAnalysisItem("VEHICLE", "RECORDED", 1, "Vehicle policy recorded.")
        ));
        when(protectionContextService.findCurrentUserContext()).thenReturn(Optional.empty());

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertEquals("INCOMPLETE_DATA", response.healthProtectionStatus());
        assertEquals("INFORMATION_REVIEW_REQUIRED", response.overallProtectionStatus());
    }

    @Test
    void shouldMarkMissingCategoryAsNotAssessed() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "RECORDED", 1, "Health policy recorded."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded.")
        ));
        when(protectionContextService.findCurrentUserContext()).thenReturn(Optional.empty());

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertEquals("NOT_ASSESSED", response.vehicleProtectionStatus());
        assertEquals("INFORMATION_REVIEW_REQUIRED", response.overallProtectionStatus());
    }

    @Test
    void shouldIncludeFinancialProfileProtectionContext() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "RECORDED", 1, "Health policy recorded."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded."),
                new ProtectionAnalysisItem("VEHICLE", "RECORDED", 1, "Vehicle policy recorded.")
        ));

        when(protectionContextService.findCurrentUserContext()).thenReturn(
                Optional.of(new ProtectionContextResponse(
                        1L,
                        "YES",
                        "LIFE_INSURANCE",
                        "TERM_INSURANCE"
                ))
        );

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertEquals("YES", response.familyResponsibility());
        assertEquals("LIFE_INSURANCE", response.protectionPriority());
        assertEquals("TERM_INSURANCE", response.protectionPreference());
        assertEquals("RECORDED", response.overallProtectionStatus());
    }

    @Test
    void shouldHandleMissingFinancialProfileProtectionContext() {
        when(insurancePolicyService.getProtectionAnalysis()).thenReturn(List.of(
                new ProtectionAnalysisItem("HEALTH", "RECORDED", 1, "Health policy recorded."),
                new ProtectionAnalysisItem("LIFE", "RECORDED", 1, "Life policy recorded."),
                new ProtectionAnalysisItem("VEHICLE", "RECORDED", 1, "Vehicle policy recorded.")
        ));
        when(protectionContextService.findCurrentUserContext()).thenReturn(Optional.empty());

        FinancialHealthProtectionResponse response =
                financialHealthProtectionService.calculateProtectionHealth();

        assertNull(response.familyResponsibility());
        assertNull(response.protectionPriority());
        assertNull(response.protectionPreference());
        assertEquals("RECORDED", response.overallProtectionStatus());
    }
}