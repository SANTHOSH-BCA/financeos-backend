package com.financeos.financeosbackend.financialprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FinancialPriorityRequest {

    @NotBlank
    private String priority;

    @NotNull
    @Positive
    private Integer priorityRank;
}