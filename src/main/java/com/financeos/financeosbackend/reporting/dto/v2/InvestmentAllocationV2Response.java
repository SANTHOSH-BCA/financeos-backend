package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;

public class InvestmentAllocationV2Response {

    private String assetType;
    private BigDecimal amount;
    private BigDecimal percentage;

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}