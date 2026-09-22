package com.financeos.financeosbackend.reporting.assembler;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.AssetReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialHealthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.GoalReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.InvestmentReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.LiabilityReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.NetWorthReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportChangeInsightV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportComparisonV2Response;

import java.util.List;

public interface FinancialReportV2Assembler {

    FinancialReportV2Response assemble(
            ReportPeriodResponse period,
            IncomeReportV2Response income,
            ExpenseReportV2Response expenses,
            CashFlowReportV2Response cashFlow,
            InvestmentReportV2Response investments,
            GoalReportV2Response goals,
            AssetReportV2Response assets,
            LiabilityReportV2Response liabilities,
            NetWorthReportV2Response netWorth,
            FinancialHealthReportV2Response financialHealth,
            ReportComparisonV2Response comparison,
            List<ReportChangeInsightV2Response> changes
    );
}
