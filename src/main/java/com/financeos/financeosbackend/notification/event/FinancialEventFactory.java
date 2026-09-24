package com.financeos.financeosbackend.notification.event;

import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;
import java.util.UUID;

public final class FinancialEventFactory {

    private FinancialEventFactory() {
    }

    public static FinancialEvent create(
            Long userId,
            FinancialEventType eventType,
            NotificationSource sourceModule,
            String relatedEntityType,
            Long relatedEntityId,
            String title,
            String message,
            FinancialEventAction action,
            LocalDateTime expiresAt
    ) {

        return new FinancialEvent(
                UUID.randomUUID().toString(),
                userId,
                eventType,
                sourceModule,
                relatedEntityType,
                relatedEntityId,
                title,
                message,
                action,
                LocalDateTime.now(),
                expiresAt
        );
    }
}