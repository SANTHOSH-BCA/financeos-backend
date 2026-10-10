package com.financeos.financeosbackend.notification.integration.insurance;

public record InsuranceReminderProcessingResult(
        int policiesExamined,
        int notificationsCreated,
        int notificationsSkipped
) {
}