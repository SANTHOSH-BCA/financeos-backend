package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtNetWorthImpactResponse;
import com.financeos.financeosbackend.liability.service.DebtNetWorthImpactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtNetWorthImpactController {

    private final DebtNetWorthImpactService debtNetWorthImpactService;

    public DebtNetWorthImpactController(
            DebtNetWorthImpactService debtNetWorthImpactService
    ) {
        this.debtNetWorthImpactService = debtNetWorthImpactService;
    }

    @GetMapping("/debt-net-worth-impact")
    public ResponseEntity<DebtNetWorthImpactResponse>
    getDebtNetWorthImpact() {

        return ResponseEntity.ok(
                debtNetWorthImpactService.getImpact()
        );
    }
}