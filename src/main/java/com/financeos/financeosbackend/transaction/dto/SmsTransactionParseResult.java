package com.financeos.financeosbackend.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SmsTransactionParseResult {

    private BigDecimal amount;
    private LocalDateTime transactionDateTime;
    private String merchantPayee;
    private String reference;
    private boolean debit;

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

    public String getMerchantPayee() {
        return merchantPayee;
    }

    public void setMerchantPayee(String merchantPayee) {
        this.merchantPayee = merchantPayee;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isDebit() {
        return debit;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }
}