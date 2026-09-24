package com.financeos.financeosbackend.notification.integration;

import com.financeos.financeosbackend.notification.entity.Notification;

public record FinancialEventNotificationResult(
        boolean notificationCreated,
        Notification notification
) {

    public static FinancialEventNotificationResult created(
            Notification notification
    ) {
        return new FinancialEventNotificationResult(
                true,
                notification
        );
    }

    public static FinancialEventNotificationResult skipped() {
        return new FinancialEventNotificationResult(
                false,
                null
        );
    }
}