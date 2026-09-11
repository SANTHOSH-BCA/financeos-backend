package com.financeos.financeosbackend.liability.controller;

import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.liability.service.DebtInvestmentTradeoffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/liabilities")
public class DebtInvestmentTradeoffController {

    private final DebtInvestmentTradeoffService
            debtInvestmentTradeoffService;

    public DebtInvestmentTradeoffController(
            DebtInvestmentTradeoffService debtInvestmentTradeoffService
    ) {
        this.debtInvestmentTradeoffService =
                debtInvestmentTradeoffService;
    }

    @GetMapping("/debt-investment-tradeoff")
    public ResponseEntity<DebtInvestmentTradeoffResponse>
    getDebtInvestmentTradeoff() {

        return ResponseEntity.ok(
                debtInvestmentTradeoffService.getTradeoff()
        );
    }
}