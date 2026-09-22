package com.financeos.financeosbackend.reporting.collector.position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportLiabilityData {

    private BigDecimal recognizedLiabilities = BigDecimal.ZERO;
    private BigDecimal debtPayments = BigDecimal.ZERO;
    private BigDecimal principalPaid = BigDecimal.ZERO;
    private BigDecimal interestPaid = BigDecimal.ZERO;

    private List<ReportLiabilityItemData> liabilities = new ArrayList<>();

    public ReportLiabilityData() {
    }

    public BigDecimal getRecognizedLiabilities() {
        return recognizedLiabilities;
    }

    public void setRecognizedLiabilities(BigDecimal recognizedLiabilities) {
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

    public List<ReportLiabilityItemData> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<ReportLiabilityItemData> liabilities) {
        this.liabilities = liabilities;
    }
}