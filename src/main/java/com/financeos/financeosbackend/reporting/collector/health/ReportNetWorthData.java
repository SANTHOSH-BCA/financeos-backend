package com.financeos.financeosbackend.reporting.collector.health;

import java.math.BigDecimal;

public class ReportNetWorthData {

    private BigDecimal recognizedAssets = BigDecimal.ZERO;
    private BigDecimal recognizedLiabilities = BigDecimal.ZERO;
    private BigDecimal netWorth = BigDecimal.ZERO;
    private boolean historicalDataAvailable;

    public ReportNetWorthData() {
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

    public boolean isHistoricalDataAvailable() {
        return historicalDataAvailable;
    }

    public void setHistoricalDataAvailable(boolean historicalDataAvailable) {
        this.historicalDataAvailable = historicalDataAvailable;
    }
}