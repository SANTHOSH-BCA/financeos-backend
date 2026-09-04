package com.financeos.financeosbackend.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpTransactionRequest {

    @NotNull(message = "Expected return date is required")
    private LocalDate expectedReturnDate;
}