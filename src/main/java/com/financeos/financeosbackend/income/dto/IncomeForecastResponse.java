package com.financeos.financeosbackend.income.dto;

import java.math.BigDecimal;
import java.util.List;

public class IncomeForecastResponse {

    private boolean forecastAvailable;

    private String status;

    private String message;

    private BigDecimal averageRecurringMonthlyIncome;

    private List<ForecastMonth> projections;

    public boolean isForecastAvailable() {
        return forecastAvailable;
    }

    public void setForecastAvailable(boolean forecastAvailable) {
        this.forecastAvailable = forecastAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getAverageRecurringMonthlyIncome() {
        return averageRecurringMonthlyIncome;
    }

    public void setAverageRecurringMonthlyIncome(
            BigDecimal averageRecurringMonthlyIncome) {

        this.averageRecurringMonthlyIncome =
                averageRecurringMonthlyIncome;
    }

    public List<ForecastMonth> getProjections() {
        return projections;
    }

    public void setProjections(List<ForecastMonth> projections) {
        this.projections = projections;
    }

    public static class ForecastMonth {

        private String month;

        private BigDecimal projectedAmount;

        private String type;

        public ForecastMonth() {
        }

        public ForecastMonth(
                String month,
                BigDecimal projectedAmount,
                String type) {

            this.month = month;
            this.projectedAmount = projectedAmount;
            this.type = type;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public BigDecimal getProjectedAmount() {
            return projectedAmount;
        }

        public void setProjectedAmount(
                BigDecimal projectedAmount) {

            this.projectedAmount = projectedAmount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}