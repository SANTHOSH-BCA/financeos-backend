package com.financeos.financeosbackend.analytics.dto;

public class SmartRecommendationResponse {

    private String title;
    private String recommendation;
    private String priority;

    public SmartRecommendationResponse() {
    }

    public SmartRecommendationResponse(String title, String recommendation, String priority) {
        this.title = title;
        this.recommendation = recommendation;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}