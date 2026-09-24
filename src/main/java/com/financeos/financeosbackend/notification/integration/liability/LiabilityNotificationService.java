package com.financeos.financeosbackend.notification.integration.liability;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiabilityNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult paymentDue(
            Long userId,
            Long liabilityId,
            String liabilityName,
            String message
    ) {

        FinancialEvent event =
                LiabilityNotificationEventFactory.paymentDue(
                        userId,
                        liabilityId,
                        liabilityName,
                        message
                );

        return financialEventNotificationService.process(event);
    }

    public FinancialEventNotificationResult paymentOverdue(
            Long userId,
            Long liabilityId,
            String liabilityName,
            String message
    ) {

        FinancialEvent event =
                LiabilityNotificationEventFactory.paymentOverdue(
                        userId,
                        liabilityId,
                        liabilityName,
                        message
                );

        return financialEventNotificationService.process(event);
    }
}