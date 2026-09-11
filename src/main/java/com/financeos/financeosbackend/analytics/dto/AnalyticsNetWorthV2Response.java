package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class AnalyticsNetWorthV2Response {

    private BigDecimal recognizedAssets;
    private BigDecimal recognizedLiabilities;
    private BigDecimal netWorth;

    public AnalyticsNetWorthV2Response() {
    }

    public AnalyticsNetWorthV2Response(
            BigDecimal recognizedAssets,
            BigDecimal recognizedLiabilities,
            BigDecimal netWorth
    ) {
        this.recognizedAssets = recognizedAssets;
        this.recognizedLiabilities = recognizedLiabilities;
        this.netWorth = netWorth;
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
}