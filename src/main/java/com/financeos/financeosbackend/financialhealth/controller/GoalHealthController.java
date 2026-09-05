package com.financeos.financeosbackend.financialhealth.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.financialhealth.dto.GoalHealthResponse;
import com.financeos.financeosbackend.financialhealth.service.FinancialHealthGoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/financial-health")
public class GoalHealthController {

    private final FinancialHealthGoalService financialHealthGoalService;

    public GoalHealthController(
            FinancialHealthGoalService financialHealthGoalService
    ) {
        this.financialHealthGoalService = financialHealthGoalService;
    }

    @GetMapping("/goals")
    public ResponseEntity<ApiResponse<GoalHealthResponse>> getGoalHealth() {

        GoalHealthResponse response =
                financialHealthGoalService.calculateGoalHealth();

        return ResponseBuilder.success(
                "Goal health retrieved successfully",
                response
        );
    }
}