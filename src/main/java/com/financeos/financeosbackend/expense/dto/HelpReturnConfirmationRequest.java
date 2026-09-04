package com.financeos.financeosbackend.expense.dto;

import jakarta.validation.constraints.NotNull;

public class HelpReturnConfirmationRequest {

    @NotNull(message = "Return transaction ID is required")
    private Long returnTransactionId;

    public Long getReturnTransactionId() {
        return returnTransactionId;
    }

    public void setReturnTransactionId(Long returnTransactionId) {
        this.returnTransactionId = returnTransactionId;
    }
}