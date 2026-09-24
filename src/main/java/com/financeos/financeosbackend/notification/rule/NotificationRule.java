package com.financeos.financeosbackend.notification.rule;

import com.financeos.financeosbackend.notification.event.FinancialEvent;

public interface NotificationRule {

    boolean supports(FinancialEvent event);

    NotificationRuleResult evaluate(FinancialEvent event);
}