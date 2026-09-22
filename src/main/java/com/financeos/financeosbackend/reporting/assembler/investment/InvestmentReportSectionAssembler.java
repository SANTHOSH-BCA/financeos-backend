package com.financeos.financeosbackend.reporting.assembler.investment;

import com.financeos.financeosbackend.reporting.collector.investment.ReportInvestmentData;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;

public interface InvestmentReportSectionAssembler {

    InvestmentReportV2Response assemble(ReportInvestmentData data);
}