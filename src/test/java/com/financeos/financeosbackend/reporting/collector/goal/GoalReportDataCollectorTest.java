package com.financeos.financeosbackend.reporting.collector.goal;

import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goalintelligence.service.GoalIntelligenceService;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalReportDataCollectorTest {

    @Mock
    private GoalIntelligenceService goalIntelligenceService;

    @InjectMocks
    private GoalReportDataCollector collector;

    @Test
    void shouldCollectGoalData() {

        User user = new User();

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        Goal goal = new Goal();

        goal.setGoalName("Emergency Fund");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("40000"));
        goal.setTargetDate(LocalDate.of(2027, 1, 1));

        when(goalIntelligenceService.getMyGoals())
                .thenReturn(List.of(goal));

        when(goalIntelligenceService.calculateGoalStatus(goal))
                .thenReturn(GoalStatus.ON_TRACK);

        when(goalIntelligenceService.calculateRemainingAmount(goal))
                .thenReturn(new BigDecimal("60000"));

        when(goalIntelligenceService
                .calculateRequiredMonthlyContribution(goal))
                .thenReturn(new BigDecimal("10000"));

        ReportGoalData result =
                collector.collect(user, period);

        assertTrue(result.isDataAvailable());

        assertEquals(1, result.getTotalGoals());
        assertEquals(0, result.getCompletedGoals());
        assertEquals(1, result.getOnTrackGoals());
        assertEquals(0, result.getAtRiskGoals());

        assertEquals(1, result.getGoals().size());

        ReportGoalItemData item =
                result.getGoals().get(0);

        assertEquals(
                "Emergency Fund",
                item.getGoalName()
        );

        assertEquals(
                0,
                item.getTargetAmount()
                        .compareTo(new BigDecimal("100000"))
        );

        assertEquals(
                0,
                item.getCurrentAmount()
                        .compareTo(new BigDecimal("40000"))
        );

        assertEquals(
                0,
                item.getRemainingAmount()
                        .compareTo(new BigDecimal("60000"))
        );

        assertEquals(
                0,
                item.getContribution()
                        .compareTo(new BigDecimal("10000"))
        );

        assertEquals(
                LocalDate.of(2027, 1, 1),
                item.getTargetDate()
        );

        assertEquals(
                "ON_TRACK",
                item.getStatus()
        );
    }

    @Test
    void shouldHandleMultipleGoalStatuses() {

        User user = new User();

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        Goal completedGoal =
                createGoal("Completed Goal");

        Goal onTrackGoal =
                createGoal("On Track Goal");

        Goal atRiskGoal =
                createGoal("At Risk Goal");

        when(goalIntelligenceService.getMyGoals())
                .thenReturn(
                        List.of(
                                completedGoal,
                                onTrackGoal,
                                atRiskGoal
                        )
                );

        when(goalIntelligenceService
                .calculateGoalStatus(completedGoal))
                .thenReturn(GoalStatus.COMPLETED);

        when(goalIntelligenceService
                .calculateGoalStatus(onTrackGoal))
                .thenReturn(GoalStatus.ON_TRACK);

        when(goalIntelligenceService
                .calculateGoalStatus(atRiskGoal))
                .thenReturn(GoalStatus.AT_RISK);

        when(goalIntelligenceService
                .calculateRemainingAmount(completedGoal))
                .thenReturn(BigDecimal.ZERO);

        when(goalIntelligenceService
                .calculateRemainingAmount(onTrackGoal))
                .thenReturn(new BigDecimal("50000"));

        when(goalIntelligenceService
                .calculateRemainingAmount(atRiskGoal))
                .thenReturn(new BigDecimal("80000"));

        when(goalIntelligenceService
                .calculateRequiredMonthlyContribution(completedGoal))
                .thenReturn(BigDecimal.ZERO);

        when(goalIntelligenceService
                .calculateRequiredMonthlyContribution(onTrackGoal))
                .thenReturn(new BigDecimal("5000"));

        when(goalIntelligenceService
                .calculateRequiredMonthlyContribution(atRiskGoal))
                .thenReturn(new BigDecimal("8000"));

        ReportGoalData result =
                collector.collect(user, period);

        assertTrue(result.isDataAvailable());

        assertEquals(3, result.getTotalGoals());
        assertEquals(1, result.getCompletedGoals());
        assertEquals(1, result.getOnTrackGoals());
        assertEquals(1, result.getAtRiskGoals());

        assertEquals(3, result.getGoals().size());
    }

    @Test
    void shouldReturnNoDataWhenUserHasNoGoals() {

        User user = new User();

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        when(goalIntelligenceService.getMyGoals())
                .thenReturn(List.of());

        ReportGoalData result =
                collector.collect(user, period);

        assertFalse(result.isDataAvailable());

        assertEquals(0, result.getTotalGoals());
        assertEquals(0, result.getGoals().size());
    }

    @Test
    void shouldRejectNullUser() {

        ReportPeriodResponse period =
                new ReportPeriodResponse(
                        ReportPeriodType.MONTHLY,
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 8, 31)
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> collector.collect(null, period)
                );

        assertEquals(
                "User must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullPeriod() {

        User user = new User();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> collector.collect(user, null)
                );

        assertEquals(
                "Report period must not be null",
                exception.getMessage()
        );
    }

    private Goal createGoal(String name) {

        Goal goal = new Goal();

        goal.setGoalName(name);
        goal.setTargetAmount(
                new BigDecimal("100000")
        );
        goal.setCurrentAmount(
                new BigDecimal("50000")
        );
        goal.setTargetDate(
                LocalDate.of(2027, 1, 1)
        );

        return goal;
    }
}