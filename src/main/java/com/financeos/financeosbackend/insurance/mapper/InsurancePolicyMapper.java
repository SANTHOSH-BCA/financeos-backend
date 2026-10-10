package com.financeos.financeosbackend.insurance.mapper;

import com.financeos.financeosbackend.insurance.dto.request.CreateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.request.UpdateInsuranceRequest;
import com.financeos.financeosbackend.insurance.dto.response.InsurancePolicyResponse;
import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import org.springframework.stereotype.Component;

@Component
public class InsurancePolicyMapper {

    public InsurancePolicy toEntity(CreateInsuranceRequest request) {
        InsurancePolicy policy = new InsurancePolicy();

        policy.setPolicyName(request.policyName());
        policy.setInsuranceType(request.insuranceType());
        policy.setProvider(request.provider());
        policy.setPolicyNumber(request.policyNumber());
        policy.setCoverageAmount(request.coverageAmount());
        policy.setPremiumAmount(request.premiumAmount());
        policy.setPremiumFrequency(request.premiumFrequency());
        policy.setStartDate(request.startDate());
        policy.setExpiryDate(request.expiryDate());
        policy.setNextPremiumDueDate(request.nextPremiumDueDate());
        policy.setNotes(request.notes());

        return policy;
    }

    public void updateEntity(
            InsurancePolicy policy,
            UpdateInsuranceRequest request
    ) {
        policy.setPolicyName(request.policyName());
        policy.setInsuranceType(request.insuranceType());
        policy.setProvider(request.provider());
        policy.setPolicyNumber(request.policyNumber());
        policy.setCoverageAmount(request.coverageAmount());
        policy.setPremiumAmount(request.premiumAmount());
        policy.setPremiumFrequency(request.premiumFrequency());
        policy.setStartDate(request.startDate());
        policy.setExpiryDate(request.expiryDate());
        policy.setNextPremiumDueDate(request.nextPremiumDueDate());
        policy.setStatus(request.status());
        policy.setNotes(request.notes());
    }

    public InsurancePolicyResponse toResponse(InsurancePolicy policy) {
        return new InsurancePolicyResponse(
                policy.getId(),
                policy.getPolicyName(),
                policy.getInsuranceType(),
                policy.getProvider(),
                policy.getPolicyNumber(),
                policy.getCoverageAmount(),
                policy.getPremiumAmount(),
                policy.getPremiumFrequency(),
                policy.getStartDate(),
                policy.getExpiryDate(),
                policy.getNextPremiumDueDate(),
                policy.getStatus(),
                policy.getNotes(),
                policy.getCreatedAt(),
                policy.getUpdatedAt(),
                policy.getPremiumPaymentStatus()
        );
    }
}

