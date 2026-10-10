package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import com.financeos.financeosbackend.notification.preference.NotificationPreferenceResponse;
import com.financeos.financeosbackend.notification.preference.NotificationPreferenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceNotificationServiceTest {

    @Mock
    private FinancialEventNotificationService financialEventNotificationService;

    @Mock
    private NotificationPreferenceService notificationPreferenceService;

    @InjectMocks
    private InsuranceNotificationService service;

    @Test
    void premiumDueShouldProcessNotificationWhenEnabled() {
        Long userId = 1L;
        Long policyId = 10L;
        LocalDate dueDate = LocalDate.now().plusDays(5);

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(enabledPreference());

        when(financialEventNotificationService.process(any()))
                .thenReturn(FinancialEventNotificationResult.skipped());

        FinancialEventNotificationResult result =
                service.premiumDue(userId, policyId, dueDate);

        assertNotNull(result);

        verify(notificationPreferenceService).getOrCreate(userId);
        verify(financialEventNotificationService).process(argThat(event ->
                event.userId().equals(userId)
                        && event.relatedEntityId().equals(policyId)
                        && event.title().equals("Insurance Premium Due")
        ));
    }

    @Test
    void premiumOverdueShouldProcessNotificationWhenEnabled() {
        Long userId = 1L;
        Long policyId = 11L;
        LocalDate dueDate = LocalDate.now().minusDays(3);

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(enabledPreference());

        when(financialEventNotificationService.process(any()))
                .thenReturn(FinancialEventNotificationResult.skipped());

        FinancialEventNotificationResult result =
                service.premiumOverdue(userId, policyId, dueDate);

        assertNotNull(result);

        verify(financialEventNotificationService).process(argThat(event ->
                event.userId().equals(userId)
                        && event.relatedEntityId().equals(policyId)
                        && event.title().equals("Insurance Premium Overdue")
        ));
    }

    @Test
    void policyExpiringShouldProcessNotificationWhenEnabled() {
        Long userId = 1L;
        Long policyId = 12L;
        LocalDate expiryDate = LocalDate.now().plusDays(10);

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(enabledPreference());

        when(financialEventNotificationService.process(any()))
                .thenReturn(FinancialEventNotificationResult.skipped());

        FinancialEventNotificationResult result =
                service.policyExpiring(userId, policyId, expiryDate);

        assertNotNull(result);

        verify(financialEventNotificationService).process(argThat(event ->
                event.userId().equals(userId)
                        && event.relatedEntityId().equals(policyId)
                        && event.title().equals("Insurance Policy Expiring")
        ));
    }

    @Test
    void premiumDueShouldSkipNotificationWhenDisabled() {
        Long userId = 1L;

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(disabledPreference());

        FinancialEventNotificationResult result =
                service.premiumDue(userId, 10L, LocalDate.now().plusDays(5));

        assertFalse(result.notificationCreated());

        verify(financialEventNotificationService, never()).process(any());
    }

    @Test
    void premiumOverdueShouldSkipNotificationWhenDisabled() {
        Long userId = 1L;

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(disabledPreference());

        FinancialEventNotificationResult result =
                service.premiumOverdue(userId, 11L, LocalDate.now().minusDays(3));

        assertFalse(result.notificationCreated());

        verify(financialEventNotificationService, never()).process(any());
    }

    @Test
    void policyExpiringShouldSkipNotificationWhenDisabled() {
        Long userId = 1L;

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(disabledPreference());

        FinancialEventNotificationResult result =
                service.policyExpiring(userId, 12L, LocalDate.now().plusDays(10));

        assertFalse(result.notificationCreated());

        verify(financialEventNotificationService, never()).process(any());
    }

    private NotificationPreferenceResponse enabledPreference() {
        return new NotificationPreferenceResponse(
                true, true, true, true, true, true
        );
    }

    private NotificationPreferenceResponse disabledPreference() {
        return new NotificationPreferenceResponse(
                true, true, true, true, false, true
        );
    }

    @Test
    void policyExpiredShouldProcessNotificationWhenEnabled() {
        Long userId = 1L;
        Long policyId = 13L;
        LocalDate expiryDate = LocalDate.now().minusDays(5);

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(enabledPreference());

        when(financialEventNotificationService.process(any()))
                .thenReturn(FinancialEventNotificationResult.skipped());

        FinancialEventNotificationResult result =
                service.policyExpired(userId, policyId, expiryDate);

        assertNotNull(result);

        verify(notificationPreferenceService).getOrCreate(userId);
        verify(financialEventNotificationService).process(argThat(event ->
                event.userId().equals(userId)
                        && event.relatedEntityId().equals(policyId)
                        && event.eventType()
                        == com.financeos.financeosbackend.notification.event.FinancialEventType.INSURANCE_POLICY_EXPIRED
                        && event.title().equals("Insurance Policy Expired")
        ));
    }

    @Test
    void policyExpiredShouldSkipNotificationWhenDisabled() {
        Long userId = 1L;
        Long policyId = 14L;

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(disabledPreference());

        FinancialEventNotificationResult result =
                service.policyExpired(
                        userId,
                        policyId,
                        LocalDate.now().minusDays(5)
                );

        assertFalse(result.notificationCreated());

        verify(notificationPreferenceService).getOrCreate(userId);
        verify(financialEventNotificationService, never()).process(any());
    }

    @Test
    void policyExpiredShouldSupportMissingExpiryDate() {
        Long userId = 1L;
        Long policyId = 15L;

        when(notificationPreferenceService.getOrCreate(userId))
                .thenReturn(enabledPreference());

        when(financialEventNotificationService.process(any()))
                .thenReturn(FinancialEventNotificationResult.skipped());

        FinancialEventNotificationResult result =
                service.policyExpired(userId, policyId, null);

        assertNotNull(result);

        verify(financialEventNotificationService).process(argThat(event ->
                event.relatedEntityId().equals(policyId)
                        && event.message().contains("has expired")
        ));
    }
}