package com.financeos.financeosbackend.reporting.assembler.goal;

import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalData;
import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalItemData;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultGoalReportSectionAssemblerTest {

    private final DefaultGoalReportSectionAssembler assembler =
            new DefaultGoalReportSectionAssembler();

    @Test
    void shouldAssembleGoalSection() {

        ReportGoalItemData goal =
                new ReportGoalItemData();

        goal.setGoalId(1L);
        goal.setGoalName("Emergency Fund");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("40000"));
        goal.setRemainingAmount(new BigDecimal("60000"));
        goal.setContribution(new BigDecimal("10000"));
        goal.setTargetDate(
                LocalDate.of(2027, 1, 1)
        );
        goal.setStatus("ON_TRACK");

        ReportGoalData data =
                new ReportGoalData();

        data.setTotalGoals(1);
        data.setCompletedGoals(0);
        data.setOnTrackGoals(1);
        data.setAtRiskGoals(0);
        data.setGoals(List.of(goal));
        data.setDataAvailable(true);

        GoalReportV2Response response =
                assembler.assemble(data);

        assertEquals(1, response.getTotalGoals());
        assertEquals(0, response.getCompletedGoals());
        assertEquals(1, response.getOnTrackGoals());
        assertEquals(0, response.getAtRiskGoals());

        assertEquals(
                1,
                response.getGoals().size()
        );

        assertEquals(
                0,
                response.getGoals()
                        .get(0)
                        .getTargetAmount()
                        .compareTo(new BigDecimal("100000"))
        );

        assertEquals(
                0,
                response.getGoals()
                        .get(0)
                        .getRemainingAmount()
                        .compareTo(new BigDecimal("60000"))
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkNoData() {

        ReportGoalData data =
                new ReportGoalData();

        data.setTotalGoals(0);
        data.setCompletedGoals(0);
        data.setOnTrackGoals(0);
        data.setAtRiskGoals(0);
        data.setDataAvailable(false);

        GoalReportV2Response response =
                assembler.assemble(data);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldHandleEmptyGoalList() {

        ReportGoalData data =
                new ReportGoalData();

        data.setTotalGoals(0);
        data.setCompletedGoals(0);
        data.setOnTrackGoals(0);
        data.setAtRiskGoals(0);
        data.setDataAvailable(false);
        data.setGoals(List.of());

        GoalReportV2Response response =
                assembler.assemble(data);

        assertEquals(
                0,
                response.getGoals().size()
        );

        assertEquals(
                ReportSectionStatus.NO_DATA,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldRejectNullData() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> assembler.assemble(null)
                );

        assertEquals(
                "Goal report data must not be null",
                exception.getMessage()
        );
    }
}