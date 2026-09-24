package com.financeos.financeosbackend.notification.management;

public class NotificationNotFoundException
        extends RuntimeException {

    public NotificationNotFoundException(
            Long notificationId
    ) {
        super(
                "Notification not found: "
                        + notificationId
        );
    }
}