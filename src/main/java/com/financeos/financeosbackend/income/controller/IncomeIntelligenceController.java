package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.income.dto.IncomeIntelligenceResponse;
import com.financeos.financeosbackend.income.service.IncomeIntelligenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incomes/intelligence")
public class IncomeIntelligenceController {

    private final IncomeIntelligenceService incomeIntelligenceService;

    public IncomeIntelligenceController(
            IncomeIntelligenceService incomeIntelligenceService) {

        this.incomeIntelligenceService =
                incomeIntelligenceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<IncomeIntelligenceResponse>>
    getIncomeIntelligence() {

        IncomeIntelligenceResponse response =
                incomeIntelligenceService
                        .getIncomeIntelligence();

        return ResponseBuilder.success(
                "Income intelligence fetched successfully",
                response
        );
    }
}