package com.financeos.financeosbackend.reporting.collector;

public class ReportDataContext {

    private ReportIncomeData income;
    private ReportExpenseData expenses;
    private ReportCashFlowData cashFlow;

    public ReportDataContext() {
    }

    public ReportIncomeData getIncome() {
        return income;
    }

    public void setIncome(ReportIncomeData income) {
        this.income = income;
    }

    public ReportExpenseData getExpenses() {
        return expenses;
    }

    public void setExpenses(ReportExpenseData expenses) {
        this.expenses = expenses;
    }

    public ReportCashFlowData getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(ReportCashFlowData cashFlow) {
        this.cashFlow = cashFlow;
    }
}