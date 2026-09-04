package com.financeos.financeosbackend.transaction.service;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionClassificationEngine {

    public ClassificationResult classify(
            FinancialTransaction transaction) {

        String text = buildSearchText(transaction);

        if (containsAny(text, "salon", "spa", "parlour", "barber")) {
            return new ClassificationResult(
                    TransactionType.EXPENSE,
                    "Personal Care",
                    new BigDecimal("0.90")
            );
        }

        if (containsAny(text, "petrol", "fuel", "gas station")) {
            return new ClassificationResult(
                    TransactionType.EXPENSE,
                    "Transport",
                    new BigDecimal("0.90")
            );
        }

        if (containsAny(text, "restaurant", "hotel", "cafe", "food")) {
            return new ClassificationResult(
                    TransactionType.EXPENSE,
                    "Food",
                    new BigDecimal("0.85")
            );
        }

        if (containsAny(text, "salary", "payroll")) {
            return new ClassificationResult(
                    TransactionType.INCOME,
                    "Salary",
                    new BigDecimal("0.95")
            );
        }

        return new ClassificationResult(
                transaction.getType(),
                transaction.getCategory(),
                new BigDecimal("0.50")
        );
    }

    private String buildSearchText(
            FinancialTransaction transaction) {

        return (
                safe(transaction.getMerchantPayee()) + " " +
                        safe(transaction.getDescription()) + " " +
                        safe(transaction.getReference())
        ).toLowerCase();
    }

    private boolean containsAny(
            String text,
            String... keywords) {

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public record ClassificationResult(
            TransactionType suggestedType,
            String suggestedCategory,
            BigDecimal confidence
    ) {
    }
}