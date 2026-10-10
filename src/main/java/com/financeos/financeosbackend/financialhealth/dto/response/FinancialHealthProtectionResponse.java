package com.financeos.financeosbackend.financialhealth.dto.response;

public record FinancialHealthProtectionResponse(
        String healthProtectionStatus,
        String lifeProtectionStatus,
        String vehicleProtectionStatus,
        String overallProtectionStatus,
        String explanation,
        String familyResponsibility,
        String protectionPriority,
        String protectionPreference
) {
}