package com.financeos.financeosbackend.financialposition.dto;

import com.financeos.financeosbackend.asset.enums.AssetType;

import java.math.BigDecimal;
import java.util.Map;

public class FinancialPositionResponse {

    private BigDecimal netWorth;
    private BigDecimal netCashFlow;
    private BigDecimal investmentValue;
    private BigDecimal debtValue;
    private BigDecimal liquidAssets;
    private Map<AssetType, BigDecimal> assetAllocation;
    private BigDecimal savings;
    private BigDecimal savingsRate;

    public FinancialPositionResponse() {
    }

    public FinancialPositionResponse(
            BigDecimal netWorth,
            BigDecimal netCashFlow,
            BigDecimal investmentValue,
            BigDecimal debtValue,
            BigDecimal liquidAssets,
            Map<AssetType, BigDecimal> assetAllocation,
            BigDecimal savings,
            BigDecimal savingsRate
    ) {
        this.netWorth = netWorth;
        this.netCashFlow = netCashFlow;
        this.investmentValue = investmentValue;
        this.debtValue = debtValue;
        this.liquidAssets = liquidAssets;
        this.assetAllocation = assetAllocation;
        this.savings = savings;
        this.savingsRate = savingsRate;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    public BigDecimal getInvestmentValue() {
        return investmentValue;
    }

    public void setInvestmentValue(BigDecimal investmentValue) {
        this.investmentValue = investmentValue;
    }

    public BigDecimal getDebtValue() {
        return debtValue;
    }

    public void setDebtValue(BigDecimal debtValue) {
        this.debtValue = debtValue;
    }

    public BigDecimal getLiquidAssets() {
        return liquidAssets;
    }

    public void setLiquidAssets(BigDecimal liquidAssets) {
        this.liquidAssets = liquidAssets;
    }

    public Map<AssetType, BigDecimal> getAssetAllocation() {
        return assetAllocation;
    }

    public void setAssetAllocation(
            Map<AssetType, BigDecimal> assetAllocation
    ) {
        this.assetAllocation = assetAllocation;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }
}