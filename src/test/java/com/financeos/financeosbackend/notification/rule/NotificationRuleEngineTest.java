package com.financeos.financeosbackend.notification.rule;

import com.financeos.financeosbackend.notification.event.FinancialEvent;
import com.financeos.financeosbackend.notification.event.FinancialEventType;
import com.financeos.financeosbackend.notification.enums.NotificationPriority;
import com.financeos.financeosbackend.notification.enums.NotificationSource;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRuleEngineTest {

    private final NotificationRuleEngine ruleEngine =
            new NotificationRuleEngine(
                    java.util.List.of()
            );

    @Test
    void goalFallingBehindShouldBeCritical() {

        FinancialEvent event = event(
                FinancialEventType.GOAL_FALLING_BEHIND
        );

        NotificationRuleResult result =
                ruleEngine.evaluate(event);

        assertTrue(result.shouldNotify());
        assertEquals(
                NotificationPriority.CRITICAL,
                result.priority()
        );
    }

    @Test
    void investmentUpdateShouldBeImportant() {

        FinancialEvent event = event(
                FinancialEventType.INVESTMENT_UPDATED
        );

        NotificationRuleResult result =
                ruleEngine.evaluate(event);

        assertTrue(result.shouldNotify());
        assertEquals(
                NotificationPriority.IMPORTANT,
                result.priority()
        );
    }

    @Test
    void dailyFinancialUpdateShouldBeInformational() {

        FinancialEvent event = event(
                FinancialEventType.DAILY_FINANCIAL_UPDATE
        );

        NotificationRuleResult result =
                ruleEngine.evaluate(event);

        assertTrue(result.shouldNotify());
        assertEquals(
                NotificationPriority.INFORMATIONAL,
                result.priority()
        );
    }

    private FinancialEvent event(
            FinancialEventType type
    ) {

        return new FinancialEvent(
                "test-event",
                1L,
                type,
                NotificationSource.GOAL,
                "GOAL",
                10L,
                "Test",
                "Test message",
                null,
                LocalDateTime.now(),
                null
        );
    }
}