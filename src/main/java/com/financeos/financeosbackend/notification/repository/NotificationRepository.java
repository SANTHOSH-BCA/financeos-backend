package com.financeos.financeosbackend.notification.repository;

import com.financeos.financeosbackend.notification.entity.Notification;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    Page<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            NotificationStatus status,
            Pageable pageable
    );

    long countByUserIdAndStatus(
            Long userId,
            NotificationStatus status
    );

    boolean existsByUserIdAndDeduplicationKeyAndStatusIn(
            Long userId,
            String deduplicationKey,
            Iterable<NotificationStatus> statuses
    );
}