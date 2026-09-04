package com.financeos.financeosbackend.investment.dto;

import java.util.List;

public class InvestmentDataQualityResponse {

    private boolean valid;
    private List<String> warnings;

    public InvestmentDataQualityResponse() {
    }

    public InvestmentDataQualityResponse(
            boolean valid,
            List<String> warnings) {
        this.valid = valid;
        this.warnings = warnings;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}