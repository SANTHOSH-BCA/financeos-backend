package com.financeos.financeosbackend.liability.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DebtBurdenAnalysisService {

    public BigDecimal calculateDebtPaymentRatio(
            BigDecimal monthlyIncome,
            BigDecimal monthlyDebtPayment
    ) {
        if (monthlyIncome == null
                || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0
                || monthlyDebtPayment == null
                || monthlyDebtPayment.compareTo(BigDecimal.ZERO) < 0) {

            return null;
        }

        return monthlyDebtPayment
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        monthlyIncome,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    public String determineBurdenLevel(
            BigDecimal debtPaymentRatio
    ) {
        if (debtPaymentRatio == null) {
            return "UNKNOWN";
        }

        if (debtPaymentRatio.compareTo(
                BigDecimal.valueOf(20)
        ) <= 0) {
            return "LOW";
        }

        if (debtPaymentRatio.compareTo(
                BigDecimal.valueOf(40)
        ) <= 0) {
            return "MODERATE";
        }

        return "HIGH";
    }

    public String buildExplanation(
            BigDecimal monthlyIncome,
            BigDecimal monthlyDebtPayment,
            BigDecimal debtPaymentRatio
    ) {
        if (monthlyIncome == null
                || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return "Debt burden cannot be assessed because monthly income is unavailable.";
        }

        if (debtPaymentRatio == null) {
            return "Debt burden cannot be calculated from the available data.";
        }

        return "Monthly debt payments of "
                + format(monthlyDebtPayment)
                + " represent "
                + debtPaymentRatio
                + "% of monthly income of "
                + format(monthlyIncome)
                + ". This is an analytical indicator, not a universal financial rule.";
    }

    private String format(BigDecimal value) {
        return value
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}