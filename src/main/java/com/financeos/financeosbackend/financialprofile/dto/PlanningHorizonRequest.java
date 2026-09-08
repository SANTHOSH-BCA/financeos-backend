package com.financeos.financeosbackend.financialprofile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanningHorizonRequest {

    @NotBlank
    private String planningHorizon;
}