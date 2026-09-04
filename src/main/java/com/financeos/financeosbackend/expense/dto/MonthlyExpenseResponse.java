package com.financeos.financeosbackend.expense.dto;

import java.math.BigDecimal;

public record MonthlyExpenseResponse(
        String month,
        BigDecimal amount
) {
}