package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.response.FinancialHealthProtectionResponse;
import com.financeos.financeosbackend.financialprofile.dto.ProtectionContextResponse;
import com.financeos.financeosbackend.financialprofile.service.ProtectionContextService;
import com.financeos.financeosbackend.insurance.dto.response.ProtectionAnalysisItem;
import com.financeos.financeosbackend.insurance.service.InsurancePolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FinancialHealthProtectionService {

    private final InsurancePolicyService insurancePolicyService;
    private final ProtectionContextService protectionContextService;

    public FinancialHealthProtectionService(
            InsurancePolicyService insurancePolicyService,
            ProtectionContextService protectionContextService
    ) {
        this.insurancePolicyService = insurancePolicyService;
        this.protectionContextService = protectionContextService;
    }

    @Transactional(readOnly = true)
    public FinancialHealthProtectionResponse calculateProtectionHealth() {
        List<ProtectionAnalysisItem> analysis =
                insurancePolicyService.getProtectionAnalysis();

        Map<String, ProtectionAnalysisItem> analysisByCategory = analysis.stream()
                .collect(Collectors.toMap(
                        ProtectionAnalysisItem::category,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        String healthStatus = getStatus(analysisByCategory, "HEALTH");
        String lifeStatus = getStatus(analysisByCategory, "LIFE");
        String vehicleStatus = getStatus(analysisByCategory, "VEHICLE");

        String overallStatus = determineOverallProtectionStatus(
                healthStatus,
                lifeStatus,
                vehicleStatus
        );

        String explanation = analysis.stream()
                .map(item -> item.category() + ": " + item.explanation())
                .collect(Collectors.joining(" "));

        ProtectionContextResponse profileContext =
                protectionContextService.findCurrentUserContext().orElse(null);

        return new FinancialHealthProtectionResponse(
                healthStatus,
                lifeStatus,
                vehicleStatus,
                overallStatus,
                explanation,
                profileContext == null ? null : profileContext.getFamilyResponsibility(),
                profileContext == null ? null : profileContext.getProtectionPriority(),
                profileContext == null ? null : profileContext.getProtectionPreference()
        );
    }

    private String getStatus(
            Map<String, ProtectionAnalysisItem> analysisByCategory,
            String category
    ) {
        ProtectionAnalysisItem item = analysisByCategory.get(category);
        return item == null ? "NOT_ASSESSED" : item.status();
    }

    private String determineOverallProtectionStatus(
            String healthStatus,
            String lifeStatus,
            String vehicleStatus
    ) {
        if ("GAP_IDENTIFIED".equals(healthStatus)
                || "GAP_IDENTIFIED".equals(lifeStatus)
                || "GAP_IDENTIFIED".equals(vehicleStatus)) {
            return "REVIEW_RECOMMENDED";
        }

        if ("INCOMPLETE_DATA".equals(healthStatus)
                || "INCOMPLETE_DATA".equals(lifeStatus)
                || "INCOMPLETE_DATA".equals(vehicleStatus)
                || "REVIEW_REQUIRED".equals(healthStatus)
                || "REVIEW_REQUIRED".equals(lifeStatus)
                || "REVIEW_REQUIRED".equals(vehicleStatus)
                || "NOT_ASSESSED".equals(healthStatus)
                || "NOT_ASSESSED".equals(lifeStatus)
                || "NOT_ASSESSED".equals(vehicleStatus)) {
            return "INFORMATION_REVIEW_REQUIRED";
        }

        return "RECORDED";
    }
}