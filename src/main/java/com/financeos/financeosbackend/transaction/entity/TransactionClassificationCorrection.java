package com.financeos.financeosbackend.transaction.entity;

import com.financeos.financeosbackend.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_classification_corrections")
public class TransactionClassificationCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private FinancialTransaction transaction;

    @Enumerated(EnumType.STRING)
    private TransactionType suggestedType;

    @Column(length = 100)
    private String suggestedCategory;

    @Enumerated(EnumType.STRING)
    private TransactionType correctedType;

    @Column(length = 100)
    private String correctedCategory;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}