package com.financeos.financeosbackend.analytics.dto;

import java.math.BigDecimal;

public class SavingsScoreResponse {

    private int score;
    private BigDecimal savingsPercentage;
    private String message;

    public SavingsScoreResponse() {
    }

    public SavingsScoreResponse(int score, BigDecimal savingsPercentage, String message) {
        this.score = score;
        this.savingsPercentage = savingsPercentage;
        this.message = message;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public BigDecimal getSavingsPercentage() {
        return savingsPercentage;
    }

    public void setSavingsPercentage(BigDecimal savingsPercentage) {
        this.savingsPercentage = savingsPercentage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}