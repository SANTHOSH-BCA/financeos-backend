package com.financeos.financeosbackend.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReconcileHelpRequest {

    @NotNull(message = "Return transaction ID is required")
    private Long returnTransactionId;
}