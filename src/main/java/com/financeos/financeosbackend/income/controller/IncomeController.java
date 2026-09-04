package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.income.dto.IncomeResponse;
import com.financeos.financeosbackend.income.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;import com.financeos.financeosbackend.income.dto.MonthlyIncomeResponse;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IncomeResponse>> addIncome(
            @Valid @RequestBody AddIncomeRequest request) {

        IncomeResponse response = incomeService.addIncome(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Income added successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<IncomeResponse>>> getMyIncome(
            Pageable pageable) {

        Page<IncomeResponse> response = incomeService.getMyIncome(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Income fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomeResponse>> updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody AddIncomeRequest request) {

        IncomeResponse response = incomeService.updateIncome(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Income updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIncome(
            @PathVariable Long id) {

        incomeService.deleteIncome(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Income deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<MonthlyIncomeResponse>>>
    getMonthlyIncomeHistory() {

        List<MonthlyIncomeResponse> response =
                incomeService.getMonthlyIncomeHistory();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Monthly income history fetched successfully",
                        response
                )
        );
    }

}