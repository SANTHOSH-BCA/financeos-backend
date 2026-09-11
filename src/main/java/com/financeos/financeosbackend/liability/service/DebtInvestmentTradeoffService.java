package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DebtInvestmentTradeoffService {

    private final DebtBurdenService debtBurdenService;
    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public DebtInvestmentTradeoffService(
            DebtBurdenService debtBurdenService,
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService
    ) {
        this.debtBurdenService = debtBurdenService;
        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public DebtInvestmentTradeoffResponse getTradeoff() {

        User user = currentUserService.getCurrentUser();

        DebtBurdenResponse debtBurden =
                debtBurdenService.getMyDebtBurden();

        BigDecimal totalOutstandingDebt =
                scale(debtBurden.getTotalOutstanding());

        BigDecimal totalInvestmentValue =
                investmentRepository.getTotalCurrentValueByUser(user);

        if (totalInvestmentValue == null) {
            totalInvestmentValue = BigDecimal.ZERO;
        }

        totalInvestmentValue = scale(totalInvestmentValue);

        BigDecimal ratio =
                calculateDebtToInvestmentRatio(
                        totalOutstandingDebt,
                        totalInvestmentValue
                );

        String tradeoffLevel =
                determineTradeoffLevel(ratio);

        String explanation =
                buildExplanation(
                        totalOutstandingDebt,
                        totalInvestmentValue,
                        ratio
                );

        return new DebtInvestmentTradeoffResponse(
                totalOutstandingDebt,
                totalInvestmentValue,
                ratio,
                tradeoffLevel,
                explanation
        );
    }

    public BigDecimal calculateDebtToInvestmentRatio(
            BigDecimal debt,
            BigDecimal investments
    ) {

        if (debt == null
                || investments == null
                || investments.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return debt
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        investments,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public String determineTradeoffLevel(
            BigDecimal debtToInvestmentRatio
    ) {

        if (debtToInvestmentRatio == null) {
            return "UNKNOWN";
        }

        if (debtToInvestmentRatio.compareTo(
                BigDecimal.valueOf(50)) <= 0) {
            return "LOW";
        }

        if (debtToInvestmentRatio.compareTo(
                BigDecimal.valueOf(100)) <= 0) {
            return "MODERATE";
        }

        return "HIGH";
    }

    public String buildExplanation(
            BigDecimal debt,
            BigDecimal investments,
            BigDecimal ratio
    ) {

        if (investments == null
                || investments.compareTo(BigDecimal.ZERO) <= 0) {

            return "Investment value is unavailable or zero, so the debt-investment trade-off cannot be assessed.";
        }

        if (ratio == null) {
            return "Debt-investment trade-off cannot be calculated from the available data.";
        }

        return "Recognized outstanding debt of "
                + format(debt)
                + " is "
                + ratio
                + "% of the current investment value of "
                + format(investments)
                + ". This is an analytical comparison, not a recommendation to sell investments or change debt repayments.";
    }

    private BigDecimal scale(BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        return value.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private String format(BigDecimal value) {

        return scale(value)
                .toPlainString();
    }
}