package com.financeos.financeosbackend.notification.rule;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationRuleEngine {

    private final List<NotificationRule> rules;

    public NotificationRuleEngine(
            List<NotificationRule> rules
    ) {
        this.rules = rules;
    }

    public NotificationRuleResult evaluate(
            FinancialEvent event
    ) {

        for (NotificationRule rule : rules) {

            if (rule.supports(event)) {
                return rule.evaluate(event);
            }
        }

        return defaultResult(event);
    }

    private NotificationRuleResult defaultResult(
            FinancialEvent event
    ) {

        return switch (event.eventType()) {

            case GOAL_FALLING_BEHIND,
                 LIABILITY_PAYMENT_OVERDUE,
                 HELP_RETURN_REQUIRED ->
                    NotificationRuleResult.notifyWith(
                            NotificationPriority.CRITICAL
                    );

            case GOAL_PROGRESS_CHANGED,
                 INVESTMENT_UPDATED,
                 EXPENSE_REQUIRES_ATTENTION,
                 TRANSACTION_REQUIRES_REVIEW,
                 LIABILITY_PAYMENT_DUE,
                 CASH_FLOW_ALERT,
                 FINANCIAL_HEALTH_CHANGED,
                 MARKET_ALERT,
                 INSURANCE_EXPIRING ->
                    NotificationRuleResult.notifyWith(
                            NotificationPriority.IMPORTANT
                    );

            case DAILY_FINANCIAL_UPDATE,
                 DAILY_INVESTMENT_UPDATE,
                 FINANCIAL_EVENT_UPCOMING ->
                    NotificationRuleResult.notifyWith(
                            NotificationPriority.INFORMATIONAL
                    );

            default ->
                    NotificationRuleResult.skip();
        };
    }
}