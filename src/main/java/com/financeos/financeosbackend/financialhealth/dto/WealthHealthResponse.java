package com.financeos.financeosbackend.financialhealth.dto;

import java.math.BigDecimal;

public class WealthHealthResponse {

    private BigDecimal recognizedAssets;
    private BigDecimal recognizedLiabilities;
    private BigDecimal netWorth;
    private String status;

    public WealthHealthResponse() {
    }

    public WealthHealthResponse(
            BigDecimal recognizedAssets,
            BigDecimal recognizedLiabilities,
            BigDecimal netWorth,
            String status
    ) {
        this.recognizedAssets = recognizedAssets;
        this.recognizedLiabilities = recognizedLiabilities;
        this.netWorth = netWorth;
        this.status = status;
    }

    public BigDecimal getRecognizedAssets() {
        return recognizedAssets;
    }

    public void setRecognizedAssets(BigDecimal recognizedAssets) {
        this.recognizedAssets = recognizedAssets;
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(BigDecimal recognizedLiabilities) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}