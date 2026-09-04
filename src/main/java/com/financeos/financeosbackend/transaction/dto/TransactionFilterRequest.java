package com.financeos.financeosbackend.transaction.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionSource;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
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
public class TransactionFilterRequest {

    private String search;

    private TransactionType type;

    private String category;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private TransactionSource source;

    private TransactionStatus status;
}