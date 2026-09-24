package com.financeos.financeosbackend.notification.dto;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.enums.NotificationSource;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        NotificationPriority priority,
        NotificationStatus status,
        String title,
        String message,
        NotificationSource sourceModule,
        String relatedEntityType,
        Long relatedEntityId,
        String actionType,
        String actionTarget,
        LocalDateTime createdAt,
        LocalDateTime readAt,
        LocalDateTime actionedAt,
        LocalDateTime expiresAt
) {

    public static NotificationResponse from(
            Notification notification
    ) {

        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getPriority(),
                notification.getStatus(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getSourceModule(),
                notification.getRelatedEntityType(),
                notification.getRelatedEntityId(),
                notification.getActionType(),
                notification.getActionTarget(),
                notification.getCreatedAt(),
                notification.getReadAt(),
                notification.getActionedAt(),
                notification.getExpiresAt()
        );
    }
}