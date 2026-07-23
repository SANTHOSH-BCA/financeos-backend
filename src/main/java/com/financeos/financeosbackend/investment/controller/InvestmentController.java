package com.financeos.financeosbackend.investment.controller;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.common.dto.PagedResponse;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping("/test")
    public String test() {
        return "Investment Controller Working";
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvestmentResponse>> addInvestment(
            @Valid @RequestBody AddInvestmentRequest request) {

        System.out.println(">>> NEW InvestmentController is running <<<");

        InvestmentResponse response = investmentService.addInvestment(request);

        return ResponseBuilder.created(
                "Investment created successfully",
                response
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<InvestmentResponse>> getMyInvestments(
            Pageable pageable) {

        Page<InvestmentResponse> response =
                investmentService.getMyInvestments(pageable);

        return ResponseBuilder.paged(
                "Investments retrieved successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestmentResponse>> updateInvestment(
            @PathVariable Long id,
            @Valid @RequestBody AddInvestmentRequest request) {

        InvestmentResponse response =
                investmentService.updateInvestment(id, request);

        return ResponseBuilder.success(
                "Investment updated successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInvestment(
            @PathVariable Long id) {

        investmentService.deleteInvestment(id);

        return ResponseBuilder.success(
                "Investment deleted successfully",
                null
        );
    }

}