package com.financeos.financeosbackend.reporting.dto.v2;

public class ReportSectionMetadata {

    private ReportSectionStatus status;
    private String message;

    public ReportSectionMetadata() {
    }

    public ReportSectionMetadata(
            ReportSectionStatus status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public ReportSectionStatus getStatus() {
        return status;
    }

    public void setStatus(ReportSectionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}