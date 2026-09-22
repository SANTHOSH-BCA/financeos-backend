package com.financeos.financeosbackend.reporting.dto.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LiabilityReportV2Response {

    private ReportSectionMetadata metadata;

    private BigDecimal recognizedLiabilities;
    private BigDecimal debtPayments;
    private BigDecimal principalPaid;
    private BigDecimal interestPaid;

    private List<ReportLiabilityItemV2Response> liabilities =
            new ArrayList<>();

    public LiabilityReportV2Response() {
    }

    public ReportSectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ReportSectionMetadata metadata) {
        this.metadata = metadata;
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(
            BigDecimal recognizedLiabilities) {
        this.recognizedLiabilities = recognizedLiabilities;
    }

    public BigDecimal getDebtPayments() {
        return debtPayments;
    }

    public void setDebtPayments(BigDecimal debtPayments) {
        this.debtPayments = debtPayments;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public List<ReportLiabilityItemV2Response> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(
            List<ReportLiabilityItemV2Response> liabilities) {
        this.liabilities = liabilities;
    }
}