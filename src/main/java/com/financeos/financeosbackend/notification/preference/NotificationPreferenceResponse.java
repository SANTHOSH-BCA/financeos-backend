package com.financeos.financeosbackend.notification.preference;

public record NotificationPreferenceResponse(
        boolean investmentUpdates,
        boolean goalAlerts,
        boolean expenseAlerts,
        boolean helpReminders,
        boolean insuranceReminders,
        boolean dailyFinancialUpdate
) {

    public static NotificationPreferenceResponse from(
            NotificationPreference preference
    ) {
        return new NotificationPreferenceResponse(
                preference.isInvestmentUpdates(),
                preference.isGoalAlerts(),
                preference.isExpenseAlerts(),
                preference.isHelpReminders(),
                preference.isInsuranceReminders(),
                preference.isDailyFinancialUpdate()
        );
    }
}