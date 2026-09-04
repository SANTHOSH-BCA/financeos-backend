package com.financeos.financeosbackend.income.dto;

import java.math.BigDecimal;
import java.time.LocalDate;import com.financeos.financeosbackend.income.enums.IncomePattern;

public class IncomeResponse {

    private Long id;

    private String source;

    private BigDecimal amount;

    private LocalDate incomeDate;

    private Long transactionId;

    private IncomePattern pattern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public IncomePattern getPattern() {
        return pattern;
    }

    public void setPattern(IncomePattern pattern) {
        this.pattern = pattern;
    }
}