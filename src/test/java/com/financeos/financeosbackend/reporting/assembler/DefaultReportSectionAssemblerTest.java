package com.financeos.financeosbackend.reporting.assembler;

import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

import com.financeos.financeosbackend.reporting.collector.ReportCashFlowData;
import com.financeos.financeosbackend.reporting.collector.ReportDataContext;
import com.financeos.financeosbackend.reporting.collector.ReportExpenseData;
import com.financeos.financeosbackend.reporting.collector.ReportIncomeData;
import com.financeos.financeosbackend.reporting.dto.v2.CashFlowReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ExpenseReportV2Response;
import com.financeos.financeosbackend.reporting.dto.v2.ReportSectionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;import com.financeos.financeosbackend.reporting.dto.v2.IncomeReportV2Response;

class DefaultReportSectionAssemblerTest {

    private final DefaultReportSectionAssembler assembler =
            new DefaultReportSectionAssembler();

    @Test
    void shouldAssembleIncomeSection() {

        ReportIncomeData income = new ReportIncomeData();

        income.setTotalIncome(new BigDecimal("50000"));
        income.setRecurringIncome(new BigDecimal("40000"));
        income.setIrregularIncome(new BigDecimal("10000"));
        income.setSourceBreakdown(new HashMap<>());

        ReportDataContext context = new ReportDataContext();

        context.setIncome(income);
        context.setExpenses(emptyExpense());
        context.setCashFlow(emptyCashFlow());

        IncomeReportV2Response response =
                assembler.assembleIncome(context);

        assertEquals(
                0,
                response.getTotalIncome()
                        .compareTo(new BigDecimal("50000"))
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkIncomeAsNoData() {

        ReportIncomeData income = new ReportIncomeData();

        income.setTotalIncome(BigDecimal.ZERO);
        income.setRecurringIncome(BigDecimal.ZERO);
        income.setIrregularIncome(BigDecimal.ZERO);
        income.setSourceBreakdown(new HashMap<>());

        ReportDataContext context = new ReportDataContext();

        context.setIncome(income);
        context.setExpenses(emptyExpense());
        context.setCashFlow(emptyCashFlow());

        IncomeReportV2Response response =
                assembler.assembleIncome(context);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldAssembleExpenseSection() {

        ReportExpenseData expense = new ReportExpenseData();

        expense.setTotalConfirmedExpenses(new BigDecimal("12000"));
        expense.setCategoryBreakdown(new HashMap<>());
        expense.setHelpAmounts(BigDecimal.ZERO);
        expense.setConvertedHelpExpenses(BigDecimal.ZERO);

        ReportDataContext context = new ReportDataContext();

        context.setIncome(emptyIncome());
        context.setExpenses(expense);
        context.setCashFlow(emptyCashFlow());

        ExpenseReportV2Response response =
                assembler.assembleExpenses(context);

        assertEquals(
                0,
                response.getTotalConfirmedExpenses()
                        .compareTo(new BigDecimal("12000"))
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldAssembleCashFlowSection() {

        ReportCashFlowData cashFlow = new ReportCashFlowData();

        cashFlow.setInflows(new BigDecimal("50000"));
        cashFlow.setOutflows(new BigDecimal("30000"));
        cashFlow.setDebtPaymentOutflows(new BigDecimal("5000"));
        cashFlow.setNetCashFlow(new BigDecimal("20000"));
        cashFlow.setSavings(new BigDecimal("20000"));
        cashFlow.setSavingsRate(new BigDecimal("40"));

        ReportDataContext context = new ReportDataContext();

        context.setIncome(emptyIncome());
        context.setExpenses(emptyExpense());
        context.setCashFlow(cashFlow);

        CashFlowReportV2Response response =
                assembler.assembleCashFlow(context);

        assertEquals(
                0,
                response.getNetCashFlow()
                        .compareTo(new BigDecimal("20000"))
        );

        assertEquals(
                ReportSectionStatus.AVAILABLE,
                response.getMetadata().getStatus()
        );
    }

    @Test
    void shouldMarkCashFlowAsNoData() {

        ReportCashFlowData cashFlow = new ReportCashFlowData();

        cashFlow.setInflows(BigDecimal.ZERO);
        cashFlow.setOutflows(BigDecimal.ZERO);
        cashFlow.setDebtPaymentOutflows(BigDecimal.ZERO);
        cashFlow.setNetCashFlow(BigDecimal.ZERO);
        cashFlow.setSavings(BigDecimal.ZERO);
        cashFlow.setSavingsRate(BigDecimal.ZERO);

        ReportDataContext context = new ReportDataContext();

        context.setIncome(emptyIncome());
        context.setExpenses(emptyExpense());
        context.setCashFlow(cashFlow);

        CashFlowReportV2Response response =
                assembler.assembleCashFlow(context);

        assertEquals(
                ReportSectionStatus.NO_DATA,
                response.getMetadata().getStatus()
        );
    }

    private ReportIncomeData emptyIncome() {

        ReportIncomeData income = new ReportIncomeData();

        income.setTotalIncome(BigDecimal.ZERO);
        income.setRecurringIncome(BigDecimal.ZERO);
        income.setIrregularIncome(BigDecimal.ZERO);
        income.setSourceBreakdown(new HashMap<>());

        return income;
    }

    private ReportExpenseData emptyExpense() {

        ReportExpenseData expense = new ReportExpenseData();

        expense.setTotalConfirmedExpenses(BigDecimal.ZERO);
        expense.setCategoryBreakdown(new HashMap<>());
        expense.setHelpAmounts(BigDecimal.ZERO);
        expense.setConvertedHelpExpenses(BigDecimal.ZERO);

        return expense;
    }

    private ReportCashFlowData emptyCashFlow() {

        ReportCashFlowData cashFlow = new ReportCashFlowData();

        cashFlow.setInflows(BigDecimal.ZERO);
        cashFlow.setOutflows(BigDecimal.ZERO);
        cashFlow.setDebtPaymentOutflows(BigDecimal.ZERO);
        cashFlow.setNetCashFlow(BigDecimal.ZERO);
        cashFlow.setSavings(BigDecimal.ZERO);
        cashFlow.setSavingsRate(BigDecimal.ZERO);

        return cashFlow;
    }
}
