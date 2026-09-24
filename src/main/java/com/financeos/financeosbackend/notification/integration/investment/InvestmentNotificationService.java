package com.financeos.financeosbackend.notification.integration.investment;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestmentNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult investmentUpdated(
            Long userId,
            Long investmentId,
            String investmentName,
            String message
    ) {

        FinancialEvent event =
                InvestmentNotificationEventFactory.investmentUpdated(
                        userId,
                        investmentId,
                        investmentName,
                        message
                );

        return financialEventNotificationService.process(event);
    }

    public FinancialEventNotificationResult dailyInvestmentUpdate(
            Long userId,
            String message
    ) {

        FinancialEvent event =
                InvestmentNotificationEventFactory.dailyInvestmentUpdate(
                        userId,
                        message
                );

        return financialEventNotificationService.process(event);
    }
}