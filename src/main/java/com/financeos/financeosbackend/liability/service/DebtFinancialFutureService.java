package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialFutureResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DebtFinancialFutureService {

    private final DebtBurdenService debtBurdenService;

    public DebtFinancialFutureService(
            DebtBurdenService debtBurdenService
    ) {
        this.debtBurdenService = debtBurdenService;
    }

    public DebtFinancialFutureResponse getDebtFinancialFuture() {

        DebtBurdenResponse burden =
                debtBurdenService.getMyDebtBurden();

        BigDecimal outstandingDebt =
                normalize(burden.getTotalOutstanding());

        BigDecimal monthlyPayment =
                normalize(burden.getTotalMonthlyPayment());

        Integer remainingMonths =
                calculateRemainingMonths(
                        outstandingDebt,
                        monthlyPayment
                );

        BigDecimal remainingPayments =
                calculateRemainingPayments(
                        monthlyPayment,
                        remainingMonths
                );

        String projectionStatus =
                determineProjectionStatus(
                        outstandingDebt,
                        monthlyPayment
                );

        String explanation =
                buildExplanation(
                        outstandingDebt,
                        monthlyPayment,
                        remainingMonths,
                        remainingPayments
                );

        return new DebtFinancialFutureResponse(
                outstandingDebt,
                monthlyPayment,
                remainingMonths,
                remainingPayments,
                projectionStatus,
                explanation
        );
    }

    public Integer calculateRemainingMonths(
            BigDecimal outstandingDebt,
            BigDecimal monthlyPayment
    ) {

        if (outstandingDebt == null
                || monthlyPayment == null
                || outstandingDebt.compareTo(BigDecimal.ZERO) <= 0
                || monthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return outstandingDebt
                .divide(
                        monthlyPayment,
                        0,
                        RoundingMode.CEILING
                )
                .intValue();
    }

    public BigDecimal calculateRemainingPayments(
            BigDecimal monthlyPayment,
            Integer remainingMonths
    ) {

        if (monthlyPayment == null
                || remainingMonths == null
                || monthlyPayment.compareTo(BigDecimal.ZERO) <= 0
                || remainingMonths <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return monthlyPayment
                .multiply(BigDecimal.valueOf(remainingMonths))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String determineProjectionStatus(
            BigDecimal outstandingDebt,
            BigDecimal monthlyPayment
    ) {

        if (outstandingDebt == null
                || outstandingDebt.compareTo(BigDecimal.ZERO) <= 0) {
            return "NO_OUTSTANDING_DEBT";
        }

        if (monthlyPayment == null
                || monthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return "INSUFFICIENT_DATA";
        }

        return "PROJECTABLE";
    }

    public String buildExplanation(
            BigDecimal outstandingDebt,
            BigDecimal monthlyPayment,
            Integer remainingMonths,
            BigDecimal remainingPayments
    ) {

        if (outstandingDebt == null
                || outstandingDebt.compareTo(BigDecimal.ZERO) <= 0) {
            return "No recognized outstanding debt is currently available for future projection.";
        }

        if (monthlyPayment == null
                || monthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return "Outstanding debt exists, but a positive monthly payment is unavailable, so future debt cash flow cannot be projected.";
        }

        if (remainingMonths == null) {
            return "Future debt cash flow could not be projected from the available liability data.";
        }

        return "Based on the current outstanding debt and monthly debt payment, the remaining debt obligation is estimated at "
                + remainingMonths
                + " months with approximately "
                + remainingPayments
                + " in scheduled payments. This is a simplified projection and does not represent a guaranteed future outcome.";
    }

    private BigDecimal normalize(BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }
}