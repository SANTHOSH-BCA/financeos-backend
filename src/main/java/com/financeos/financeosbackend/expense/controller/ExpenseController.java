package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import com.financeos.financeosbackend.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/expenses")
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
    public ExpenseResponse addExpense(@Valid @RequestBody AddExpenseRequest request) {

        return expenseService.addExpense(request);

    }

    @GetMapping
    public Page<ExpenseResponse> getMyExpenses(Pageable pageable) {

        return expenseService.getMyExpenses(pageable);

    }

    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody AddExpenseRequest request) {

        return expenseService.updateExpense(id, request);

    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {

        return expenseService.deleteExpense(id);

    }

}