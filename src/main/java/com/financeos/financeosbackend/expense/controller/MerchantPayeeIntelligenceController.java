package com.financeos.financeosbackend.expense.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.MerchantPayeeInsightResponse;
import com.financeos.financeosbackend.expense.service.MerchantPayeeIntelligenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses/merchant-payees")
public class MerchantPayeeIntelligenceController {

    private final MerchantPayeeIntelligenceService
            merchantPayeeIntelligenceService;

    public MerchantPayeeIntelligenceController(
            MerchantPayeeIntelligenceService
                    merchantPayeeIntelligenceService) {

        this.merchantPayeeIntelligenceService =
                merchantPayeeIntelligenceService;
    }

    @GetMapping("/intelligence")
    public ResponseEntity<ApiResponse<List<MerchantPayeeInsightResponse>>>
    getMerchantPayeeInsights() {

        List<MerchantPayeeInsightResponse> response =
                merchantPayeeIntelligenceService
                        .getMerchantPayeeInsights();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Merchant and payee intelligence fetched successfully",
                        response
                )
        );
    }
}