package com.financeos.financeosbackend.reporting.collector.position;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ReportAssetData {

    private BigDecimal recognizedAssets = BigDecimal.ZERO;
    private BigDecimal liquidAssets = BigDecimal.ZERO;

    private Map<String, BigDecimal> allocation = new HashMap<>();

    private boolean historicalDataAvailable;

    public ReportAssetData() {
    }

    public BigDecimal getRecognizedAssets() {
        return recognizedAssets;
    }

    public void setRecognizedAssets(BigDecimal recognizedAssets) {
        this.recognizedAssets = recognizedAssets;
    }

    public BigDecimal getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(BigDecimal liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public Map<String, BigDecimal> getAllocation() {
        return allocation;
    }

    public void setAllocation(Map<String, BigDecimal> allocation) {
        this.allocation = allocation;
    }

    public boolean isHistoricalDataAvailable() {
        return historicalDataAvailable;
    }

    public void setHistoricalDataAvailable(boolean historicalDataAvailable) {
        this.historicalDataAvailable = historicalDataAvailable;
    }
}