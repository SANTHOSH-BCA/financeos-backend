package com.financeos.financeosbackend.reporting.dto;

import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;

import java.time.LocalDate;

public class ReportPeriodResponse {

    private ReportPeriodType periodType;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReportPeriodResponse() {
    }

    public ReportPeriodResponse(
            ReportPeriodType periodType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ReportPeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(ReportPeriodType periodType) {
        this.periodType = periodType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}