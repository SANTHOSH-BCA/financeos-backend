package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.ExpenseReviewQueueResponse;
import com.financeos.financeosbackend.expense.service.ExpenseReviewQueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/review-queue")
public class ExpenseReviewQueueController {

    private final ExpenseReviewQueueService expenseReviewQueueService;

    public ExpenseReviewQueueController(
            ExpenseReviewQueueService expenseReviewQueueService) {

        this.expenseReviewQueueService = expenseReviewQueueService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseReviewQueueResponse>>>
    getReviewQueue() {

        List<ExpenseReviewQueueResponse> response =
                expenseReviewQueueService.getReviewQueue();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Expense review queue fetched successfully",
                        response
                )
        );
    }
}
