package com.financeos.financeosbackend.notification.integration;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.rule.NotificationRuleEngine;
import com.financeos.financeosbackend.notification.rule.NotificationRuleResult;
import com.financeos.financeosbackend.notification.service.NotificationCreationResult;
import com.financeos.financeosbackend.notification.service.NotificationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinancialEventNotificationService {

    private final NotificationRuleEngine ruleEngine;

    private final NotificationCreationService
            notificationCreationService;

    @Transactional
    public FinancialEventNotificationResult process(
            FinancialEvent event
    ) {

        NotificationRuleResult ruleResult =
                ruleEngine.evaluate(event);

        if (!ruleResult.shouldNotify()) {
            return FinancialEventNotificationResult.skipped();
        }

        NotificationCreationResult creationResult =
                notificationCreationService.create(
                        event,
                        ruleResult.priority()
                );

        if (!creationResult.created()) {
            return FinancialEventNotificationResult.skipped();
        }

        Notification notification =
                creationResult.notification();

        return FinancialEventNotificationResult.created(
                notification
        );
    }
}