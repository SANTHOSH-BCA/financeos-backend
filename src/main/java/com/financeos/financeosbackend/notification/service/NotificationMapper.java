package com.financeos.financeosbackend.notification.service;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(
            FinancialEvent event,
            User user,
            NotificationPriority priority
    ) {

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setType(
                switch (event.eventType()) {
                    case INVESTMENT_UPDATED,
                         DAILY_INVESTMENT_UPDATE ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.INVESTMENT_UPDATE;

                    case GOAL_PROGRESS_CHANGED,
                         GOAL_FALLING_BEHIND ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.GOAL_ALERT;

                    case EXPENSE_REQUIRES_ATTENTION ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.EXPENSE_ALERT;

                    case TRANSACTION_REQUIRES_REVIEW ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.TRANSACTION_ALERT;

                    case HELP_RETURN_REQUIRED ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.HELP_RETURN_REMINDER;

                    case LIABILITY_PAYMENT_DUE,
                         LIABILITY_PAYMENT_OVERDUE ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.UPCOMING_FINANCIAL_EVENT;

                    case CASH_FLOW_ALERT ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.UPCOMING_FINANCIAL_EVENT;

                    case FINANCIAL_HEALTH_CHANGED ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.FINANCIAL_HEALTH_ALERT;

                    case MARKET_ALERT ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.MARKET_ALERT;

                    case INSURANCE_EXPIRING ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.INSURANCE_REMINDER;

                    case FINANCIAL_EVENT_UPCOMING ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.UPCOMING_FINANCIAL_EVENT;

                    case DAILY_FINANCIAL_UPDATE ->
                            com.financeos.financeosbackend.notification.enums.NotificationType.DAILY_FINANCIAL_UPDATE;
                }
        );

        notification.setPriority(priority);
        notification.setStatus(NotificationStatus.UNREAD);

        notification.setTitle(event.title());
        notification.setMessage(event.message());

        notification.setSourceModule(event.sourceModule());
        notification.setSourceEventId(event.eventId());

        notification.setRelatedEntityType(
                event.relatedEntityType()
        );

        notification.setRelatedEntityId(
                event.relatedEntityId()
        );

        if (event.action() != null) {
            notification.setActionType(
                    event.action().actionType()
            );

            notification.setActionTarget(
                    event.action().actionTarget()
            );
        }

        notification.setDeduplicationKey(
                buildDeduplicationKey(event)
        );

        notification.setExpiresAt(
                event.expiresAt()
        );

        return notification;
    }

    private String buildDeduplicationKey(
            FinancialEvent event
    ) {
        return event.sourceModule()
                + ":"
                + event.eventType()
                + ":"
                + event.relatedEntityType()
                + ":"
                + event.relatedEntityId();
    }
}