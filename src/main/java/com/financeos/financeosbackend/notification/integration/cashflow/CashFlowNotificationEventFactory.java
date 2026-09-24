package com.financeos.financeosbackend.notification.integration.cashflow;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class CashFlowNotificationEventFactory {

    private CashFlowNotificationEventFactory() {
    }

    public static FinancialEvent cashFlowAlert(
            Long userId,
            String message
    ) {

        return new FinancialEvent(
                "CASH-FLOW-ALERT-" + userId + "-"
                        + System.nanoTime(),
                userId,
                FinancialEventType.CASH_FLOW_ALERT,
                NotificationSource.CASH_FLOW,
                null,
                null,
                "Cash Flow Alert",
                message,
                new FinancialEventAction(
                        "OPEN_CASH_FLOW",
                        "/money/cash-flow"
                ),
                LocalDateTime.now(),
                null
        );
    }
}