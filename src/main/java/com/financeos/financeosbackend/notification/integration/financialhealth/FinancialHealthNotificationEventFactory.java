package com.financeos.financeosbackend.notification.integration.financialhealth;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class FinancialHealthNotificationEventFactory {

    private FinancialHealthNotificationEventFactory() {
    }

    public static FinancialEvent healthChanged(
            Long userId,
            String message
    ) {

        return new FinancialEvent(
                "FINANCIAL-HEALTH-" + userId + "-"
                        + System.nanoTime(),
                userId,
                FinancialEventType.FINANCIAL_HEALTH_CHANGED,
                NotificationSource.FINANCIAL_HEALTH,
                null,
                null,
                "Financial Health Update",
                message,
                new FinancialEventAction(
                        "OPEN_FINANCIAL_HEALTH",
                        "/dashboard"
                ),
                LocalDateTime.now(),
                null
        );
    }
}