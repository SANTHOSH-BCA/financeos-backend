package com.financeos.financeosbackend.integration.helper;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class InvestmentTestHelper {

    private InvestmentTestHelper() {
    }

    public static AddInvestmentRequest validInvestment() {

        AddInvestmentRequest request = new AddInvestmentRequest();

        request.setInvestmentName("HDFC SIP");
        request.setInvestmentType("Mutual Fund");
        request.setAmount(new BigDecimal("5000"));
        request.setInvestmentDate(LocalDate.now());

        return request;
    }

    public static AddInvestmentRequest updatedInvestment() {

        AddInvestmentRequest request = new AddInvestmentRequest();

        request.setInvestmentName("ICICI SIP");
        request.setInvestmentType("Stock");
        request.setAmount(new BigDecimal("8000"));
        request.setInvestmentDate(LocalDate.now());

        return request;
    }

    public static AddInvestmentRequest invalidInvestment() {

        AddInvestmentRequest request = new AddInvestmentRequest();

        request.setInvestmentName("");
        request.setInvestmentType("");
        request.setAmount(BigDecimal.ZERO);
        request.setInvestmentDate(null);

        return request;
    }
}