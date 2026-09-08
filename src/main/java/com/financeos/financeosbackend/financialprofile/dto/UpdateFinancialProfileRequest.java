package com.financeos.financeosbackend.financialprofile.dto;

import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFinancialProfileRequest {

    @Past
    private LocalDate dateOfBirth;

    private String occupation;

    private String employmentStatus;

    private String investmentExperience;

    private String planningHorizon;

    private String financialResponsibility;
}