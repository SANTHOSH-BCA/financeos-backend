package com.financeos.financeosbackend.transaction.specification;

import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionSource;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinancialTransactionSpecification {

    public static Specification<FinancialTransaction> hasSearch(String search) {

        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("merchantPayee")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("reference")),
                            pattern
                    )
            );
        };
    }

    public static Specification<FinancialTransaction> hasType(
            TransactionType type) {

        return (root, query, criteriaBuilder) -> {

            if (type == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<FinancialTransaction> hasCategory(
            String category) {

        return (root, query, criteriaBuilder) -> {

            if (category == null || category.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("category"),
                    category
            );
        };
    }

    public static Specification<FinancialTransaction> hasMinAmount(
            BigDecimal minAmount) {

        return (root, query, criteriaBuilder) -> {

            if (minAmount == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("amount"),
                    minAmount
            );
        };
    }

    public static Specification<FinancialTransaction> hasMaxAmount(
            BigDecimal maxAmount) {

        return (root, query, criteriaBuilder) -> {

            if (maxAmount == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("amount"),
                    maxAmount
            );
        };
    }

    public static Specification<FinancialTransaction> hasStartDate(
            LocalDate startDate) {

        return (root, query, criteriaBuilder) -> {

            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDateTime =
                    startDate.atStartOfDay();

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("transactionDateTime"),
                    startDateTime
            );
        };
    }

    public static Specification<FinancialTransaction> hasEndDate(
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {

            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime endDateTime =
                    endDate.plusDays(1).atStartOfDay();

            return criteriaBuilder.lessThan(
                    root.get("transactionDateTime"),
                    endDateTime
            );
        };
    }

    public static Specification<FinancialTransaction> hasSource(
            TransactionSource source) {

        return (root, query, criteriaBuilder) -> {

            if (source == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("source"),
                    source
            );
        };
    }

    public static Specification<FinancialTransaction> hasStatus(
            TransactionStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<FinancialTransaction> belongsToUser(
            com.financeos.financeosbackend.user.entity.User user) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }
}