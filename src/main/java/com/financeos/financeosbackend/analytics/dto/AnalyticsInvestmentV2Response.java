package com.financeos.financeosbackend.analytics.dto;

import com.financeos.financeosbackend.investment.dto.InvestmentAllocationResponse;
import com.financeos.financeosbackend.investment.dto.InvestmentExposureResponse;
import com.financeos.financeosbackend.investment.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsInvestmentV2Response {

    private InvestmentPerformanceResponse performance;
    private List<InvestmentAllocationResponse> allocation;
    private List<InvestmentExposureResponse> exposure;
    private List<InvestmentInsightResponse> insights;
    private BigDecimal investmentToNetWorthPercentage;

    public AnalyticsInvestmentV2Response() {
    }

    public AnalyticsInvestmentV2Response(
            InvestmentPerformanceResponse performance,
            List<InvestmentAllocationResponse> allocation,
            List<InvestmentExposureResponse> exposure,
            List<InvestmentInsightResponse> insights,
            BigDecimal investmentToNetWorthPercentage
    ) {
        this.performance = performance;
        this.allocation = allocation;
        this.exposure = exposure;
        this.insights = insights;
        this.investmentToNetWorthPercentage = investmentToNetWorthPercentage;
    }

    public InvestmentPerformanceResponse getPerformance() {
        return performance;
    }

    public void setPerformance(InvestmentPerformanceResponse performance) {
        this.performance = performance;
    }

    public List<InvestmentAllocationResponse> getAllocation() {
        return allocation;
    }

    public void setAllocation(List<InvestmentAllocationResponse> allocation) {
        this.allocation = allocation;
    }

    public List<InvestmentExposureResponse> getExposure() {
        return exposure;
    }

    public void setExposure(List<InvestmentExposureResponse> exposure) {
        this.exposure = exposure;
    }

    public List<InvestmentInsightResponse> getInsights() {
        return insights;
    }

    public void setInsights(List<InvestmentInsightResponse> insights) {
        this.insights = insights;
    }

    public BigDecimal getInvestmentToNetWorthPercentage() {
        return investmentToNetWorthPercentage;
    }

    public void setInvestmentToNetWorthPercentage(
            BigDecimal investmentToNetWorthPercentage
    ) {
        this.investmentToNetWorthPercentage = investmentToNetWorthPercentage;
    }
}