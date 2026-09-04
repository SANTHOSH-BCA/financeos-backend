package com.financeos.financeosbackend.transaction.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionTypeRequest {

    @NotNull(message = "Transaction type is required")
    private TransactionType type;
}