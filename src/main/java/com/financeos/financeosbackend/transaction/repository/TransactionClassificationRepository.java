package com.financeos.financeosbackend.transaction.repository;

import com.financeos.financeosbackend.transaction.entity.TransactionClassification;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionClassificationRepository
        extends JpaRepository<TransactionClassification, Long> {

    Optional<TransactionClassification> findByTransaction(
            FinancialTransaction transaction
    );
}