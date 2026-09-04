package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class HelpHistoryResponse {

    private Long helpTransactionId;
    private BigDecimal helpAmount;
    private BigDecimal returnedAmount;
    private BigDecimal outstandingAmount;
    private LocalDate expectedReturnDate;
    private String status;
    private List<HelpReturnItemResponse> returns;

    public Long getHelpTransactionId() {
        return helpTransactionId;
    }

    public void setHelpTransactionId(Long helpTransactionId) {
        this.helpTransactionId = helpTransactionId;
    }

    public BigDecimal getHelpAmount() {
        return helpAmount;
    }

    public void setHelpAmount(BigDecimal helpAmount) {
        this.helpAmount = helpAmount;
    }

    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(BigDecimal returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
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

    public List<HelpReturnItemResponse> getReturns() {
        return returns;
    }

    public void setReturns(List<HelpReturnItemResponse> returns) {
        this.returns = returns;
    }

    public static class HelpReturnItemResponse {

        private Long transactionId;
        private BigDecimal amount;
        private LocalDate transactionDate;
        private String merchantPayee;

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

        public LocalDate getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getMerchantPayee() {
            return merchantPayee;
        }

        public void setMerchantPayee(String merchantPayee) {
            this.merchantPayee = merchantPayee;
        }
    }
}