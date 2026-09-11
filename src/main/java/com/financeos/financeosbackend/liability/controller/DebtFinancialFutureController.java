package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtFinancialFutureResponse;
import com.financeos.financeosbackend.liability.service.DebtFinancialFutureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtFinancialFutureController {

    private final DebtFinancialFutureService debtFinancialFutureService;

    public DebtFinancialFutureController(
            DebtFinancialFutureService debtFinancialFutureService
    ) {
        this.debtFinancialFutureService = debtFinancialFutureService;
    }

    @GetMapping("/financial-future")
    public ResponseEntity<DebtFinancialFutureResponse>
    getDebtFinancialFuture() {

        return ResponseEntity.ok(
                debtFinancialFutureService.getDebtFinancialFuture()
        );
    }
}