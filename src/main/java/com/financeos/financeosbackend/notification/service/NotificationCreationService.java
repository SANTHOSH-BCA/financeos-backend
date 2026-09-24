package com.financeos.financeosbackend.notification.service;

import com.financeos.financeosbackend.notification.deduplication.NotificationDeduplicationService;
import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCreationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserRepository userRepository;

    private final NotificationDeduplicationService
            deduplicationService;

    @Transactional
    public NotificationCreationResult create(
            FinancialEvent event,
            NotificationPriority priority
    ) {

        if (deduplicationService.isDuplicate(
                event.userId(),
                buildDeduplicationKey(event)
        )) {
            return NotificationCreationResult.skipped();
        }

        User user = userRepository.findById(event.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found: "
                                        + event.userId()
                        )
                );

        Notification notification =
                notificationMapper.toEntity(
                        event,
                        user,
                        priority
                );

        Notification saved =
                notificationRepository.save(notification);

        return NotificationCreationResult.created(saved);
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