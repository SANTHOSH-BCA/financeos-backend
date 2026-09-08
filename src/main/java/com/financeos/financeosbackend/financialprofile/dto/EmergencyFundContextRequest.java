package com.financeos.financeosbackend.financialprofile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyFundContextRequest {

    private String preferredCoverage;

    private String safetyLevel;
}