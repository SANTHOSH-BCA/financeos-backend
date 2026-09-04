package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpReturnDetectionService {

    private final FinancialTransactionRepository transactionRepository;

    public HelpReturnDetectionService(
            FinancialTransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    public List<FinancialTransaction> findPossibleReturns(
            FinancialTransaction helpTransaction) {

        User user = helpTransaction.getUser();

        LocalDateTime helpDate =
                helpTransaction.getTransactionDateTime();

        LocalDateTime startTime = helpDate;

        LocalDateTime endTime =
                helpTransaction.getExpectedReturnDate()
                        .plusDays(30)
                        .atTime(23, 59, 59);

        BigDecimal helpAmount = helpTransaction.getAmount();

        BigDecimal tolerance =
                helpAmount.multiply(BigDecimal.valueOf(0.10));

        BigDecimal minAmount =
                helpAmount.subtract(tolerance);

        BigDecimal maxAmount =
                helpAmount.add(tolerance);

        return transactionRepository.findPossibleHelpReturns(
                user,
                TransactionType.INCOME,
                minAmount,
                maxAmount,
                startTime,
                endTime,
                TransactionStatus.REJECTED
        );
    }
}