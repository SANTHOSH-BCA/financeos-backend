package com.financeos.financeosbackend.notification.integration.expense;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult expenseRequiresAttention(
            Long userId,
            Long expenseId,
            String message
    ) {

        FinancialEvent event =
                ExpenseNotificationEventFactory
                        .expenseRequiresAttention(
                                userId,
                                expenseId,
                                message
                        );

        return financialEventNotificationService.process(event);
    }
}