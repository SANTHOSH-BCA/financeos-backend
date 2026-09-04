package com.financeos.financeosbackend.income.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.Positive;import com.financeos.financeosbackend.income.enums.IncomePattern;
public class AddIncomeRequest {

    @NotBlank(message = "Income source is required")
    private String source;

    @NotNull(message = "Income amount is required")
    @Positive(message = "Income amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Income date is required")
    private LocalDate incomeDate;

    private IncomePattern pattern = IncomePattern.IRREGULAR;

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

    public IncomePattern getPattern() {
        return pattern;
    }

    public void setPattern(IncomePattern pattern) {
        this.pattern = pattern;
    }
}