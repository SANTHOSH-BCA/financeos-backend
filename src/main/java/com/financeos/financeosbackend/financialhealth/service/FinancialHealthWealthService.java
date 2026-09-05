package com.financeos.financeosbackend.financialhealth.service;

import com.financeos.financeosbackend.financialhealth.dto.WealthHealthResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialHealthWealthService {

    private final NetWorthService netWorthService;

    public FinancialHealthWealthService(
            NetWorthService netWorthService
    ) {
        this.netWorthService = netWorthService;
    }

    public WealthHealthResponse calculateWealthHealth() {

        BigDecimal recognizedAssets =
                netWorthService.calculateRecognizedAssets();

        BigDecimal recognizedLiabilities =
                netWorthService.calculateRecognizedLiabilities();

        BigDecimal netWorth =
                netWorthService.calculateNetWorth();

        String status;

        if (netWorth.compareTo(BigDecimal.ZERO) > 0) {
            status = "POSITIVE";
        } else if (netWorth.compareTo(BigDecimal.ZERO) == 0) {
            status = "NEUTRAL";
        } else {
            status = "NEGATIVE";
        }

        return new WealthHealthResponse(
                recognizedAssets,
                recognizedLiabilities,
                netWorth,
                status
        );
    }
}