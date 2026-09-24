package com.financeos.financeosbackend.notification.integration.liability;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class LiabilityNotificationEventFactory {

    private LiabilityNotificationEventFactory() {
    }

    public static FinancialEvent paymentDue(
            Long userId,
            Long liabilityId,
            String liabilityName,
            String message
    ) {

        return new FinancialEvent(
                "LIABILITY-DUE-" + liabilityId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.LIABILITY_PAYMENT_DUE,
                NotificationSource.LIABILITY,
                "LIABILITY",
                liabilityId,
                "Liability Payment Due",
                message,
                new FinancialEventAction(
                        "OPEN_LIABILITY",
                        "/liabilities/" + liabilityId
                ),
                LocalDateTime.now(),
                null
        );
    }

    public static FinancialEvent paymentOverdue(
            Long userId,
            Long liabilityId,
            String liabilityName,
            String message
    ) {

        return new FinancialEvent(
                "LIABILITY-OVERDUE-" + liabilityId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.LIABILITY_PAYMENT_OVERDUE,
                NotificationSource.LIABILITY,
                "LIABILITY",
                liabilityId,
                "Liability Payment Overdue",
                message,
                new FinancialEventAction(
                        "OPEN_LIABILITY",
                        "/liabilities/" + liabilityId
                ),
                LocalDateTime.now(),
                null
        );
    }
}