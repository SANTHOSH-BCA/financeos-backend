package com.financeos.financeosbackend.notification.deduplication;

import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationDeduplicationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationDeduplicationService deduplicationService;

    @Test
    void shouldTreatUnreadNotificationAsDuplicate() {
        when(notificationRepository
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        eq(1L),
                        eq("INSURANCE:INSURANCE_EXPIRING:INSURANCE_PREMIUM_DUE:100"),
                        any()
                )).thenReturn(true);

        boolean result = deduplicationService.isDuplicate(
                1L,
                "INSURANCE:INSURANCE_EXPIRING:INSURANCE_PREMIUM_DUE:100"
        );

        assertTrue(result);

        verify(notificationRepository)
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        eq(1L),
                        eq("INSURANCE:INSURANCE_EXPIRING:INSURANCE_PREMIUM_DUE:100"),
                        eq(EnumSet.of(
                                NotificationStatus.UNREAD,
                                NotificationStatus.READ
                        ))
                );
    }

    @Test
    void shouldTreatReadNotificationAsDuplicate() {
        when(notificationRepository
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        eq(1L),
                        eq("INSURANCE:INSURANCE_EXPIRING:INSURANCE_PREMIUM_OVERDUE:100"),
                        any()
                )).thenReturn(true);

        boolean result = deduplicationService.isDuplicate(
                1L,
                "INSURANCE:INSURANCE_EXPIRING:INSURANCE_PREMIUM_OVERDUE:100"
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenRepositoryFindsNoDuplicate() {
        when(notificationRepository
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        eq(1L),
                        eq("INSURANCE:INSURANCE_EXPIRING:INSURANCE_POLICY_EXPIRING:100"),
                        any()
                )).thenReturn(false);

        boolean result = deduplicationService.isDuplicate(
                1L,
                "INSURANCE:INSURANCE_EXPIRING:INSURANCE_POLICY_EXPIRING:100"
        );

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForBlankDeduplicationKey() {
        assertFalse(deduplicationService.isDuplicate(1L, "   "));

        verify(notificationRepository, never())
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void shouldReturnFalseForNullDeduplicationKey() {
        assertFalse(deduplicationService.isDuplicate(1L, null));

        verify(notificationRepository, never())
                .existsByUserIdAndDeduplicationKeyAndStatusIn(
                        any(),
                        any(),
                        any()
                );
    }
}