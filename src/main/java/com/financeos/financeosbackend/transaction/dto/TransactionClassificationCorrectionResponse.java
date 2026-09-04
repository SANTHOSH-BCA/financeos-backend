package com.financeos.financeosbackend.transaction.dto;

import com.financeos.financeosbackend.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionClassificationCorrectionResponse {

    private Long id;

    private Long transactionId;

    private TransactionType suggestedType;

    private String suggestedCategory;

    private TransactionType correctedType;

    private String correctedCategory;

    private LocalDateTime createdAt;
}