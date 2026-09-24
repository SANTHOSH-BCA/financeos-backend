package com.financeos.financeosbackend.notification.management;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;

public class NotificationNotFoundException
        extends ResourceNotFoundException {

    public NotificationNotFoundException(
            Long notificationId
    ) {
        super(
                "Notification not found: "
                        + notificationId
        );
    }
}