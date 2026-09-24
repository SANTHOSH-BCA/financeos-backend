package com.financeos.financeosbackend.notification.management;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationManagementService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationManagementResult markAsRead(
            Long userId,
            Long notificationId
    ) {

        Notification notification =
                getUserNotification(
                        userId,
                        notificationId
                );

        if (notification.getStatus()
                == NotificationStatus.UNREAD) {

            notification.setStatus(
                    NotificationStatus.READ
            );

            notification.setReadAt(
                    LocalDateTime.now()
            );

            notificationRepository.save(notification);
        }

        return NotificationManagementResult.success(
                "Notification marked as read"
        );
    }

    @Transactional
    public NotificationManagementResult markAsActioned(
            Long userId,
            Long notificationId
    ) {

        Notification notification =
                getUserNotification(
                        userId,
                        notificationId
                );

        notification.setStatus(
                NotificationStatus.ACTIONED
        );

        notification.setActionedAt(
                LocalDateTime.now()
        );

        if (notification.getReadAt() == null) {
            notification.setReadAt(
                    LocalDateTime.now()
            );
        }

        notificationRepository.save(notification);

        return NotificationManagementResult.success(
                "Notification marked as actioned"
        );
    }

    @Transactional
    public NotificationManagementResult dismiss(
            Long userId,
            Long notificationId
    ) {

        Notification notification =
                getUserNotification(
                        userId,
                        notificationId
                );

        notification.setStatus(
                NotificationStatus.DISMISSED
        );

        notificationRepository.save(notification);

        return NotificationManagementResult.success(
                "Notification dismissed"
        );
    }

    private Notification getUserNotification(
            Long userId,
            Long notificationId
    ) {

        Notification notification =
                notificationRepository.findById(
                        notificationId
                ).orElseThrow(() ->
                        new NotificationNotFoundException(
                                notificationId
                        )
                );

        if (!notification.getUser()
                .getId()
                .equals(userId)) {

            throw new NotificationNotFoundException(
                    notificationId
            );
        }

        return notification;
    }
}