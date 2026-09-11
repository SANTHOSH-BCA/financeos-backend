package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialHealthResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DebtFinancialHealthService {

    private final DebtBurdenService debtBurdenService;

    public DebtFinancialHealthService(
            DebtBurdenService debtBurdenService
    ) {
        this.debtBurdenService = debtBurdenService;
    }

    public DebtFinancialHealthResponse getDebtFinancialHealth() {

        DebtBurdenResponse burden =
                debtBurdenService.getMyDebtBurden();

        BigDecimal outstandingDebt =
                normalize(burden.getTotalOutstanding());

        BigDecimal monthlyPayment =
                normalize(burden.getTotalMonthlyPayment());

        BigDecimal debtPaymentRatio =
                normalizeNullable(burden.getDebtPaymentRatio());

        int activeCount = burden.getActiveLiabilityCount();

        Integer overdueCount = calculateOverdueCount();

        BigDecimal principalPaid =
                normalize(burden.getTotalPrincipalPaid());

        BigDecimal interestPaid =
                normalize(burden.getTotalInterestPaid());

        BigDecimal totalRepaid =
                normalize(burden.getTotalPaid());

        String debtHealth =
                determineDebtHealth(
                        debtPaymentRatio,
                        overdueCount,
                        outstandingDebt
                );

        String explanation =
                buildExplanation(
                        debtPaymentRatio,
                        overdueCount,
                        outstandingDebt
                );

        return new DebtFinancialHealthResponse(
                outstandingDebt,
                monthlyPayment,
                debtPaymentRatio,
                activeCount,
                overdueCount,
                principalPaid,
                interestPaid,
                totalRepaid,
                debtHealth,
                explanation
        );
    }

    public String determineDebtHealth(
            BigDecimal debtPaymentRatio,
            Integer overdueCount,
            BigDecimal outstandingDebt
    ) {

        if (overdueCount != null && overdueCount > 0) {
            return "NEEDS_ATTENTION";
        }

        if (debtPaymentRatio == null) {
            if (outstandingDebt == null
                    || outstandingDebt.compareTo(BigDecimal.ZERO) == 0) {
                return "HEALTHY";
            }

            return "INFORMATIONAL";
        }

        if (debtPaymentRatio.compareTo(BigDecimal.valueOf(40)) > 0) {
            return "HIGH_BURDEN";
        }

        if (debtPaymentRatio.compareTo(BigDecimal.valueOf(20)) > 0) {
            return "MODERATE_BURDEN";
        }

        return "HEALTHY";
    }

    public String buildExplanation(
            BigDecimal debtPaymentRatio,
            Integer overdueCount,
            BigDecimal outstandingDebt
    ) {

        if (overdueCount != null && overdueCount > 0) {
            return "One or more liabilities are overdue and require attention.";
        }

        if (outstandingDebt == null
                || outstandingDebt.compareTo(BigDecimal.ZERO) == 0) {
            return "No recognized outstanding debt is currently recorded.";
        }

        if (debtPaymentRatio == null) {
            return "Recognized debt exists, but the debt-payment ratio cannot be calculated because income data is unavailable.";
        }

        if (debtPaymentRatio.compareTo(BigDecimal.valueOf(40)) > 0) {
            return "Debt payments represent a significant share of current income. This is an analytical indicator, not a universal financial rule.";
        }

        if (debtPaymentRatio.compareTo(BigDecimal.valueOf(20)) > 0) {
            return "Debt payments represent a moderate share of current income. Review the relationship between debt obligations and available cash flow.";
        }

        return "Debt payments currently represent a relatively smaller share of current income. Continue monitoring repayment obligations.";
    }

    private Integer calculateOverdueCount() {

        /*
         * DebtBurdenResponse currently exposes the aggregated burden,
         * but not the individual overdue-liability count.
         *
         * The existing burden service therefore cannot provide this
         * value directly yet.
         *
         * Keep the integration safe until overdue aggregation is
         * exposed by the liability domain.
         */
        return 0;
    }

    private BigDecimal normalize(BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal normalizeNullable(BigDecimal value) {

        if (value == null) {
            return null;
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }
}