package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.investment.dto.InvestmentIntelligenceResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvestmentIntelligenceService {

    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public InvestmentIntelligenceService(
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService) {

        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public InvestmentIntelligenceResponse getInvestmentIntelligence() {

        User user = currentUserService.getCurrentUser();

        List<Investment> investments =
                investmentRepository.findByUser(
                        user,
                        org.springframework.data.domain.Pageable.unpaged()
                ).getContent();

        if (investments.isEmpty()) {
            return new InvestmentIntelligenceResponse(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    null,
                    null,
                    null,
                    "NO_INVESTMENTS",
                    "No investment data available"
            );
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

        BigDecimal returnPercentage = BigDecimal.ZERO;

        if (totalInvested.compareTo(BigDecimal.ZERO) > 0) {
            returnPercentage = profitLoss
                    .divide(totalInvested, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        Investment best = investments.stream()
                .max(Comparator.comparing(this::getProfitLoss))
                .orElse(null);

        Investment worst = investments.stream()
                .min(Comparator.comparing(this::getProfitLoss))
                .orElse(null);

        Map<String, BigDecimal> typeTotals =
                investments.stream()
                        .collect(Collectors.groupingBy(
                                Investment::getInvestmentType,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        this::getInvestedAmount,
                                        BigDecimal::add
                                )
                        ));

        String dominantType =
                typeTotals.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);

        String contributionBehaviour;

        if (investments.size() <= 1) {
            contributionBehaviour = "LOW_ACTIVITY";
        } else if (investments.size() <= 5) {
            contributionBehaviour = "MODERATE_ACTIVITY";
        } else {
            contributionBehaviour = "HIGH_ACTIVITY";
        }

        String observation;

        if (profitLoss.compareTo(BigDecimal.ZERO) > 0) {
            observation = "Portfolio is currently in profit";
        } else if (profitLoss.compareTo(BigDecimal.ZERO) < 0) {
            observation = "Portfolio is currently below invested amount";
        } else {
            observation = "Portfolio is currently at invested value";
        }

        return new InvestmentIntelligenceResponse(
                totalInvested,
                currentValue,
                profitLoss,
                returnPercentage,
                best != null ? best.getInvestmentName() : null,
                worst != null ? worst.getInvestmentName() : null,
                dominantType,
                contributionBehaviour,
                observation
        );
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