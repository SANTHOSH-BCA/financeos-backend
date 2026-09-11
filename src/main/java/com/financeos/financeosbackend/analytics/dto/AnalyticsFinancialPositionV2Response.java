package com.financeos.financeosbackend.analytics.dto;

import com.financeos.financeosbackend.financialposition.dto.FinancialPositionResponse;

public class AnalyticsFinancialPositionV2Response {

    private final FinancialPositionResponse financialPosition;

    public AnalyticsFinancialPositionV2Response(
            FinancialPositionResponse financialPosition
    ) {
        this.financialPosition = financialPosition;
    }

    public FinancialPositionResponse getFinancialPosition() {
        return financialPosition;
    }
}