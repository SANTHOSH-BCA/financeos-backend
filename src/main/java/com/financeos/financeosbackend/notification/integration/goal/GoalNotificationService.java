package com.financeos.financeosbackend.notification.integration.goal;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalNotificationService {

    private final FinancialEventNotificationService
            financialEventNotificationService;

    public FinancialEventNotificationResult process(
            FinancialEvent event
    ) {

        return financialEventNotificationService.process(event);
    }

    public FinancialEventNotificationResult goalProgressChanged(
            Long userId,
            Long goalId,
            String goalName,
            String message
    ) {

        FinancialEvent event =
                GoalNotificationEventFactory.goalProgressChanged(
                        userId,
                        goalId,
                        goalName,
                        message
                );

        return process(event);
    }

    public FinancialEventNotificationResult goalFallingBehind(
            Long userId,
            Long goalId,
            String goalName
    ) {

        FinancialEvent event =
                GoalNotificationEventFactory.goalFallingBehind(
                        userId,
                        goalId,
                        goalName
                );

        return process(event);
    }
}