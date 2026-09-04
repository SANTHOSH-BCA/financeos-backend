package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDate;
import java.util.List;

@Service
public class HelpOverdueService {

    private final FinancialTransactionRepository transactionRepository;

    public HelpOverdueService(
            FinancialTransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public int markOverdueHelpTransactions() {

        LocalDate today = LocalDate.now();

        List<FinancialTransaction> transactions =
                transactionRepository.findOverdueHelpTransactions(
                        TransactionType.HELP_GIVEN,
                        TransactionStatus.PENDING,
                        today
                );

        for (FinancialTransaction transaction : transactions) {
            transaction.setStatus(TransactionStatus.HELP_OVERDUE);
        }

        transactionRepository.saveAll(transactions);

        return transactions.size();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkOverdueHelpTransactions() {

        markOverdueHelpTransactions();
    }
}