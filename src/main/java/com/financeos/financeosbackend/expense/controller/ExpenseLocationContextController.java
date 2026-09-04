package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.ExpenseLocationContextResponse;
import com.financeos.financeosbackend.expense.service.ExpenseLocationContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/location")
public class ExpenseLocationContextController {

    private final ExpenseLocationContextService locationContextService;

    public ExpenseLocationContextController(
            ExpenseLocationContextService locationContextService) {

        this.locationContextService = locationContextService;
    }

    @GetMapping("/context")
    public ResponseEntity<ApiResponse<List<ExpenseLocationContextResponse>>>
    getLocationContexts() {

        List<ExpenseLocationContextResponse> response =
                locationContextService.getLocationContexts();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Expense location context fetched successfully",
                        response
                )
        );
    }
}