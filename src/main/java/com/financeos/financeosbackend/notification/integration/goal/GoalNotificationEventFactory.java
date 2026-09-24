package com.financeos.financeosbackend.notification.integration.goal;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;

import java.time.LocalDateTime;

public final class GoalNotificationEventFactory {

    private GoalNotificationEventFactory() {
    }

    public static FinancialEvent goalProgressChanged(
            Long userId,
            Long goalId,
            String goalName,
            String message
    ) {

        return new FinancialEvent(
                "GOAL-PROGRESS-" + goalId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.GOAL_PROGRESS_CHANGED,
                NotificationSource.GOAL,
                "GOAL",
                goalId,
                "Goal Progress Updated",
                message,
                new FinancialEventAction(
                        "OPEN_GOAL",
                        "/goals/" + goalId
                ),
                LocalDateTime.now(),
                null
        );
    }

    public static FinancialEvent goalFallingBehind(
            Long userId,
            Long goalId,
            String goalName
    ) {

        return new FinancialEvent(
                "GOAL-BEHIND-" + goalId + "-" + System.nanoTime(),
                userId,
                FinancialEventType.GOAL_FALLING_BEHIND,
                NotificationSource.GOAL,
                "GOAL",
                goalId,
                "Goal Falling Behind",
                "Your " + goalName + " goal is falling behind.",
                new FinancialEventAction(
                        "OPEN_GOAL",
                        "/goals/" + goalId
                ),
                LocalDateTime.now(),
                null
        );
    }
}