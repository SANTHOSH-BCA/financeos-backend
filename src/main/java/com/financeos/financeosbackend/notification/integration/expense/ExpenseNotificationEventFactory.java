package com.financeos.financeosbackend.notification.integration.expense;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class ExpenseNotificationEventFactory {

    private ExpenseNotificationEventFactory() {
    }

    public static FinancialEvent expenseRequiresAttention(
            Long userId,
            Long expenseId,
            String message
    ) {

        return new FinancialEvent(
                "EXPENSE-ATTENTION-" + expenseId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.EXPENSE_REQUIRES_ATTENTION,
                NotificationSource.EXPENSE,
                "EXPENSE",
                expenseId,
                "Expense Requires Attention",
                message,
                new FinancialEventAction(
                        "OPEN_EXPENSE",
                        "/expenses/" + expenseId
                ),
                LocalDateTime.now(),
                null
        );
    }
}