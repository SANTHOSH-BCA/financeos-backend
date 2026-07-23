package com.financeos.financeosbackend.analytics.dto;

public class FinancialHealthResponse {

    private int score;
    private String status;
    private String message;

    public FinancialHealthResponse() {
    }

    public FinancialHealthResponse(int score, String status, String message) {
        this.score = score;
        this.status = status;
        this.message = message;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
}