package com.financeos.financeosbackend.reporting.assembler;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

import com.financeos.financeosbackend.reporting.collector.ReportDataContext;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;

public interface ReportSectionAssembler {

    IncomeReportV2Response assembleIncome(ReportDataContext context);

    ExpenseReportV2Response assembleExpenses(ReportDataContext context);

    CashFlowReportV2Response assembleCashFlow(ReportDataContext context);
}
