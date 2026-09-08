package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialResponsibilityContextResponse {

    private Long id;
    private String responsibilityLevel;
    private Integer dependentsCount;
    private String dependentContext;
}