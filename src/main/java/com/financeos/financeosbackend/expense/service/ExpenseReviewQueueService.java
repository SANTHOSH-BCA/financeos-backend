package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.ExpenseReviewQueueResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import java.util.List;

@Service
public class ExpenseReviewQueueService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public ExpenseReviewQueueService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService) {

        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
    }

    public List<ExpenseReviewQueueResponse> getReviewQueue() {

        User user = currentUserService.getCurrentUser();

        List<TransactionStatus> reviewStatuses = List.of(
                TransactionStatus.PENDING,
                TransactionStatus.EDITED
        );

        return transactionRepository.findInboxTransactions(
                        user,
                        reviewStatuses
                )
                .stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.EXPENSE
                                || transaction.getType() == TransactionType.HELP_GIVEN
                                || transaction.getType() == TransactionType.INCOME)
                .map(this::mapToResponse)
                .toList();
    }

    private ExpenseReviewQueueResponse mapToResponse(
            FinancialTransaction transaction) {

        ExpenseReviewQueueResponse response =
                new ExpenseReviewQueueResponse();

        response.setTransactionId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setTransactionDateTime(
                transaction.getTransactionDateTime()
        );
        response.setType(transaction.getType());
        response.setCategory(transaction.getCategory());
        response.setSource(transaction.getSource());
        response.setStatus(transaction.getStatus());
        response.setMerchantPayee(transaction.getMerchantPayee());
        response.setDescription(transaction.getDescription());
        response.setReference(transaction.getReference());
        response.setLocation(transaction.getLocation());

        return response;
    }
}