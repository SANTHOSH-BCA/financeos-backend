package com.financeos.financeosbackend.reporting.insight;

import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;

public interface ReportChangeInsightAssembler {

    ReportChangeInsightV2Response assemble(
            ReportChangeData data
    );
}