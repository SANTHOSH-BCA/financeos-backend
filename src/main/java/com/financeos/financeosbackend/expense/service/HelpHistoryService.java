package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.HelpHistoryResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HelpHistoryService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public HelpHistoryService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService) {
        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
    }

    public HelpHistoryResponse getHelpHistory(Long helpTransactionId) {

        User user = currentUserService.getCurrentUser();

        FinancialTransaction helpTransaction =
                transactionRepository.findByIdAndUser(
                        helpTransactionId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Help transaction not found"
                        ));

        if (helpTransaction.getType() != TransactionType.HELP_GIVEN
                && helpTransaction.getType() != TransactionType.EXPENSE) {
            throw new IllegalStateException(
                    "Transaction is not a Help transaction"
            );
        }

        BigDecimal returnedAmount =
                helpTransaction.getReturnedAmount();

        BigDecimal outstandingAmount =
                helpTransaction.getAmount()
                        .subtract(returnedAmount);

        List<HelpHistoryResponse.HelpReturnItemResponse> returns =
                helpTransaction.getHelpReturns()
                        .stream()
                        .map(returnTransaction -> {

                            HelpHistoryResponse.HelpReturnItemResponse item =
                                    new HelpHistoryResponse
                                            .HelpReturnItemResponse();

                            item.setTransactionId(
                                    returnTransaction.getId()
                            );

                            item.setAmount(
                                    returnTransaction.getAmount()
                            );

                            item.setTransactionDate(
                                    returnTransaction
                                            .getTransactionDateTime()
                                            .toLocalDate()
                            );

                            item.setMerchantPayee(
                                    returnTransaction.getMerchantPayee()
                            );

                            return item;
                        })
                        .toList();

        HelpHistoryResponse response =
                new HelpHistoryResponse();

        response.setHelpTransactionId(
                helpTransaction.getId()
        );

        response.setHelpAmount(
                helpTransaction.getAmount()
        );

        response.setReturnedAmount(
                returnedAmount
        );

        response.setOutstandingAmount(
                outstandingAmount.max(BigDecimal.ZERO)
        );

        response.setExpectedReturnDate(
                helpTransaction.getExpectedReturnDate()
        );

        response.setStatus(
                helpTransaction.getHelpStatus()
        );

        response.setReturns(returns);

        return response;
    }
}