package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.notification.enums.NotificationSource;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventFactory;
import com.financeos.financeosbackend.notification.event.FinancialEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class InsuranceNotificationEventFactory {

    private InsuranceNotificationEventFactory() {
    }

    public static FinancialEvent premiumDue(
            Long userId,
            Long policyId,
            LocalDate dueDate
    ) {
        return create(
                userId,
                policyId,
                "INSURANCE_PREMIUM_DUE",
                "Insurance Premium Due",
                dueDate == null
                        ? "An insurance premium payment is due. Review your insurance details."
                        : "An insurance premium payment is due on "
                                + dueDate + ". Review your insurance details.",
                LocalDateTime.now().plusDays(30)
        );
    }

    public static FinancialEvent premiumOverdue(
            Long userId,
            Long policyId,
            LocalDate dueDate
    ) {
        return create(
                userId,
                policyId,
                "INSURANCE_PREMIUM_OVERDUE",
                "Insurance Premium Overdue",
                dueDate == null
                        ? "An insurance premium payment may be overdue. Review your insurance details."
                        : "An insurance premium payment due on "
                                + dueDate + " is overdue. Review your insurance details.",
                LocalDateTime.now().plusDays(30)
        );
    }

    public static FinancialEvent policyExpiring(
            Long userId,
            Long policyId,
            LocalDate expiryDate
    ) {
        return create(
                userId,
                policyId,
                "INSURANCE_POLICY_EXPIRING",
                "Insurance Policy Expiring",
                expiryDate == null
                        ? "An insurance policy may require renewal. Review your insurance details."
                        : "An insurance policy is approaching its expiry date of "
                                + expiryDate + ". Review renewal options.",
                expiryDate == null
                        ? LocalDateTime.now().plusDays(30)
                        : expiryDate.plusDays(1).atStartOfDay()
        );
    }

    public static FinancialEvent policyExpired(
            Long userId,
            Long policyId,
            LocalDate expiryDate
    ) {
        String message = expiryDate == null
                ? "An insurance policy has expired. Review your insurance details."
                : "An insurance policy expired on "
                        + expiryDate + ". Review renewal or replacement options.";

        return FinancialEventFactory.create(
                userId,
                FinancialEventType.INSURANCE_POLICY_EXPIRED,
                NotificationSource.INSURANCE,
                "INSURANCE_POLICY_EXPIRED",
                policyId,
                "Insurance Policy Expired",
                message,
                new FinancialEventAction(
                        "OPEN_INSURANCE_POLICY",
                        "/insurance/" + policyId
                ),
                LocalDateTime.now().plusDays(30)
        );
    }
    private static FinancialEvent create(
            Long userId,
            Long policyId,
            String relatedEntityType,
            String title,
            String message,
            LocalDateTime expiresAt
    ) {
        return FinancialEventFactory.create(
                userId,
                FinancialEventType.INSURANCE_EXPIRING,
                NotificationSource.INSURANCE,
                relatedEntityType,
                policyId,
                title,
                message,
                new FinancialEventAction(
                        "OPEN_INSURANCE_POLICY",
                        "/insurance/" + policyId
                ),
                expiresAt
        );
    }
}