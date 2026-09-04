package com.financeos.financeosbackend.expense.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionSource;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseReviewQueueResponse {

    private Long transactionId;
    private BigDecimal amount;
    private LocalDateTime transactionDateTime;
    private TransactionType type;
    private String category;
    private TransactionSource source;
    private TransactionStatus status;
    private String merchantPayee;
    private String description;
    private String reference;
    private String location;

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

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionSource getSource() {
        return source;
    }

    public void setSource(TransactionSource source) {
        this.source = source;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getMerchantPayee() {
        return merchantPayee;
    }

    public void setMerchantPayee(String merchantPayee) {
        this.merchantPayee = merchantPayee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}