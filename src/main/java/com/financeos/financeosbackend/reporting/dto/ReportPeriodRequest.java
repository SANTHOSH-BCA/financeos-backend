package com.financeos.financeosbackend.reporting.dto;

import com.financeos.financeosbackend.reporting.enums.ReportPeriodType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReportPeriodRequest {

    @NotNull
    private ReportPeriodType periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    public ReportPeriodRequest() {
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