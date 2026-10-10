package com.financeos.financeosbackend.notification.integration.insurance;

import com.financeos.financeosbackend.notification.enums.NotificationSource;
import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;import static org.junit.jupiter.api.Assertions.assertTrue;

class InsuranceNotificationEventFactoryTest {

    private static final Long USER_ID = 1L;
    private static final Long POLICY_ID = 100L;

    @Test
    void premiumDueShouldHaveDistinctDeduplicationIdentity() {
        FinancialEvent event =
                InsuranceNotificationEventFactory.premiumDue(
                        USER_ID,
                        POLICY_ID,
                        LocalDate.now().plusDays(7)
                );

        assertEquals("INSURANCE_PREMIUM_DUE", event.relatedEntityType());
        assertEquals(POLICY_ID, event.relatedEntityId());
        assertEquals(FinancialEventType.INSURANCE_EXPIRING, event.eventType());
        assertEquals(NotificationSource.INSURANCE, event.sourceModule());
        assertEquals("Insurance Premium Due", event.title());
    }

    @Test
    void premiumOverdueShouldHaveDistinctDeduplicationIdentity() {
        FinancialEvent event =
                InsuranceNotificationEventFactory.premiumOverdue(
                        USER_ID,
                        POLICY_ID,
                        LocalDate.now().minusDays(5)
                );

        assertEquals("INSURANCE_PREMIUM_OVERDUE", event.relatedEntityType());
        assertEquals(POLICY_ID, event.relatedEntityId());
        assertEquals(FinancialEventType.INSURANCE_EXPIRING, event.eventType());
        assertEquals(NotificationSource.INSURANCE, event.sourceModule());
        assertEquals("Insurance Premium Overdue", event.title());
    }

    @Test
    void policyExpiringShouldHaveDistinctDeduplicationIdentity() {
        FinancialEvent event =
                InsuranceNotificationEventFactory.policyExpiring(
                        USER_ID,
                        POLICY_ID,
                        LocalDate.now().plusDays(15)
                );

        assertEquals("INSURANCE_POLICY_EXPIRING", event.relatedEntityType());
        assertEquals(POLICY_ID, event.relatedEntityId());
        assertEquals(FinancialEventType.INSURANCE_EXPIRING, event.eventType());
        assertEquals(NotificationSource.INSURANCE, event.sourceModule());
        assertEquals("Insurance Policy Expiring", event.title());
    }

    @Test
    void allReminderTypesShouldHaveDifferentDeduplicationIdentities() {
        LocalDate date = LocalDate.now();

        FinancialEvent premiumDue =
                InsuranceNotificationEventFactory.premiumDue(
                        USER_ID, POLICY_ID, date.plusDays(5)
                );

        FinancialEvent premiumOverdue =
                InsuranceNotificationEventFactory.premiumOverdue(
                        USER_ID, POLICY_ID, date.minusDays(5)
                );

        FinancialEvent policyExpiring =
                InsuranceNotificationEventFactory.policyExpiring(
                        USER_ID, POLICY_ID, date.plusDays(10)
                );

        assertFalse(
                premiumDue.relatedEntityType().equals(
                        premiumOverdue.relatedEntityType()
                )
        );

        assertFalse(
                premiumDue.relatedEntityType().equals(
                        policyExpiring.relatedEntityType()
                )
        );

        assertFalse(
                premiumOverdue.relatedEntityType().equals(
                        policyExpiring.relatedEntityType()
                )
        );
    }

    @Test
    void reminderMessagesShouldNotExposePolicyNumbers() {
        FinancialEvent premiumDue =
                InsuranceNotificationEventFactory.premiumDue(
                        USER_ID, POLICY_ID, LocalDate.now().plusDays(5)
                );

        FinancialEvent premiumOverdue =
                InsuranceNotificationEventFactory.premiumOverdue(
                        USER_ID, POLICY_ID, LocalDate.now().minusDays(5)
                );

        FinancialEvent policyExpiring =
                InsuranceNotificationEventFactory.policyExpiring(
                        USER_ID, POLICY_ID, LocalDate.now().plusDays(10)
                );

        assertNotNull(premiumDue.message());
        assertNotNull(premiumOverdue.message());
        assertNotNull(policyExpiring.message());

        assertFalse(premiumDue.message().contains("POLICY-SECRET-123"));
        assertFalse(premiumOverdue.message().contains("POLICY-SECRET-123"));
        assertFalse(policyExpiring.message().contains("POLICY-SECRET-123"));
    }

    @Test
    void policyExpiredShouldUseDedicatedEventType() {
        LocalDate expiryDate = LocalDate.now().minusDays(5);

        FinancialEvent event =
                InsuranceNotificationEventFactory.policyExpired(
                        USER_ID,
                        POLICY_ID,
                        expiryDate
                );

        assertEquals(
                FinancialEventType.INSURANCE_POLICY_EXPIRED,
                event.eventType()
        );
        assertEquals(
                "INSURANCE_POLICY_EXPIRED",
                event.relatedEntityType()
        );
        assertEquals(POLICY_ID, event.relatedEntityId());
        assertEquals("Insurance Policy Expired", event.title());
        assertEquals(NotificationSource.INSURANCE, event.sourceModule());
    }

    @Test
    void expiredPolicyMessageShouldNotExposePolicyNumbers() {
        FinancialEvent event =
                InsuranceNotificationEventFactory.policyExpired(
                        USER_ID,
                        POLICY_ID,
                        LocalDate.now().minusDays(5)
                );

        assertNotNull(event.message());
        assertFalse(event.message().contains("POLICY-SECRET-123"));
        assertTrue(event.message().contains(
                LocalDate.now().minusDays(5).toString()
        ));
    }
}