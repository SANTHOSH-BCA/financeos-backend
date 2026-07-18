package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import com.financeos.financeosbackend.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.expense.dto.ExpenseFilterRequest;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.PagedResponse;


@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/test")
    public String test() {
        return "Expense Controller Working";
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> addExpense(
            @Valid @RequestBody AddExpenseRequest request) {

        ExpenseResponse response = expenseService.addExpense(request);

        return ResponseBuilder.created(
                "Expense created successfully",
                response
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ExpenseResponse>> getMyExpenses(
            Pageable pageable) {

        Page<ExpenseResponse> response =
                expenseService.getMyExpenses(pageable);

        return ResponseBuilder.paged(
                "Expenses fetched successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody AddExpenseRequest request) {

        ExpenseResponse response = expenseService.updateExpense(id, request);

        return ResponseBuilder.success(
                "Expense updated successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(
            @PathVariable Long id) {

        String response = expenseService.deleteExpense(id);

        return ResponseBuilder.success(
                "Expense deleted successfully",
                response
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedResponse<ExpenseResponse>> filterExpenses(
            ExpenseFilterRequest request,
            Pageable pageable) {

        Page<ExpenseResponse> response =
                expenseService.filterExpenses(request, pageable);

        return ResponseBuilder.paged(
                "Expenses filtered successfully",
                response
        );
    }

}