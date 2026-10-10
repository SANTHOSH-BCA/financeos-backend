package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import com.financeos.financeosbackend.insurance.repository.InsurancePolicyRepository;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceReminderProcessor {

    private static final int REMINDER_WINDOW_DAYS = 30;

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final InsuranceNotificationService insuranceNotificationService;

    @Transactional
    public InsuranceReminderProcessingResult processRemindersForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        LocalDate today = LocalDate.now();
        LocalDate reminderWindowEnd = today.plusDays(REMINDER_WINDOW_DAYS);

        int policiesExamined = 0;
        int notificationsCreated = 0;
        int notificationsSkipped = 0;

        List<InsurancePolicy> overduePremiums =
                insurancePolicyRepository
                        .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
                                userId,
                                PremiumPaymentStatus.PENDING,
                                today.minusDays(1)
                        );

        for (InsurancePolicy policy : overduePremiums) {
            policiesExamined++;

            FinancialEventNotificationResult result =
                    insuranceNotificationService.premiumOverdue(
                            userId,
                            policy.getId(),
                            policy.getNextPremiumDueDate()
                    );

            if (result.notificationCreated()) {
                notificationsCreated++;
            } else {
                notificationsSkipped++;
            }
        }

        List<InsurancePolicy> upcomingPremiums =
                insurancePolicyRepository
                        .findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
                                userId,
                                PremiumPaymentStatus.PENDING,
                                today,
                                reminderWindowEnd
                        );

        for (InsurancePolicy policy : upcomingPremiums) {
            policiesExamined++;

            FinancialEventNotificationResult result =
                    insuranceNotificationService.premiumDue(
                            userId,
                            policy.getId(),
                            policy.getNextPremiumDueDate()
                    );

            if (result.notificationCreated()) {
                notificationsCreated++;
            } else {
                notificationsSkipped++;
            }
        }

        List<InsurancePolicy> expiringPolicies =
                insurancePolicyRepository
                        .findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
                                userId,
                                InsuranceStatus.ACTIVE,
                                today,
                                reminderWindowEnd
                        );

        for (InsurancePolicy policy : expiringPolicies) {
            policiesExamined++;

            FinancialEventNotificationResult result =
                    insuranceNotificationService.policyExpiring(
                            userId,
                            policy.getId(),
                            policy.getExpiryDate()
                    );

            if (result.notificationCreated()) {
                notificationsCreated++;
            } else {
                notificationsSkipped++;
            }
        }

        List<InsurancePolicy> expiredPolicies =
                insurancePolicyRepository
                        .findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
                                userId,
                                InsuranceStatus.ACTIVE,
                                today
                        );

        for (InsurancePolicy policy : expiredPolicies) {
            policiesExamined++;

            FinancialEventNotificationResult result =
                    insuranceNotificationService.policyExpired(
                            userId,
                            policy.getId(),
                            policy.getExpiryDate()
                    );

            if (result.notificationCreated()) {
                notificationsCreated++;
            } else {
                notificationsSkipped++;
            }
        }
        return new InsuranceReminderProcessingResult(
                policiesExamined,
                notificationsCreated,
                notificationsSkipped
        );
    }
}