package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.expense.dto.ExpenseInsightResponse;
import com.financeos.financeosbackend.expense.service.ExpenseInsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/insights")
public class ExpenseInsightController {

    private final ExpenseInsightService expenseInsightService;

    public ExpenseInsightController(
            ExpenseInsightService expenseInsightService) {

        this.expenseInsightService = expenseInsightService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseInsightResponse>>>
    getExpenseInsights() {

        List<ExpenseInsightResponse> response =
                expenseInsightService.getExpenseInsights();

        return ResponseBuilder.success(
                "Expense insights fetched successfully",
                response
        );
    }
}