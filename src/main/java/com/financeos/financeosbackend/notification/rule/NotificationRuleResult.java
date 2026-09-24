package com.financeos.financeosbackend.notification.rule;

import com.financeos.financeosbackend.notification.enums.NotificationPriority;

public record NotificationRuleResult(
        boolean shouldNotify,
        NotificationPriority priority
) {

    public static NotificationRuleResult notifyWith(
            NotificationPriority priority
    ) {
        return new NotificationRuleResult(
                true,
                priority
        );
    }

    public static NotificationRuleResult skip() {
        return new NotificationRuleResult(
                false,
                null
        );
    }
}