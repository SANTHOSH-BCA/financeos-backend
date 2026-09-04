package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.income.dto.IncomeInsightResponse;
import com.financeos.financeosbackend.income.service.IncomeInsightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/incomes/insights")
public class IncomeInsightController {

    private final IncomeInsightService incomeInsightService;

    public IncomeInsightController(
            IncomeInsightService incomeInsightService) {
        this.incomeInsightService = incomeInsightService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<IncomeInsightResponse>>>
    getIncomeInsights() {

        List<IncomeInsightResponse> response =
                incomeInsightService.getIncomeInsights();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Income insights fetched successfully",
                        response
                )
        );
    }
}