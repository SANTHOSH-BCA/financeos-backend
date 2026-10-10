package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.insurance.repository.InsurancePolicyRepository;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceReminderProcessorTest {

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private InsuranceNotificationService insuranceNotificationService;

    @InjectMocks
    private InsuranceReminderProcessor insuranceReminderProcessor;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
    }

    @Test
    void shouldProcessOverduePremiumReminders() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(10L);
        when(policy.getNextPremiumDueDate()).thenReturn(today.minusDays(5));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today.minusDays(1))
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.premiumOverdue(
                userId, 10L, today.minusDays(5)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(1, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .premiumOverdue(userId, 10L, today.minusDays(5));
    }

    @Test
    void shouldProcessUpcomingPremiumReminders() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(20L);
        when(policy.getNextPremiumDueDate()).thenReturn(today.plusDays(7));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today.minusDays(1))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.premiumDue(
                userId, 20L, today.plusDays(7)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(1, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .premiumDue(userId, 20L, today.plusDays(7));
    }

    @Test
    void shouldProcessExpiringPolicyReminders() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(30L);
        when(policy.getExpiryDate()).thenReturn(today.plusDays(10));

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId),
                        eq(InsuranceStatus.ACTIVE),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.policyExpiring(
                userId, 30L, today.plusDays(10)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(1, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .policyExpiring(userId, 30L, today.plusDays(10));
    }

    @Test
    void shouldReturnZeroCountsWhenNoRemindersExist() {
        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING), any(LocalDate.class)
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING),
                        any(LocalDate.class), any(LocalDate.class)
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId), eq(InsuranceStatus.ACTIVE),
                        any(LocalDate.class), any(LocalDate.class)
                )).thenReturn(List.of());

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(0, result.policiesExamined());
        assertEquals(0, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService, never())
                .premiumDue(any(), any(), any());

        verify(insuranceNotificationService, never())
                .premiumOverdue(any(), any(), any());

        verify(insuranceNotificationService, never())
                .policyExpiring(any(), any(), any());
    }

    @Test
    void shouldCountSkippedNotifications() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(40L);
        when(policy.getNextPremiumDueDate()).thenReturn(today);

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING),
                        eq(today.minusDays(1))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING),
                        eq(today), eq(today.plusDays(30))
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.premiumDue(
                userId, 40L, today
        )).thenReturn(FinancialEventNotificationResult.skipped());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId), eq(InsuranceStatus.ACTIVE),
                        eq(today), eq(today.plusDays(30))
                )).thenReturn(List.of());

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(0, result.notificationsCreated());
        assertEquals(1, result.notificationsSkipped());
    }

    @Test
    void shouldRejectNullUserId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> insuranceReminderProcessor.processRemindersForUser(null)
        );
    }
    @Test
    void shouldProcessAllReminderCategoriesInOneRun() {
        LocalDate today = LocalDate.now();

        InsurancePolicy overduePolicy = mock(InsurancePolicy.class);
        when(overduePolicy.getId()).thenReturn(51L);
        when(overduePolicy.getNextPremiumDueDate()).thenReturn(today.minusDays(3));

        InsurancePolicy upcomingPolicy = mock(InsurancePolicy.class);
        when(upcomingPolicy.getId()).thenReturn(52L);
        when(upcomingPolicy.getNextPremiumDueDate()).thenReturn(today.plusDays(5));

        InsurancePolicy expiringPolicy = mock(InsurancePolicy.class);
        when(expiringPolicy.getId()).thenReturn(53L);
        when(expiringPolicy.getExpiryDate()).thenReturn(today.plusDays(12));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today.minusDays(1))
                )).thenReturn(List.of(overduePolicy));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of(upcomingPolicy));

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId),
                        eq(InsuranceStatus.ACTIVE),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of(expiringPolicy));

        when(insuranceNotificationService.premiumOverdue(
                userId, 51L, today.minusDays(3)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        when(insuranceNotificationService.premiumDue(
                userId, 52L, today.plusDays(5)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        when(insuranceNotificationService.policyExpiring(
                userId, 53L, today.plusDays(12)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(3, result.policiesExamined());
        assertEquals(3, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .premiumOverdue(userId, 51L, today.minusDays(3));
        verify(insuranceNotificationService)
                .premiumDue(userId, 52L, today.plusDays(5));
        verify(insuranceNotificationService)
                .policyExpiring(userId, 53L, today.plusDays(12));
    }

    @Test
    void shouldProcessExpiredPolicyReminders() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(60L);
        when(policy.getExpiryDate()).thenReturn(today.minusDays(5));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today.minusDays(1))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId),
                        eq(PremiumPaymentStatus.PENDING),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId),
                        eq(InsuranceStatus.ACTIVE),
                        eq(today),
                        eq(today.plusDays(30))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        eq(userId),
                        eq(InsuranceStatus.ACTIVE),
                        eq(today)
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.policyExpired(
                userId, 60L, today.minusDays(5)
        )).thenReturn(FinancialEventNotificationResult.created(null));

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(1, result.notificationsCreated());
        assertEquals(0, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .policyExpired(userId, 60L, today.minusDays(5));
    }

    @Test
    void shouldCountSkippedExpiredPolicyNotifications() {
        LocalDate today = LocalDate.now();
        InsurancePolicy policy = mock(InsurancePolicy.class);

        when(policy.getId()).thenReturn(61L);
        when(policy.getExpiryDate()).thenReturn(today.minusDays(3));

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING), eq(today.minusDays(1))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                        eq(userId), eq(PremiumPaymentStatus.PENDING),
                        eq(today), eq(today.plusDays(30))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                        eq(userId), eq(InsuranceStatus.ACTIVE),
                        eq(today), eq(today.plusDays(30))
                )).thenReturn(List.of());

        when(insurancePolicyRepository
                .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                        eq(userId), eq(InsuranceStatus.ACTIVE), eq(today)
                )).thenReturn(List.of(policy));

        when(insuranceNotificationService.policyExpired(
                userId, 61L, today.minusDays(3)
        )).thenReturn(FinancialEventNotificationResult.skipped());

        InsuranceReminderProcessingResult result =
                insuranceReminderProcessor.processRemindersForUser(userId);

        assertEquals(1, result.policiesExamined());
        assertEquals(0, result.notificationsCreated());
        assertEquals(1, result.notificationsSkipped());

        verify(insuranceNotificationService)
                .policyExpired(userId, 61L, today.minusDays(3));
    }
}