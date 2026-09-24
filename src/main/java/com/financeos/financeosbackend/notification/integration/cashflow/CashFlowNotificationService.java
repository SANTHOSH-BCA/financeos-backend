package com.financeos.financeosbackend.notification.integration.cashflow;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashFlowNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult cashFlowAlert(
            Long userId,
            String message
    ) {

        FinancialEvent event =
                CashFlowNotificationEventFactory.cashFlowAlert(
                        userId,
                        message
                );

        return financialEventNotificationService.process(event);
    }
}