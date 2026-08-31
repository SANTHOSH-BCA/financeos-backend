package com.financeos.financeosbackend.liability.validator;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LiabilityValidator {

    public void validateResponsibility(CreateLiabilityRequest request) {

        ResponsibilityType responsibilityType =
                request.getResponsibilityType();

        BigDecimal percentage =
                request.getResponsibilityPercentage();

        if (responsibilityType == ResponsibilityType.INDIVIDUAL) {

            if (percentage == null) {
                request.setResponsibilityPercentage(
                        BigDecimal.valueOf(100)
                );
                return;
            }

            if (percentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                throw new IllegalArgumentException(
                        "Individual responsibility must be 100%"
                );
            }

            return;
        }

        if (responsibilityType == ResponsibilityType.SHARED) {

            if (percentage == null) {
                throw new IllegalArgumentException(
                        "Responsibility percentage is required for shared responsibility"
                );
            }

            if (percentage.compareTo(BigDecimal.ZERO) <= 0
                    || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException(
                        "Responsibility percentage must be between 0 and 100"
                );
            }

            return;
        }

        if (responsibilityType == ResponsibilityType.FAMILY_UNCLEAR) {

            request.setResponsibilityPercentage(null);
        }
    }
}