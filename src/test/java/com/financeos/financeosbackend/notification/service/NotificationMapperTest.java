package com.financeos.financeosbackend.notification.service;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventAction;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationSource;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private final NotificationMapper mapper =
            new NotificationMapper();

    @Test
    void shouldMapFinancialEventToNotification() {

        User user = new User();
        user.setId(1L);

        FinancialEvent event =
                new FinancialEvent(
                        "event-123",
                        1L,
                        FinancialEventType.GOAL_FALLING_BEHIND,
                        NotificationSource.GOAL,
                        "GOAL",
                        42L,
                        "Goal Falling Behind",
                        "House goal is falling behind.",
                        new FinancialEventAction(
                                "OPEN_GOAL",
                                "/goals/42"
                        ),
                        LocalDateTime.now(),
                        null
                );

        Notification notification =
                mapper.toEntity(
                        event,
                        user,
                        NotificationPriority.CRITICAL
                );

        assertEquals(user, notification.getUser());
        assertEquals(
                NotificationStatus.UNREAD,
                notification.getStatus()
        );
        assertEquals(
                NotificationPriority.CRITICAL,
                notification.getPriority()
        );
        assertEquals(
                "Goal Falling Behind",
                notification.getTitle()
        );
        assertEquals(
                "House goal is falling behind.",
                notification.getMessage()
        );
        assertEquals(
                NotificationSource.GOAL,
                notification.getSourceModule()
        );
        assertEquals(
                "GOAL",
                notification.getRelatedEntityType()
        );
        assertEquals(
                42L,
                notification.getRelatedEntityId()
        );
        assertEquals(
                "OPEN_GOAL",
                notification.getActionType()
        );
        assertEquals(
                "/goals/42",
                notification.getActionTarget()
        );
    }
}