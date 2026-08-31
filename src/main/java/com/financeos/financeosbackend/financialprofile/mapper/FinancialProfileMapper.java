package com.financeos.financeosbackend.financialprofile.mapper;

import com.financeos.financeosbackend.financialprofile.dto.CreateFinancialProfileRequest;
import com.financeos.financeosbackend.financialprofile.dto.FinancialProfileResponse;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FinancialProfileMapper {

    public FinancialProfile toEntity(
            CreateFinancialProfileRequest request,
            User user
    ) {
        FinancialProfile profile = new FinancialProfile();

        profile.setUser(user);
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setOccupation(request.getOccupation());
        profile.setEmploymentStatus(request.getEmploymentStatus());
        profile.setInvestmentExperience(request.getInvestmentExperience());
        profile.setPlanningHorizon(request.getPlanningHorizon());
        profile.setFinancialResponsibility(request.getFinancialResponsibility());

        return profile;
    }

    public FinancialProfileResponse toResponse(FinancialProfile profile) {
        return new FinancialProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getDateOfBirth(),
                profile.getOccupation(),
                profile.getEmploymentStatus(),
                profile.getInvestmentExperience(),
                profile.getPlanningHorizon(),
                profile.getFinancialResponsibility(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}