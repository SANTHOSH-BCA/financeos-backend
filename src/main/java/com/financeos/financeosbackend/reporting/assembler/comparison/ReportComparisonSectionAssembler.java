package com.financeos.financeosbackend.reporting.assembler.comparison;

import com.financeos.financeosbackend.reporting.comparison.ReportComparisonData;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;

public interface ReportComparisonSectionAssembler {

    ReportComparisonV2Response assemble(ReportComparisonData data);
}