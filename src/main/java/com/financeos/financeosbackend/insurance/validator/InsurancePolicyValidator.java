package com.financeos.financeosbackend.insurance.validator;

import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.UpdateInsuranceRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InsurancePolicyValidator {

    public void validateCreate(CreateInsuranceRequest request) {
        validateDates(
                request.startDate(),
                request.expiryDate(),
                request.nextPremiumDueDate()
        );
    }

    public void validateUpdate(UpdateInsuranceRequest request) {
        validateDates(
                request.startDate(),
                request.expiryDate(),
                request.nextPremiumDueDate()
        );
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate expiryDate,
            LocalDate nextPremiumDueDate
    ) {
        if (expiryDate != null && expiryDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Policy expiry date cannot be before its start date"
            );
        }

        if (nextPremiumDueDate != null
                && nextPremiumDueDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Next premium due date cannot be before the policy start date"
            );
        }
    }
}