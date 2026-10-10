package com.financeos.financeosbackend.insurance.dto.response;

import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InsurancePolicyResponse(

        Long id,
        String policyName,
        InsuranceType insuranceType,
        String provider,
        String policyNumber,
        BigDecimal coverageAmount,
        BigDecimal premiumAmount,
        PremiumFrequency premiumFrequency,
        LocalDate startDate,
        LocalDate expiryDate,
        LocalDate nextPremiumDueDate,
        InsuranceStatus status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PremiumPaymentStatus premiumPaymentStatus

) {
}

