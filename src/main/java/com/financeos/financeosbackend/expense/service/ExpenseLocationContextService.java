package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.ExpenseLocationContextResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseLocationContextService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public ExpenseLocationContextService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService) {

        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
    }

    public List<ExpenseLocationContextResponse> getLocationContexts() {

        User user = currentUserService.getCurrentUser();

        return transactionRepository.findByUser(user)
                .stream()
                .filter(transaction ->
                        transaction.getType() == TransactionType.EXPENSE)
                .filter(transaction ->
                        transaction.getLocation() != null
                                && !transaction.getLocation().isBlank())
                .map(this::mapToResponse)
                .toList();
    }

    private ExpenseLocationContextResponse mapToResponse(
            FinancialTransaction transaction) {

        String context;

        if (transaction.getMerchantPayee() != null
                && !transaction.getMerchantPayee().isBlank()
                && transaction.getCategory() != null
                && !transaction.getCategory().isBlank()) {

            context = "Location supports the existing merchant and category context";

        } else if (transaction.getMerchantPayee() != null
                && !transaction.getMerchantPayee().isBlank()) {

            context = "Location supports the merchant/payee context";

        } else {

            context = "Location available as supporting transaction context";
        }

        return new ExpenseLocationContextResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getMerchantPayee(),
                transaction.getLocation(),
                transaction.getCategory(),
                context
        );
    }
}