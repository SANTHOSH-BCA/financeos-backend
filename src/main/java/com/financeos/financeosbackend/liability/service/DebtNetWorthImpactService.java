package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtNetWorthImpactResponse;
import com.financeos.financeosbackend.networth.service.NetWorthService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DebtNetWorthImpactService {

    private final NetWorthService netWorthService;

    public DebtNetWorthImpactService(
            NetWorthService netWorthService
    ) {
        this.netWorthService = netWorthService;
    }

    public DebtNetWorthImpactResponse getImpact() {

        BigDecimal recognizedAssets =
                normalize(netWorthService.calculateIncludedAssets());

        BigDecimal recognizedLiabilities =
                normalize(netWorthService.calculateIncludedLiabilities());

        BigDecimal netWorth =
                normalize(netWorthService.calculateNetWorth());

        BigDecimal liabilityImpactPercentage =
                calculateLiabilityImpactPercentage(
                        recognizedLiabilities,
                        recognizedAssets
                );

        String explanation =
                buildExplanation(
                        recognizedLiabilities,
                        netWorth,
                        liabilityImpactPercentage
                );

        return new DebtNetWorthImpactResponse(
                recognizedAssets,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                recognizedLiabilities,
                netWorth,
                liabilityImpactPercentage,
                explanation
        );
    }

    public BigDecimal calculateLiabilityImpactPercentage(
            BigDecimal liabilities,
            BigDecimal assets
    ) {

        if (liabilities == null
                || assets == null
                || assets.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return liabilities
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        assets,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public String buildExplanation(
            BigDecimal liabilities,
            BigDecimal netWorth,
            BigDecimal liabilityImpactPercentage
    ) {

        if (liabilities == null
                || liabilities.compareTo(BigDecimal.ZERO) == 0) {

            return "No recognized liabilities are currently reducing your net worth.";
        }

        if (netWorth != null
                && netWorth.compareTo(BigDecimal.ZERO) < 0) {

            return "Recognized liabilities currently exceed recognized financial assets, resulting in negative net worth.";
        }

        if (liabilityImpactPercentage == null) {

            return "Recognized liabilities reduce net worth. The liability impact percentage cannot be calculated because recognized assets are unavailable or zero.";
        }

        return "Recognized liabilities reduce your net worth by "
                + liabilities.setScale(2, RoundingMode.HALF_UP)
                + ". Their current value is approximately "
                + liabilityImpactPercentage.setScale(2, RoundingMode.HALF_UP)
                + "% of recognized assets.";
    }

    private BigDecimal normalize(BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }
}