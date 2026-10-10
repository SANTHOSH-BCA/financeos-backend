package com.financeos.financeosbackend.insurance.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.financeos.financeosbackend.insurance.enums.PremiumFrequency;

public record RenewInsuranceRequest(

        @NotNull(message = "New start date is required")
        LocalDate newStartDate,

        @NotNull(message = "New expiry date is required")
        LocalDate newExpiryDate,

        @NotNull(message = "New coverage amount is required")
        @DecimalMin(value = "0.01", message = "Coverage amount must be greater than zero")
        BigDecimal newCoverageAmount,

        @NotNull(message = "New premium amount is required")
        @DecimalMin(value = "0.01", message = "Premium amount must be greater than zero")
        BigDecimal newPremiumAmount,

        @NotNull(message = "New premium frequency is required")
        PremiumFrequency newPremiumFrequency

) {
}
