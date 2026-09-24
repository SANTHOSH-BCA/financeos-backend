package com.financeos.financeosbackend.notification.integration.market;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult marketAlert(
            Long userId,
            String message
    ) {

        FinancialEvent event =
                MarketNotificationEventFactory.marketAlert(
                        userId,
                        message
                );

        return financialEventNotificationService.process(event);
    }
}