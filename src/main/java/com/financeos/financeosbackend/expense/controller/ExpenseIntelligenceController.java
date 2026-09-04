package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.expense.dto.ExpenseIntelligenceResponse;
import com.financeos.financeosbackend.expense.service.ExpenseIntelligenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expenses/intelligence")
public class ExpenseIntelligenceController {

    private final ExpenseIntelligenceService expenseIntelligenceService;

    public ExpenseIntelligenceController(
            ExpenseIntelligenceService expenseIntelligenceService) {

        this.expenseIntelligenceService = expenseIntelligenceService;
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpense() {

        BigDecimal total =
                expenseIntelligenceService.getTotalExpense();

        return ResponseBuilder.success(
                "Total expense fetched successfully",
                total
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>>
    getCategoryWiseExpense() {

        Map<String, BigDecimal> categoryWiseExpense =
                expenseIntelligenceService.getCategoryWiseExpense();

        return ResponseBuilder.success(
                "Category-wise expense fetched successfully",
                categoryWiseExpense
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>>
    getMonthlyExpense() {

        Map<String, BigDecimal> monthlyExpense =
                expenseIntelligenceService.getMonthlyExpense();

        return ResponseBuilder.success(
                "Monthly expense fetched successfully",
                monthlyExpense
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ExpenseIntelligenceResponse>>
    getExpenseIntelligence() {

        ExpenseIntelligenceResponse response =
                expenseIntelligenceService.getExpenseIntelligence();

        return ResponseBuilder.success(
                "Expense intelligence fetched successfully",
                response
        );
    }
}