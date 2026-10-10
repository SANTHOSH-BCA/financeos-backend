package com.financeos.financeosbackend.insurance.dto.response;

import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InsuranceRenewalResponse(
        Long id,
        LocalDate previousStartDate,
        LocalDate previousExpiryDate,
        LocalDate newStartDate,
        LocalDate newExpiryDate,
        BigDecimal previousCoverageAmount,
        BigDecimal newCoverageAmount,
        BigDecimal previousPremiumAmount,
        BigDecimal newPremiumAmount,
        PremiumFrequency previousPremiumFrequency,
        PremiumFrequency newPremiumFrequency,
        LocalDateTime renewedAt
) {
}