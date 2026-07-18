package com.financeos.financeosbackend.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddExpenseRequest {

    @NotBlank(message = "Expense title is required")
    private String title;

    @NotNull(message = "Expense amount is required")
    @Positive(message = "Expense amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Expense category is required")
    private String category;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

}