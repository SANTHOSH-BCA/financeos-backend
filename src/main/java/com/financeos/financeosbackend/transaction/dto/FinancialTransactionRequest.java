package com.financeos.financeosbackend.transaction.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialTransactionRequest {

    @NotNull(message = "Transaction amount is required")
    @Positive(message = "Transaction amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transaction date and time is required")
    private LocalDateTime transactionDateTime;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    private String category;

    private String merchantPayee;

    private String description;

    private String reference;

    private String location;

    private Long goalId;

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}