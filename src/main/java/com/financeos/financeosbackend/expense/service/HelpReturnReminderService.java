package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.HelpReturnReminderResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HelpReturnReminderService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public HelpReturnReminderService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService) {

        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public List<HelpReturnReminderResponse> getHelpReturnReminders() {

        User user = currentUserService.getCurrentUser();

        LocalDate today = LocalDate.now();

        List<FinancialTransaction> helpTransactions =
                transactionRepository.findByUserAndStatus(
                        user,
                        TransactionStatus.PENDING
                );

        return helpTransactions.stream()
                .filter(transaction ->
                        transaction.getType()
                                == TransactionType.HELP_GIVEN)
                .filter(transaction ->
                        transaction.getExpectedReturnDate() != null)
                .map(transaction -> {

                    LocalDate expectedReturnDate =
                            transaction.getExpectedReturnDate();

                    if (expectedReturnDate.isBefore(today)) {

                        transaction.setStatus(
                                TransactionStatus.HELP_OVERDUE
                        );

                        transactionRepository.save(transaction);

                        return new HelpReturnReminderResponse(
                                transaction.getId(),
                                transaction.getAmount(),
                                transaction.getMerchantPayee(),
                                expectedReturnDate,
                                "OVERDUE",
                                "Help return is overdue. Was the money returned?"
                        );
                    }

                    if (expectedReturnDate.isEqual(today)) {

                        return new HelpReturnReminderResponse(
                                transaction.getId(),
                                transaction.getAmount(),
                                transaction.getMerchantPayee(),
                                expectedReturnDate,
                                "DUE_TODAY",
                                "Help return is due today. Was the money returned?"
                        );
                    }

                    return null;
                })
                .filter(response -> response != null)
                .toList();
    }
}