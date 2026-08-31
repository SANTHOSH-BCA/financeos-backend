package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProfileResponse {

    private Long id;

    private Long userId;

    private LocalDate dateOfBirth;

    private String occupation;

    private String employmentStatus;

    private String investmentExperience;

    private String planningHorizon;

    private String financialResponsibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}