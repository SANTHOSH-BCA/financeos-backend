package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.event.LiabilityNotificationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LiabilityNotificationEventService {

    private final ApplicationEventPublisher eventPublisher;

    public LiabilityNotificationEventService(
            ApplicationEventPublisher eventPublisher
    ) {
        this.eventPublisher = eventPublisher;
    }

    public void publishUpcomingPaymentEvent(
            Liability liability
    ) {

        if (liability == null
                || liability.getUser() == null
                || liability.getNextPaymentDate() == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        if (liability.getNextPaymentDate().isBefore(today)) {
            return;
        }

        long daysUntilPayment =
                ChronoUnit.DAYS.between(
                        today,
                        liability.getNextPaymentDate()
                );

        if (daysUntilPayment > 7) {
            return;
        }

        String message =
                "Upcoming payment for "
                        + liability.getLiabilityName()
                        + " is due on "
                        + liability.getNextPaymentDate()
                        + ".";

        publish(
                liability,
                LiabilityNotificationEvent.EventType.UPCOMING_PAYMENT,
                message
        );
    }

    public void publishPaymentOverdueEvent(
            Liability liability
    ) {

        if (liability == null
                || liability.getUser() == null
                || liability.getNextPaymentDate() == null) {
            return;
        }

        if (!liability.getNextPaymentDate().isBefore(LocalDate.now())) {
            return;
        }

        String message =
                "Payment for "
                        + liability.getLiabilityName()
                        + " appears to be overdue.";

        publish(
                liability,
                LiabilityNotificationEvent.EventType.PAYMENT_OVERDUE,
                message
        );
    }

    public void publishRepaymentMilestoneEvent(
            Liability liability,
            BigDecimal previousOutstandingAmount
    ) {

        if (liability == null
                || liability.getUser() == null
                || previousOutstandingAmount == null
                || liability.getOutstandingAmount() == null
                || previousOutstandingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal currentOutstanding =
                liability.getOutstandingAmount();

        BigDecimal paidAmount =
                previousOutstandingAmount.subtract(currentOutstanding);

        BigDecimal previousPaidPercentage =
                calculatePaidPercentage(
                        previousOutstandingAmount,
                        previousOutstandingAmount
                );

        BigDecimal currentPaidPercentage =
                calculatePaidPercentage(
                        previousOutstandingAmount,
                        paidAmount
                );

        if (crossedMilestone(previousPaidPercentage, currentPaidPercentage, 50)
                || crossedMilestone(previousPaidPercentage, currentPaidPercentage, 75)
                || crossedMilestone(previousPaidPercentage, currentPaidPercentage, 100)) {

            String message =
                    "Repayment milestone reached for "
                            + liability.getLiabilityName()
                            + ".";

            publish(
                    liability,
                    LiabilityNotificationEvent.EventType.REPAYMENT_MILESTONE,
                    message
            );
        }
    }

    public void publishLoanNearingCompletionEvent(
            Liability liability
    ) {

        if (liability == null
                || liability.getUser() == null
                || liability.getOutstandingAmount() == null
                || liability.getPaymentAmount() == null) {
            return;
        }

        if (liability.getOutstandingAmount().compareTo(BigDecimal.ZERO) <= 0
                || liability.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal remainingPayments =
                liability.getOutstandingAmount()
                        .divide(
                                liability.getPaymentAmount(),
                                0,
                                java.math.RoundingMode.CEILING
                        );

        if (remainingPayments.compareTo(BigDecimal.valueOf(3)) > 0) {
            return;
        }

        String message =
                "Loan "
                        + liability.getLiabilityName()
                        + " is nearing completion.";

        publish(
                liability,
                LiabilityNotificationEvent.EventType.LOAN_NEARING_COMPLETION,
                message
        );
    }

    private void publish(
            Liability liability,
            LiabilityNotificationEvent.EventType eventType,
            String message
    ) {

        LiabilityNotificationEvent event =
                new LiabilityNotificationEvent(
                        liability.getUser().getId(),
                        liability.getId(),
                        liability.getLiabilityName(),
                        eventType,
                        liability.getStatus(),
                        liability.getOutstandingAmount(),
                        liability.getPaymentAmount(),
                        liability.getNextPaymentDate(),
                        message
                );

        eventPublisher.publishEvent(event);
    }

    private BigDecimal calculatePaidPercentage(
            BigDecimal originalAmount,
            BigDecimal paidAmount
    ) {

        if (originalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return paidAmount
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        originalAmount,
                        2,
                        java.math.RoundingMode.HALF_UP
                );
    }

    private boolean crossedMilestone(
            BigDecimal previousPercentage,
            BigDecimal currentPercentage,
            int milestone
    ) {

        BigDecimal milestoneValue =
                BigDecimal.valueOf(milestone);

        return previousPercentage.compareTo(milestoneValue) < 0
                && currentPercentage.compareTo(milestoneValue) >= 0;
    }
}