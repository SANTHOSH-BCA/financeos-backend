package com.financeos.financeosbackend.notification.controller;

import com.financeos.financeosbackend.notification.dto.NotificationPageResponse;
import com.financeos.financeosbackend.notification.dto.NotificationResponse;
import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationPageResponse getNotifications(
            Long userId,
            NotificationStatus status,
            int page,
            int size
    ) {

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<Notification> result;

        if (status == null) {

            result = notificationRepository
                    .findByUserIdOrderByCreatedAtDesc(
                            userId,
                            pageable
                    );

        } else {

            result = notificationRepository
                    .findByUserIdAndStatusOrderByCreatedAtDesc(
                            userId,
                            status,
                            pageable
                    );
        }

        return new NotificationPageResponse(
                result.getContent()
                        .stream()
                        .map(NotificationResponse::from)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(
            Long userId
    ) {

        return notificationRepository
                .countByUserIdAndStatus(
                        userId,
                        NotificationStatus.UNREAD
                );
    }
}