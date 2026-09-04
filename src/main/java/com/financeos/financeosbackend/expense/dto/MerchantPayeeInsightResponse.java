package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;

public class MerchantPayeeInsightResponse {

    private String merchantPayee;
    private long transactionCount;
    private BigDecimal totalAmount;
    private String spendingContext;

    public MerchantPayeeInsightResponse() {
    }

    public MerchantPayeeInsightResponse(
            String merchantPayee,
            long transactionCount,
            BigDecimal totalAmount,
            String spendingContext) {
        this.merchantPayee = merchantPayee;
        this.transactionCount = transactionCount;
        this.totalAmount = totalAmount;
        this.spendingContext = spendingContext;
    }

    public String getMerchantPayee() {
        return merchantPayee;
    }

    public void setMerchantPayee(String merchantPayee) {
        this.merchantPayee = merchantPayee;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSpendingContext() {
        return spendingContext;
    }

    public void setSpendingContext(String spendingContext) {
        this.spendingContext = spendingContext;
    }
}