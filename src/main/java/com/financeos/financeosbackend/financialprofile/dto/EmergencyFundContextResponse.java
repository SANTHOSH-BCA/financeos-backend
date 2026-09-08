package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyFundContextResponse {

    private Long id;
    private String preferredCoverage;
    private String safetyLevel;
}