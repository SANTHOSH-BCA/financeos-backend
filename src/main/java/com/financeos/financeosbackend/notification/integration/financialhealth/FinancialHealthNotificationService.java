package com.financeos.financeosbackend.notification.integration.financialhealth;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialHealthNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult healthChanged(
            Long userId,
            String message
    ) {

        FinancialEvent event =
                FinancialHealthNotificationEventFactory
                        .healthChanged(
                                userId,
                                message
                        );

        return financialEventNotificationService.process(event);
    }
}