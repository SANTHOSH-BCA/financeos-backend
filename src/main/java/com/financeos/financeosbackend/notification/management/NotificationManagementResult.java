package com.financeos.financeosbackend.notification.management;

public record NotificationManagementResult(
        boolean success,
        String message
) {

    public static NotificationManagementResult success(
            String message
    ) {
        return new NotificationManagementResult(
                true,
                message
        );
    }
}