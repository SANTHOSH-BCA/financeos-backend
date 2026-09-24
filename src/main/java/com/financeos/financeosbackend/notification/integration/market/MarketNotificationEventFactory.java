package com.financeos.financeosbackend.notification.integration.market;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class MarketNotificationEventFactory {

    private MarketNotificationEventFactory() {
    }

    public static FinancialEvent marketAlert(
            Long userId,
            String message
    ) {

        return new FinancialEvent(
                "MARKET-ALERT-" + userId + "-"
                        + System.nanoTime(),
                userId,
                FinancialEventType.MARKET_ALERT,
                NotificationSource.MARKET,
                null,
                null,
                "Important Market Alert",
                message,
                new FinancialEventAction(
                        "OPEN_MARKET",
                        "/market"
                ),
                LocalDateTime.now(),
                null
        );
    }
}