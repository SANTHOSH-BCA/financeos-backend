package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.investment.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;import java.util.HashMap;
import java.util.Map;

@Service
public class InvestmentInsightService {

    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public InvestmentInsightService(
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService) {

        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public List<InvestmentInsightResponse> getInvestmentInsights() {

        User user = currentUserService.getCurrentUser();

        List<Investment> investments =
                investmentRepository.findByUser(
                        user,
                        org.springframework.data.domain.Pageable.unpaged()
                ).getContent();

        List<InvestmentInsightResponse> insights = new ArrayList<>();

        if (investments.isEmpty()) {
            insights.add(new InvestmentInsightResponse(
                    "NO_DATA",
                    "No investment data available"
            ));
            return insights;
        }

        BigDecimal totalInvested =
                investments.stream()
                        .map(this::getInvestedAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentValue =
                investments.stream()
                        .map(this::getCurrentValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitLoss =
                currentValue.subtract(totalInvested);

        if (profitLoss.compareTo(BigDecimal.ZERO) > 0) {
            insights.add(new InvestmentInsightResponse(
                    "PORTFOLIO_GROWTH",
                    "Your portfolio is currently above the total invested amount"
            ));
        } else if (profitLoss.compareTo(BigDecimal.ZERO) < 0) {
            insights.add(new InvestmentInsightResponse(
                    "PORTFOLIO_DECLINE",
                    "Your portfolio is currently below the total invested amount"
            ));
        }

        Investment bestPerformer = investments.stream()
                .max(Comparator.comparing(this::getProfitLoss))
                .orElse(null);

        if (bestPerformer != null) {
            insights.add(new InvestmentInsightResponse(
                    "BEST_PERFORMER",
                    bestPerformer.getInvestmentName()
                            + " is currently your best-performing investment"
            ));
        }

        Investment largestInvestment = investments.stream()
                .max(Comparator.comparing(this::getInvestedAmount))
                .orElse(null);

        if (largestInvestment != null && totalInvested.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal concentration = getInvestedAmount(largestInvestment)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalInvested, 2, java.math.RoundingMode.HALF_UP);

            if (concentration.compareTo(BigDecimal.valueOf(50)) > 0) {
                insights.add(new InvestmentInsightResponse(
                        "HIGH_CONCENTRATION",
                        largestInvestment.getInvestmentName()
                                + " represents "
                                + concentration
                                + "% of your invested portfolio"
                ));
            }
        }

        Map<String, BigDecimal> investmentTypeTotals = new HashMap<>();

        for (Investment investment : investments) {
            String investmentType = investment.getInvestmentType();

            if (investmentType == null || investmentType.isBlank()) {
                continue;
            }

            investmentTypeTotals.merge(
                    investmentType,
                    getInvestedAmount(investment),
                    BigDecimal::add
            );
        }

        if (!investmentTypeTotals.isEmpty()
                && totalInvested.compareTo(BigDecimal.ZERO) > 0) {

            Map.Entry<String, BigDecimal> dominantType =
                    investmentTypeTotals.entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);

            if (dominantType != null) {

                BigDecimal typeConcentration =
                        dominantType.getValue()
                                .multiply(BigDecimal.valueOf(100))
                                .divide(
                                        totalInvested,
                                        2,
                                        java.math.RoundingMode.HALF_UP
                                );

                insights.add(new InvestmentInsightResponse(
                        "ASSET_CLASS_CONCENTRATION",
                        dominantType.getKey()
                                + " represents "
                                + typeConcentration
                                + "% of your invested portfolio"
                ));
            }
        }

        if (investments.size() >= 5) {
            insights.add(new InvestmentInsightResponse(
                    "DIVERSIFIED_ACTIVITY",
                    "Your portfolio contains multiple investment holdings"
            ));
        }

        return insights;
    }

    private BigDecimal getInvestedAmount(Investment investment) {
        return investment.getTotalInvestedAmount() != null
                ? investment.getTotalInvestedAmount()
                : investment.getAmount();
    }

    private BigDecimal getCurrentValue(Investment investment) {
        return investment.getCurrentValue() != null
                ? investment.getCurrentValue()
                : getInvestedAmount(investment);
    }

    private BigDecimal getProfitLoss(Investment investment) {
        return getCurrentValue(investment)
                .subtract(getInvestedAmount(investment));
    }
}