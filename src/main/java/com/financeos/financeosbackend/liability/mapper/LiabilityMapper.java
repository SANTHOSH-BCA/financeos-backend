package com.financeos.financeosbackend.liability.mapper;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class LiabilityMapper {

    public Liability toEntity(CreateLiabilityRequest request) {
        Liability liability = new Liability();

        liability.setLiabilityName(request.getLiabilityName());
        liability.setLiabilityType(request.getLiabilityType());
        liability.setOutstandingAmount(request.getOutstandingAmount());
        liability.setOriginalAmount(request.getOriginalAmount());
        liability.setLender(request.getLender());
        liability.setInterestRate(request.getInterestRate());
        liability.setInterestType(request.getInterestType());
        liability.setPaymentAmount(request.getPaymentAmount());
        liability.setPaymentFrequency(request.getPaymentFrequency());
        liability.setStartDate(request.getStartDate());
        liability.setEndDate(request.getEndDate());
        liability.setNextPaymentDate(request.getNextPaymentDate());
        liability.setResponsibilityType(request.getResponsibilityType());
        liability.setResponsibilityPercentage(request.getResponsibilityPercentage());
        liability.setValuationDate(request.getValuationDate());

        return liability;
    }

    public LiabilityResponse toResponse(Liability liability) {
        LiabilityResponse response = new LiabilityResponse();

        response.setId(liability.getId());
        response.setLiabilityName(liability.getLiabilityName());
        response.setLiabilityType(liability.getLiabilityType());
        response.setOutstandingAmount(liability.getOutstandingAmount());
        response.setOriginalAmount(liability.getOriginalAmount());
        response.setLender(liability.getLender());
        response.setInterestRate(liability.getInterestRate());
        response.setInterestType(liability.getInterestType());
        response.setPaymentAmount(liability.getPaymentAmount());
        response.setPaymentFrequency(liability.getPaymentFrequency());
        response.setStartDate(liability.getStartDate());
        response.setEndDate(liability.getEndDate());
        response.setNextPaymentDate(liability.getNextPaymentDate());
        response.setStatus(liability.getStatus());
        response.setResponsibilityType(liability.getResponsibilityType());
        response.setResponsibilityPercentage(liability.getResponsibilityPercentage());
        response.setValuationDate(liability.getValuationDate());
        response.setCreatedAt(liability.getCreatedAt());
        response.setUpdatedAt(liability.getUpdatedAt());

        if (liability.getResponsibilityType() == ResponsibilityType.FAMILY_UNCLEAR) {
            response.setRecognizedLiability(BigDecimal.ZERO);
            response.setIncludedInNetWorth(false);
            response.setResponsibilityPercentage(null);
            return response;
        }

        BigDecimal percentage = liability.getResponsibilityPercentage();

        BigDecimal recognizedLiability =
                liability.getOutstandingAmount()
                        .multiply(percentage)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP
                        );

        response.setRecognizedLiability(recognizedLiability);
        response.setIncludedInNetWorth(true);

        return response;
    }
}