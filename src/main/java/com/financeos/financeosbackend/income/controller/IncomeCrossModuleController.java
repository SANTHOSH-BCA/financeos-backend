package com.financeos.financeosbackend.income.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.income.dto.IncomeCrossModuleResponse;
import com.financeos.financeosbackend.income.service.IncomeCrossModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incomes/cross-module")
public class IncomeCrossModuleController {

    private final IncomeCrossModuleService incomeCrossModuleService;

    public IncomeCrossModuleController(
            IncomeCrossModuleService incomeCrossModuleService) {

        this.incomeCrossModuleService = incomeCrossModuleService;
    }

    @GetMapping("/intelligence")
    public ResponseEntity<ApiResponse<IncomeCrossModuleResponse>>
    getCrossModuleIntelligence() {

        IncomeCrossModuleResponse response =
                incomeCrossModuleService.getCrossModuleIntelligence();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Income cross-module intelligence fetched successfully",
                        response
                )
        );
    }
}