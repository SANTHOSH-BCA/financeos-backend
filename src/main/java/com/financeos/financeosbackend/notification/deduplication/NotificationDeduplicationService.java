package com.financeos.financeosbackend.notification.deduplication;

import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class NotificationDeduplicationService {

    private final NotificationRepository notificationRepository;

    public boolean isDuplicate(
            Long userId,
            String deduplicationKey
    ) {

        if (deduplicationKey == null
                || deduplicationKey.isBlank()) {

            return false;
        }

        return notificationRepository
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        userId,
                        deduplicationKey,
                        EnumSet.of(
                                NotificationStatus.UNREAD,
                                NotificationStatus.READ
                        )
                );
    }
}