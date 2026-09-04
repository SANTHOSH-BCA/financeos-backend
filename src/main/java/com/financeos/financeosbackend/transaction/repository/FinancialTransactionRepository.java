package com.financeos.financeosbackend.transaction.repository;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransaction, Long>,
        JpaSpecificationExecutor<FinancialTransaction> {

    Page<FinancialTransaction> findByUser(
            User user,
            Pageable pageable
    );

    List<FinancialTransaction> findByUser(User user);

    Optional<FinancialTransaction> findByIdAndUser(
            Long id,
            User user
    );

    List<FinancialTransaction> findByUserAndStatus(
            User user,
            TransactionStatus status
    );

    @Query("""
        SELECT t
        FROM FinancialTransaction t
        WHERE t.user = :user
          AND t.amount = :amount
          AND t.transactionDateTime BETWEEN :startTime AND :endTime
        """)
    List<FinancialTransaction> findPossibleDuplicates(
            @Param("user") User user,
            @Param("amount") BigDecimal amount,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
        SELECT t
        FROM FinancialTransaction t
        WHERE t.user = :user
          AND t.status IN :statuses
        ORDER BY t.transactionDateTime DESC
        """)
    List<FinancialTransaction> findInboxTransactions(
            @Param("user") User user,
            @Param("statuses") List<TransactionStatus> statuses
    );

    @Query("""
        SELECT t
        FROM FinancialTransaction t
        WHERE t.type = :type
          AND t.status = :status
          AND t.expectedReturnDate < :today
        """)
    List<FinancialTransaction> findOverdueHelpTransactions(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status,
            @Param("today") LocalDate today
    );

    @Query("""
        SELECT t
        FROM FinancialTransaction t
        WHERE t.user = :user
          AND t.type = :type
          AND t.amount BETWEEN :minAmount AND :maxAmount
          AND t.transactionDateTime >= :startTime
          AND t.transactionDateTime <= :endTime
          AND t.status = :status
        ORDER BY t.transactionDateTime ASC
        """)
    List<FinancialTransaction> findPossibleHelpReturns(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") TransactionStatus status
    );
}