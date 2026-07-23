package com.financeos.financeosbackend.integration.helper;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class ExpenseTestHelper {

    private ExpenseTestHelper() {
    }

    public static AddExpenseRequest validExpense() {

        AddExpenseRequest request = new AddExpenseRequest();

        request.setTitle("Petrol");
        request.setAmount(new BigDecimal("500"));
        request.setCategory("Transport");
        request.setExpenseDate(LocalDate.now());

        return request;
    }

    public static AddExpenseRequest invalidExpense() {

        AddExpenseRequest request = new AddExpenseRequest();

        request.setTitle("");
        request.setAmount(BigDecimal.ZERO);
        request.setCategory("");
        request.setExpenseDate(null);

        return request;
    }

    public static AddExpenseRequest futureExpense() {

        AddExpenseRequest request = validExpense();

        request.setExpenseDate(LocalDate.now().plusDays(5));

        return request;
    }

    public static AddExpenseRequest updatedExpense() {

        AddExpenseRequest request = new AddExpenseRequest();

        request.setTitle("Lunch");
        request.setAmount(new BigDecimal("250"));
        request.setCategory("Food");
        request.setExpenseDate(LocalDate.now());

        return request;
    }

}