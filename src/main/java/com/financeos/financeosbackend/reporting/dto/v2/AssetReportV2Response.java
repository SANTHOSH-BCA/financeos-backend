package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AssetReportV2Response {

    private ReportSectionMetadata metadata;

    private BigDecimal recognizedAssets;
    private BigDecimal liquidAssets;

    private List<AssetAllocationV2Response> allocation =
            new ArrayList<>();

    public AssetReportV2Response() {
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

    public BigDecimal getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(BigDecimal liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public List<AssetAllocationV2Response> getAllocation() {
        return allocation;
    }

    public void setAllocation(List<AssetAllocationV2Response> allocation) {
        this.allocation = allocation;
    }
}