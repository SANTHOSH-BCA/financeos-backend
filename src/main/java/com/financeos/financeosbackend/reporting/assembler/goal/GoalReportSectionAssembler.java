package com.financeos.financeosbackend.reporting.assembler.goal;

import com.financeos.financeosbackend.reporting.collector.goal.ReportGoalData;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;

public interface GoalReportSectionAssembler {

    GoalReportV2Response assemble(ReportGoalData data);
}