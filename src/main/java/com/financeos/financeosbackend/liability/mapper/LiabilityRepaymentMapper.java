package com.financeos.financeosbackend.liability.mapper;

import com.financeos.financeosbackend.liability.dto.CreateLiabilityRepaymentRequest;
import com.financeos.financeosbackend.liability.dto.LiabilityRepaymentResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import org.springframework.stereotype.Component;

@Component
public class LiabilityRepaymentMapper {

    public LiabilityRepayment toEntity(
            CreateLiabilityRepaymentRequest request,
            Liability liability
    ) {
        LiabilityRepayment repayment = new LiabilityRepayment();

        repayment.setLiability(liability);
        repayment.setPaymentAmount(request.getPaymentAmount());
        repayment.setPrincipalAmount(request.getPrincipalAmount());
        repayment.setInterestAmount(request.getInterestAmount());
        repayment.setRepaymentDate(request.getRepaymentDate());
        repayment.setNotes(request.getNotes());

        return repayment;
    }

    public LiabilityRepaymentResponse toResponse(
            LiabilityRepayment repayment
    ) {
        LiabilityRepaymentResponse response =
                new LiabilityRepaymentResponse();

        response.setId(repayment.getId());
        response.setLiabilityId(repayment.getLiability().getId());
        response.setPaymentAmount(repayment.getPaymentAmount());
        response.setPrincipalAmount(repayment.getPrincipalAmount());
        response.setInterestAmount(repayment.getInterestAmount());
        response.setRepaymentDate(repayment.getRepaymentDate());
        response.setNotes(repayment.getNotes());
        response.setCreatedAt(repayment.getCreatedAt());

        return response;
    }
}