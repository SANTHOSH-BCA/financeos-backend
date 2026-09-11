package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtFinancialHealthResponse;
import com.financeos.financeosbackend.liability.service.DebtFinancialHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtFinancialHealthController {

    private final DebtFinancialHealthService debtFinancialHealthService;

    public DebtFinancialHealthController(
            DebtFinancialHealthService debtFinancialHealthService
    ) {
        this.debtFinancialHealthService = debtFinancialHealthService;
    }

    @GetMapping("/financial-health")
    public ResponseEntity<DebtFinancialHealthResponse>
    getDebtFinancialHealth() {

        return ResponseEntity.ok(
                debtFinancialHealthService.getDebtFinancialHealth()
        );
    }
}