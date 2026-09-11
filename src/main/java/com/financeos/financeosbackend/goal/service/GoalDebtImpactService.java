package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.service.DebtBurdenService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GoalDebtImpactService {

    private final DebtBurdenService debtBurdenService;

    public GoalDebtImpactService(
            DebtBurdenService debtBurdenService
    ) {
        this.debtBurdenService = debtBurdenService;
    }

    public BigDecimal getMonthlyDebtPayment() {

        DebtBurdenResponse debtBurden =
                debtBurdenService.getMyDebtBurden();

        if (debtBurden.getTotalMonthlyPayment() == null) {
            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        return debtBurden.getTotalMonthlyPayment()
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateAvailableAfterDebt(
            BigDecimal monthlyIncome
    ) {

        if (monthlyIncome == null) {
            return null;
        }

        return monthlyIncome
                .subtract(getMonthlyDebtPayment())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateContributionGap(
            BigDecimal requiredMonthlyContribution,
            BigDecimal availableAfterDebt
    ) {

        if (requiredMonthlyContribution == null
                || availableAfterDebt == null) {
            return null;
        }

        BigDecimal gap =
                requiredMonthlyContribution
                        .subtract(availableAfterDebt);

        if (gap.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return gap.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    public boolean hasDebtImpact(
            BigDecimal requiredMonthlyContribution,
            BigDecimal availableAfterDebt
    ) {

        if (requiredMonthlyContribution == null
                || availableAfterDebt == null) {
            return false;
        }

        return requiredMonthlyContribution
                .compareTo(availableAfterDebt) > 0;
    }
}