package com.financeos.financeosbackend.notification.event;

import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public record FinancialEvent(

        String eventId,

        Long userId,

        FinancialEventType eventType,

        NotificationSource sourceModule,

        String relatedEntityType,

        Long relatedEntityId,

        String title,

        String message,

        FinancialEventAction action,

        LocalDateTime occurredAt,

        LocalDateTime expiresAt

) {
}