package com.financeos.financeosbackend.transaction.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionSource;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
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
public class FinancialTransactionResponse {

    private Long id;

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

    private Long linkedExpenseId;

    private BigDecimal returnedAmount;

    private BigDecimal outstandingAmount;

    private Long goalId;

    public Long getGoalId() {
        return goalId;
    }

    public void setGoalId(Long goalId) {
        this.goalId = goalId;
    }
}