package com.financeos.financeosbackend.insurance.dto.request;

import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.InsuranceType;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInsuranceRequest(

        @NotBlank(message = "Policy name is required")
        @Size(max = 150, message = "Policy name must not exceed 150 characters")
        String policyName,

        @NotNull(message = "Insurance type is required")
        InsuranceType insuranceType,

        @NotBlank(message = "Insurance provider is required")
        @Size(max = 150, message = "Provider must not exceed 150 characters")
        String provider,

        @Size(max = 100, message = "Policy number must not exceed 100 characters")
        String policyNumber,

        @NotNull(message = "Coverage amount is required")
        @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
        BigDecimal coverageAmount,

        @NotNull(message = "Premium amount is required")
        @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
        BigDecimal premiumAmount,

        @NotNull(message = "Premium frequency is required")
        PremiumFrequency premiumFrequency,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        LocalDate expiryDate,

        LocalDate nextPremiumDueDate,

        @NotNull(message = "Policy status is required")
        InsuranceStatus status,

        @Size(max = 5000, message = "Notes must not exceed 5000 characters")
        String notes
) {
}