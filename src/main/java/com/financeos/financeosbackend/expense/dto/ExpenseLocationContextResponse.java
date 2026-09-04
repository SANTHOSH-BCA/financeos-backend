package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;

public class ExpenseLocationContextResponse {

    private Long transactionId;
    private BigDecimal amount;
    private String merchantPayee;
    private String location;
    private String category;
    private String context;

    public ExpenseLocationContextResponse() {
    }

    public ExpenseLocationContextResponse(
            Long transactionId,
            BigDecimal amount,
            String merchantPayee,
            String location,
            String category,
            String context) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.merchantPayee = merchantPayee;
        this.location = location;
        this.category = category;
        this.context = context;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchantPayee() {
        return merchantPayee;
    }

    public void setMerchantPayee(String merchantPayee) {
        this.merchantPayee = merchantPayee;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}