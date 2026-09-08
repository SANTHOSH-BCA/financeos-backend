package com.financeos.financeosbackend.financialprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialPriorityResponse {

    private Long id;

    private String priority;

    private Integer priorityRank;
}