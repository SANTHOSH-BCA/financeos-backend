package com.financeos.financeosbackend.goal.dto;

public class GoalInsightResponse {

    private String type;
    private String message;

    public GoalInsightResponse() {
    }

    public GoalInsightResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}