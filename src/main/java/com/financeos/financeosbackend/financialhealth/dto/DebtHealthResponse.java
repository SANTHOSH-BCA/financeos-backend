package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class DebtHealthResponse {

    private BigDecimal recognizedLiabilities;
    private String status;

    public DebtHealthResponse() {
    }

    public DebtHealthResponse(
            BigDecimal recognizedLiabilities,
            String status
    ) {
        this.recognizedLiabilities = recognizedLiabilities;
        this.status = status;
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(BigDecimal recognizedLiabilities) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}