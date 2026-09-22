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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefaultFinancialReportV2Assembler
        implements FinancialReportV2Assembler {

    @Override
    public FinancialReportV2Response assemble(
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
    ) {
        if (period == null) {
            throw new IllegalArgumentException(
                    "Report period must not be null."
            );
        }

        FinancialReportV2Response response =
                new FinancialReportV2Response();

        response.setReportId(null);
        response.setReportPeriod(period);
        response.setGeneratedAt(LocalDateTime.now());
        response.setReportStatus("PREVIEW");

        response.setIncome(income);
        response.setExpenses(expenses);
        response.setCashFlow(cashFlow);
        response.setInvestments(investments);
        response.setGoals(goals);
        response.setAssets(assets);
        response.setLiabilities(liabilities);
        response.setNetWorth(netWorth);
        response.setFinancialHealth(financialHealth);
        response.setComparison(comparison);
        response.setChanges(changes);

        return response;
    }
}
