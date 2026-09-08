package com.financeos.financeosbackend.financialprofile.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FinancialResponsibilityContextRequest {

    private String responsibilityLevel;

    @PositiveOrZero
    private Integer dependentsCount;

    private String dependentContext;
}