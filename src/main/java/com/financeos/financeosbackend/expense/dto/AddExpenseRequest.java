package com.financeos.financeosbackend.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String title;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate expenseDate;

}