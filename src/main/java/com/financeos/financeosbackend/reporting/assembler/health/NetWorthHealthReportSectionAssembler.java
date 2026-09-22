package com.financeos.financeosbackend.reporting.assembler.health;

import com.financeos.financeosbackend.reporting.collector.health.ReportFinancialHealthData;
import com.financeos.financeosbackend.reporting.collector.health.ReportNetWorthData;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;

public interface NetWorthHealthReportSectionAssembler {

    NetWorthReportV2Response assembleNetWorth(ReportNetWorthData data);

    FinancialHealthReportV2Response assembleFinancialHealth(
            ReportFinancialHealthData data
    );
}