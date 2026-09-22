package com.financeos.financeosbackend.reporting.assembler;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

import com.financeos.financeosbackend.reporting.collector.ReportDataContext;
import com.financeos.financeosbackend.reporting.collector.ReportCashFlowData;
import com.financeos.financeosbackend.reporting.collector.ReportExpenseData;
import com.financeos.financeosbackend.reporting.collector.ReportIncomeData;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionMetadata;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.springframework.stereotype.Service;

@Service
public class DefaultReportSectionAssembler implements ReportSectionAssembler {

    @Override
    public IncomeReportV2Response assembleIncome(ReportDataContext context) {

        ReportIncomeData data = context.getIncome();

        IncomeReportV2Response response = new IncomeReportV2Response();

        response.setTotalIncome(data.getTotalIncome());
        response.setRecurringIncome(data.getRecurringIncome());
        response.setIrregularIncome(data.getIrregularIncome());
        response.setSourceBreakdown(data.getSourceBreakdown());

        if (data.getTotalIncome().signum() == 0) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No income records were available for the selected period."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Income data is available for the selected period."
                    )
            );
        }

        return response;
    }

    @Override
    public ExpenseReportV2Response assembleExpenses(ReportDataContext context) {

        ReportExpenseData data = context.getExpenses();

        ExpenseReportV2Response response = new ExpenseReportV2Response();

        response.setTotalConfirmedExpenses(data.getTotalConfirmedExpenses());
        response.setCategoryBreakdown(data.getCategoryBreakdown());
        response.setHelpAmounts(data.getHelpAmounts());
        response.setConvertedHelpExpenses(data.getConvertedHelpExpenses());

        if (data.getTotalConfirmedExpenses().signum() == 0) {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No confirmed expense records were available for the selected period."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Confirmed expense data is available for the selected period."
                    )
            );
        }

        return response;
    }

    @Override
    public CashFlowReportV2Response assembleCashFlow(ReportDataContext context) {

        ReportCashFlowData data = context.getCashFlow();

        CashFlowReportV2Response response = new CashFlowReportV2Response();

        response.setInflows(data.getInflows());
        response.setOutflows(data.getOutflows());
        response.setDebtPaymentOutflows(data.getDebtPaymentOutflows());
        response.setNetCashFlow(data.getNetCashFlow());
        response.setSavings(data.getSavings());
        response.setSavingsRate(data.getSavingsRate());

        if (data.getInflows().signum() == 0
                && data.getOutflows().signum() == 0) {

            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.NO_DATA,
                            "No cash-flow activity was available for the selected period."
                    )
            );
        } else {
            response.setMetadata(
                    new ReportSectionMetadata(
                            ReportSectionStatus.AVAILABLE,
                            "Cash-flow data is available for the selected period."
                    )
            );
        }

        return response;
    }
}
