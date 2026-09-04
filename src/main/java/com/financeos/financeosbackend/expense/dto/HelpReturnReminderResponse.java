package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HelpReturnReminderResponse {

    private Long transactionId;
    private BigDecimal amount;
    private String merchantPayee;
    private LocalDate expectedReturnDate;
    private String status;
    private String message;

    public HelpReturnReminderResponse() {
    }

    public HelpReturnReminderResponse(
            Long transactionId,
            BigDecimal amount,
            String merchantPayee,
            LocalDate expectedReturnDate,
            String status,
            String message) {

        this.transactionId = transactionId;
        this.amount = amount;
        this.merchantPayee = merchantPayee;
        this.expectedReturnDate = expectedReturnDate;
        this.status = status;
        this.message = message;
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

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}