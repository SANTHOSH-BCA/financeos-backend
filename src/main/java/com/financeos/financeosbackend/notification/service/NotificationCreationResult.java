package com.financeos.financeosbackend.notification.service;

import com.financeos.financeosbackend.notification.entity.Notification;

public record NotificationCreationResult(
        boolean created,
        Notification notification
) {

    public static NotificationCreationResult created(
            Notification notification
    ) {
        return new NotificationCreationResult(true, notification);
    }

    public static NotificationCreationResult skipped() {
        return new NotificationCreationResult(false, null);
    }
}