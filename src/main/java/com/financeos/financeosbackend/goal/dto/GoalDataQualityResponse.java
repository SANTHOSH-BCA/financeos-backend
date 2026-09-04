package com.financeos.financeosbackend.goal.dto;

import java.util.List;

public class GoalDataQualityResponse {

    private boolean valid;
    private List<String> warnings;

    public GoalDataQualityResponse() {
    }

    public GoalDataQualityResponse(
            boolean valid,
            List<String> warnings) {
        this.valid = valid;
        this.warnings = warnings;
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}