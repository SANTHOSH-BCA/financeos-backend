package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationResult;
import com.financeos.financeosbackend.notification.integration.FinancialEventNotificationService;
import com.financeos.financeosbackend.notification.preference.NotificationPreferenceResponse;
import com.financeos.financeosbackend.notification.preference.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InsuranceNotificationService {

    private final FinancialEventNotificationService financialEventNotificationService;
    private final NotificationPreferenceService notificationPreferenceService;

    @Transactional
    public FinancialEventNotificationResult premiumDue(
            Long userId,
            Long policyId,
            LocalDate dueDate
    ) {
        return processIfEnabled(
                userId,
                InsuranceNotificationEventFactory.premiumDue(
                        userId,
                        policyId,
                        dueDate
                )
        );
    }

    @Transactional
    public FinancialEventNotificationResult premiumOverdue(
            Long userId,
            Long policyId,
            LocalDate dueDate
    ) {
        return processIfEnabled(
                userId,
                InsuranceNotificationEventFactory.premiumOverdue(
                        userId,
                        policyId,
                        dueDate
                )
        );
    }

    @Transactional
    public FinancialEventNotificationResult policyExpiring(
            Long userId,
            Long policyId,
            LocalDate expiryDate
    ) {
        return processIfEnabled(
                userId,
                InsuranceNotificationEventFactory.policyExpiring(
                        userId,
                        policyId,
                        expiryDate
                )
        );
    }

    @Transactional
    public FinancialEventNotificationResult policyExpired(
            Long userId,
            Long policyId,
            LocalDate expiryDate
    ) {
        return processIfEnabled(
                userId,
                InsuranceNotificationEventFactory.policyExpired(
                        userId,
                        policyId,
                        expiryDate
                )
        );
    }
    private FinancialEventNotificationResult processIfEnabled(
            Long userId,
            FinancialEvent event
    ) {
        NotificationPreferenceResponse preference =
                notificationPreferenceService.getOrCreate(userId);

        if (!preference.insuranceReminders()) {
            return FinancialEventNotificationResult.skipped();
        }

        return financialEventNotificationService.process(event);
    }
}