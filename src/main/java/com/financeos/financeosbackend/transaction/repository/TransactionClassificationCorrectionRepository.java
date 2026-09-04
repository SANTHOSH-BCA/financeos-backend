package com.financeos.financeosbackend.transaction.repository;

import com.financeos.financeosbackend.transaction.entity.TransactionClassificationCorrection;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionClassificationCorrectionRepository
        extends JpaRepository<TransactionClassificationCorrection, Long> {

    List<TransactionClassificationCorrection> findByTransaction(
            FinancialTransaction transaction
    );
}