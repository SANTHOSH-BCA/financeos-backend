package com.financeos.financeosbackend.insurance.mapper;

import com.financeos.financeosbackend.insurance.dto.response.InsuranceRenewalResponse;
import com.financeos.financeosbackend.insurance.entity.InsuranceRenewal;
import org.springframework.stereotype.Component;

@Component
public class InsuranceRenewalMapper {

    public InsuranceRenewalResponse toResponse(InsuranceRenewal renewal) {
        return new InsuranceRenewalResponse(
                renewal.getId(),
                renewal.getPreviousStartDate(),
                renewal.getPreviousExpiryDate(),
                renewal.getNewStartDate(),
                renewal.getNewExpiryDate(),
                renewal.getPreviousCoverageAmount(),
                renewal.getNewCoverageAmount(),
                renewal.getPreviousPremiumAmount(),
                renewal.getNewPremiumAmount(),
                renewal.getPreviousPremiumFrequency(),
                renewal.getNewPremiumFrequency(),
                renewal.getRenewedAt()
        );
    }
}