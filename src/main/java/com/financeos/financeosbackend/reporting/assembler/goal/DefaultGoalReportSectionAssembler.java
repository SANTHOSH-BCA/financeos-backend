package com.financeos.financeosbackend.reporting.assembler.goal;

import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalData;
import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalItemData;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultGoalReportSectionAssembler
        implements GoalReportSectionAssembler {

    @Override
    public GoalReportV2Response assemble(ReportGoalData data) {

        if (data == null) {
            throw new IllegalArgumentException(
                    "Goal report data must not be null"
            );
        }

        GoalReportV2Response response =
                new GoalReportV2Response();

        response.setTotalGoals(data.getTotalGoals());
        response.setCompletedGoals(data.getCompletedGoals());
        response.setOnTrackGoals(data.getOnTrackGoals());
        response.setAtRiskGoals(data.getAtRiskGoals());

        response.setGoals(
                assembleGoals(data.getGoals())
        );

        response.setMetadata(
                buildMetadata(data)
        );

        return response;
    }

    private List<GoalReportV2Response.GoalItem> assembleGoals(
            List<ReportGoalItemData> goalItems) {

        List<GoalReportV2Response.GoalItem> result =
                new ArrayList<>();

        if (goalItems == null) {
            return result;
        }

        for (ReportGoalItemData item : goalItems) {

            GoalReportV2Response.GoalItem goal =
                    new GoalReportV2Response.GoalItem();

            goal.setGoalId(item.getGoalId());
            goal.setGoalName(item.getGoalName());
            goal.setTargetAmount(item.getTargetAmount());
            goal.setCurrentAmount(item.getCurrentAmount());
            goal.setRemainingAmount(item.getRemainingAmount());
            goal.setContribution(item.getContribution());
            goal.setTargetDate(item.getTargetDate());
            goal.setProjectedCompletionDate(
                    item.getProjectedCompletionDate()
            );
            goal.setStatus(item.getStatus());

            result.add(goal);
        }

        return result;
    }

    private ReportSectionMetadata buildMetadata(
            ReportGoalData data) {

        if (!data.isDataAvailable()
                || data.getTotalGoals() == 0) {

            return new ReportSectionMetadata(
                    ReportSectionStatus.NO_DATA,
                    "No goals were available for the selected report."
            );
        }

        return new ReportSectionMetadata(
                ReportSectionStatus.AVAILABLE,
                "Goal data is available for the selected report."
        );
    }
}