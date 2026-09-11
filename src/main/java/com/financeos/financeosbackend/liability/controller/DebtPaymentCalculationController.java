package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtPaymentCalculationResponse;
import com.financeos.financeosbackend.liability.service.DebtPaymentCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtPaymentCalculationController {

    private final DebtPaymentCalculationService calculationService;

    public DebtPaymentCalculationController(
            DebtPaymentCalculationService calculationService
    ) {
        this.calculationService = calculationService;
    }

    @GetMapping("/{liabilityId}/payment-calculation")
    public ResponseEntity<DebtPaymentCalculationResponse> calculate(
            @PathVariable Long liabilityId
    ) {
        return ResponseEntity.ok(
                calculationService.calculate(liabilityId)
        );
    }
}