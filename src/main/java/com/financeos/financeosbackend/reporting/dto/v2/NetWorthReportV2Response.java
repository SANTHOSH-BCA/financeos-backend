package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;

public class NetWorthReportV2Response {

    private ReportSectionMetadata metadata;

    private BigDecimal recognizedAssets = BigDecimal.ZERO;
    private BigDecimal recognizedLiabilities = BigDecimal.ZERO;
    private BigDecimal netWorth = BigDecimal.ZERO;

    public NetWorthReportV2Response() {
    }

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
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

    public void setRecognizedLiabilities(
            BigDecimal recognizedLiabilities
    ) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }
}