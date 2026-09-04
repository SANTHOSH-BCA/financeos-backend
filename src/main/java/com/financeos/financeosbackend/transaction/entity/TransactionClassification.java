package com.financeos.financeosbackend.transaction.entity;

import com.financeos.financeosbackend.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_classifications")
public class TransactionClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private FinancialTransaction transaction;

    @Enumerated(EnumType.STRING)
    private TransactionType suggestedType;

    @Column(length = 100)
    private String suggestedCategory;

    @Column(precision = 5, scale = 2)
    private BigDecimal confidence;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}