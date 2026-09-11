package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.PaymentFrequency;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DebtCalculationService {

    private final LiabilityRepaymentRepository repaymentRepository;

    public DebtCalculationService(
            LiabilityRepaymentRepository repaymentRepository
    ) {
        this.repaymentRepository = repaymentRepository;
    }

    public BigDecimal calculateTotalPrincipalPaid(Liability liability) {
        return sumPrincipal(getRepayments(liability));
    }

    public BigDecimal calculateTotalInterestPaid(Liability liability) {
        return sumInterest(getRepayments(liability));
    }

    public BigDecimal calculateTotalPaid(Liability liability) {
        return sumPayments(getRepayments(liability));
    }

    public BigDecimal calculateRemainingAmount(Liability liability) {
        return scale(liability.getOutstandingAmount());
    }

    public BigDecimal calculateMonthlyPayment(Liability liability) {

        if (liability.getPaymentAmount() == null
                || liability.getPaymentFrequency() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal payment = liability.getPaymentAmount();

        return switch (liability.getPaymentFrequency()) {
            case MONTHLY -> payment;
            case QUARTERLY -> payment.divide(
                    BigDecimal.valueOf(3),
                    2,
                    RoundingMode.HALF_UP
            );
            case HALF_YEARLY -> payment.divide(
                    BigDecimal.valueOf(6),
                    2,
                    RoundingMode.HALF_UP
            );
            case YEARLY -> payment.divide(
                    BigDecimal.valueOf(12),
                    2,
                    RoundingMode.HALF_UP
            );
            case IRREGULAR -> BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        };
    }

    public Integer calculateRemainingTenureMonths(Liability liability) {

        if (liability.getOutstandingAmount() == null
                || liability.getOutstandingAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        if (liability.getPaymentAmount() == null
                || liability.getPaymentAmount()
                .compareTo(BigDecimal.ZERO) <= 0
                || liability.getPaymentFrequency() == null) {
            return null;
        }

        BigDecimal monthlyPayment = calculateMonthlyPayment(liability);

        if (monthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        if (liability.getInterestRate() == null
                || liability.getInterestRate()
                .compareTo(BigDecimal.ZERO) <= 0) {

            return liability.getOutstandingAmount()
                    .divide(monthlyPayment, 0, RoundingMode.CEILING)
                    .intValue();
        }

        BigDecimal monthlyRate =
                liability.getInterestRate()
                        .divide(
                                BigDecimal.valueOf(1200),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal interestOnly =
                liability.getOutstandingAmount()
                        .multiply(monthlyRate);

        if (monthlyPayment.compareTo(interestOnly) <= 0) {
            return null;
        }

        double principal =
                monthlyPayment.doubleValue();

        double balance =
                liability.getOutstandingAmount().doubleValue();

        double rate =
                monthlyRate.doubleValue();

        double months =
                -Math.log(
                        1 - (balance * rate / principal)
                ) / Math.log(1 + rate);

        return (int) Math.ceil(months);
    }

    public LocalDate calculateNextPaymentDate(
            Liability liability,
            LocalDate referenceDate
    ) {

        if (liability.getNextPaymentDate() != null) {
            return liability.getNextPaymentDate();
        }

        if (liability.getStartDate() == null
                || liability.getPaymentFrequency() == null) {
            return null;
        }

        LocalDate startDate = liability.getStartDate();

        if (startDate.isAfter(referenceDate)) {
            return startDate;
        }

        Period period = switch (liability.getPaymentFrequency()) {
            case MONTHLY -> Period.ofMonths(1);
            case QUARTERLY -> Period.ofMonths(3);
            case HALF_YEARLY -> Period.ofMonths(6);
            case YEARLY -> Period.ofYears(1);
            case IRREGULAR -> null;
        };

        if (period == null) {
            return null;
        }

        LocalDate nextDate = startDate;

        while (!nextDate.isAfter(referenceDate)) {
            nextDate = nextDate.plus(period);
        }

        return nextDate;
    }

    private List<LiabilityRepayment> getRepayments(
            Liability liability
    ) {
        return repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability);
    }

    private BigDecimal sumPrincipal(
            List<LiabilityRepayment> repayments
    ) {
        return repayments.stream()
                .map(LiabilityRepayment::getPrincipalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumInterest(
            List<LiabilityRepayment> repayments
    ) {
        return repayments.stream()
                .map(LiabilityRepayment::getInterestAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumPayments(
            List<LiabilityRepayment> repayments
    ) {
        return repayments.stream()
                .map(LiabilityRepayment::getPaymentAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal scale(BigDecimal value) {
        return value == null
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : value.setScale(2, RoundingMode.HALF_UP);
    }
}