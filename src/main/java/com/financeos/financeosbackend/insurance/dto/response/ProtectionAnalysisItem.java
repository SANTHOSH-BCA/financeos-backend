package com.financeos.financeosbackend.insurance.dto.response;

public record ProtectionAnalysisItem(
        String category,
        String status,
        int recordedPolicyCount,
        String explanation
) {
}
