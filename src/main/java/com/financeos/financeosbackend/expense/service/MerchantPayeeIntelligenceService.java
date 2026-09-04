package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.MerchantPayeeInsightResponse;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import com.financeos.financeosbackend.transaction.repository.FinancialTransactionRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MerchantPayeeIntelligenceService {

    private final FinancialTransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public MerchantPayeeIntelligenceService(
            FinancialTransactionRepository transactionRepository,
            CurrentUserService currentUserService) {

        this.transactionRepository = transactionRepository;
        this.currentUserService = currentUserService;
    }

    public List<MerchantPayeeInsightResponse> getMerchantPayeeInsights() {

        User user = currentUserService.getCurrentUser();

        List<FinancialTransaction> transactions =
                transactionRepository.findByUser(user);

        Map<String, List<FinancialTransaction>> groupedTransactions =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.EXPENSE)
                        .filter(transaction ->
                                transaction.getMerchantPayee() != null
                                        && !transaction.getMerchantPayee().isBlank())
                        .collect(Collectors.groupingBy(
                                transaction ->
                                        transaction.getMerchantPayee().trim()
                        ));

        return groupedTransactions.entrySet()
                .stream()
                .map(entry -> {

                    List<FinancialTransaction> payeeTransactions =
                            entry.getValue();

                    BigDecimal totalAmount =
                            payeeTransactions.stream()
                                    .map(FinancialTransaction::getAmount)
                                    .reduce(
                                            BigDecimal.ZERO,
                                            BigDecimal::add
                                    );

                    String context =
                            determineSpendingContext(payeeTransactions);

                    return new MerchantPayeeInsightResponse(
                            entry.getKey(),
                            payeeTransactions.size(),
                            totalAmount,
                            context
                    );
                })
                .sorted(
                        Comparator.comparing(
                                MerchantPayeeInsightResponse::getTotalAmount
                        ).reversed()
                )
                .toList();
    }

    private String determineSpendingContext(
            List<FinancialTransaction> transactions) {

        boolean hasCategory =
                transactions.stream()
                        .anyMatch(transaction ->
                                transaction.getCategory() != null
                                        && !transaction.getCategory().isBlank());

        if (!hasCategory) {
            return "UNKNOWN_PAYEE_CONTEXT";
        }

        long recurringCount =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getCategory() != null)
                        .count();

        if (recurringCount >= 3) {
            return "REPEATED_PAYEE";
        }

        return "KNOWN_PAYEE";
    }
}