package com.financeos.financeosbackend.expense.dto;

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
public class ExpenseResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private String category;

    private LocalDate expenseDate;

}