package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.service.DebtBurdenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtBurdenController {

    private final DebtBurdenService debtBurdenService;

    public DebtBurdenController(
            DebtBurdenService debtBurdenService
    ) {
        this.debtBurdenService = debtBurdenService;
    }

    @GetMapping("/debt-burden")
    public ResponseEntity<DebtBurdenResponse> getMyDebtBurden() {
        return ResponseEntity.ok(
                debtBurdenService.getMyDebtBurden()
        );
    }
}