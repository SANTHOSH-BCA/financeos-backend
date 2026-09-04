package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionDuplicateDetectionService {

    private final FinancialTransactionRepository transactionRepository;

    public TransactionDuplicateDetectionService(
            FinancialTransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    public boolean isPossibleDuplicate(
            FinancialTransaction transaction) {

        User user = transaction.getUser();

        LocalDateTime transactionTime =
                transaction.getTransactionDateTime();

        LocalDateTime start =
                transactionTime.minusMinutes(5);

        LocalDateTime end =
                transactionTime.plusMinutes(5);

        List<FinancialTransaction> candidates =
                transactionRepository.findPossibleDuplicates(
                        user,
                        transaction.getAmount(),
                        start,
                        end
                );

        return candidates.stream()
                .anyMatch(candidate ->
                        !candidate.getId().equals(transaction.getId())
                                && isSameMerchant(
                                candidate,
                                transaction
                        )
                                && isSameReference(
                                candidate,
                                transaction
                        )
                                && candidate.getSource()
                                == transaction.getSource()
                );
    }

    private boolean isSameMerchant(
            FinancialTransaction first,
            FinancialTransaction second) {

        if (first.getMerchantPayee() == null
                || second.getMerchantPayee() == null) {

            return false;
        }

        return first.getMerchantPayee()
                .equalsIgnoreCase(second.getMerchantPayee());
    }

    private boolean isSameReference(
            FinancialTransaction first,
            FinancialTransaction second) {

        if (first.getReference() == null
                && second.getReference() == null) {
            return true;
        }

        if (first.getReference() == null
                || second.getReference() == null) {
            return false;
        }

        return first.getReference()
                .equalsIgnoreCase(second.getReference());
    }
}