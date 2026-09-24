package com.financeos.financeosbackend.notification.integration.investment;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class InvestmentNotificationEventFactory {

    private InvestmentNotificationEventFactory() {
    }

    public static FinancialEvent investmentUpdated(
            Long userId,
            Long investmentId,
            String investmentName,
            String message
    ) {

        return new FinancialEvent(
                "INVESTMENT-UPDATED-" + investmentId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.INVESTMENT_UPDATED,
                NotificationSource.INVESTMENT,
                "INVESTMENT",
                investmentId,
                "Investment Update",
                message,
                new FinancialEventAction(
                        "OPEN_INVESTMENT",
                        "/investments/" + investmentId
                ),
                LocalDateTime.now(),
                null
        );
    }

    public static FinancialEvent dailyInvestmentUpdate(
            Long userId,
            String message
    ) {

        return new FinancialEvent(
                "DAILY-INVESTMENT-" + userId + "-"
                        + LocalDateTime.now().toLocalDate(),
                userId,
                FinancialEventType.DAILY_INVESTMENT_UPDATE,
                NotificationSource.INVESTMENT,
                null,
                null,
                "Daily Investment Update",
                message,
                new FinancialEventAction(
                        "OPEN_INVESTMENTS",
                        "/investments"
                ),
                LocalDateTime.now(),
                null
        );
    }
}