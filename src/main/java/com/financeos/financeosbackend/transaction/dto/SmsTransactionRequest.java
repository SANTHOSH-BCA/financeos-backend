package com.financeos.financeosbackend.transaction.dto;

import jakarta.validation.constraints.NotBlank;

public class SmsTransactionRequest {

    @NotBlank(message = "SMS message is required")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}