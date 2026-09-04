package com.financeos.financeosbackend.income.dto;

import java.math.BigDecimal;
import java.util.Map;
import com.financeos.financeosbackend.income.enums.IncomeStability;
import com.financeos.financeosbackend.income.enums.IncomeStability;


public class IncomeIntelligenceResponse {

    private BigDecimal currentMonthIncome;

    private BigDecimal previousMonthIncome;

    private BigDecimal changeAmount;

    private BigDecimal changePercentage;

    private String trend;

    private IncomeStability stability;

    private String majorIncomeSource;

    private Map<String, BigDecimal> sourceContributions;

    private Map<String, BigDecimal> sourceContributionPercentages;

    private String meaningfulChange;

    public BigDecimal getCurrentMonthIncome() {
        return currentMonthIncome;
    }

    public void setCurrentMonthIncome(BigDecimal currentMonthIncome) {
        this.currentMonthIncome = currentMonthIncome;
    }

    public BigDecimal getPreviousMonthIncome() {
        return previousMonthIncome;
    }

    public void setPreviousMonthIncome(BigDecimal previousMonthIncome) {
        this.previousMonthIncome = previousMonthIncome;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(BigDecimal changePercentage) {
        this.changePercentage = changePercentage;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public IncomeStability getStability() {
        return stability;
    }

    public void setStability(IncomeStability stability) {
        this.stability = stability;
    }

    public String getMajorIncomeSource() {
        return majorIncomeSource;
    }

    public void setMajorIncomeSource(String majorIncomeSource) {
        this.majorIncomeSource = majorIncomeSource;
    }

    public Map<String, BigDecimal> getSourceContributions() {
        return sourceContributions;
    }

    public void setSourceContributions(
            Map<String, BigDecimal> sourceContributions) {

        this.sourceContributions = sourceContributions;
    }

    public String getMeaningfulChange() {
        return meaningfulChange;
    }

    public void setMeaningfulChange(String meaningfulChange) {
        this.meaningfulChange = meaningfulChange;
    }

    public Map<String, BigDecimal> getSourceContributionPercentages() {
        return sourceContributionPercentages;
    }

    public void setSourceContributionPercentages(
            Map<String, BigDecimal> sourceContributionPercentages) {

        this.sourceContributionPercentages =
                sourceContributionPercentages;
    }
}