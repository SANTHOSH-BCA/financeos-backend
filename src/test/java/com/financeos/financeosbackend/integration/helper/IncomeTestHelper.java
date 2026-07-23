package com.financeos.financeosbackend.integration.helper;

import com.financeos.financeosbackend.income.dto.AddIncomeRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class IncomeTestHelper {

    private IncomeTestHelper() {
    }

    public static AddIncomeRequest validIncome() {

        AddIncomeRequest request = new AddIncomeRequest();

        request.setSource("Salary");
        request.setAmount(new BigDecimal("25000"));
        request.setIncomeDate(LocalDate.now());

        return request;
    }

    public static AddIncomeRequest invalidIncome() {

        AddIncomeRequest request = new AddIncomeRequest();

        request.setSource("");
        request.setAmount(BigDecimal.ZERO);
        request.setIncomeDate(null);

        return request;
    }

    public static AddIncomeRequest updatedIncome() {

        AddIncomeRequest request = new AddIncomeRequest();

        request.setSource("Freelancing");
        request.setAmount(new BigDecimal("30000"));
        request.setIncomeDate(LocalDate.now());

        return request;
    }

}