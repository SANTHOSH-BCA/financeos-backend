package com.financeos.financeosbackend.notification.event;

public record FinancialEventAction(
        String actionType,
        String actionTarget
) {
}