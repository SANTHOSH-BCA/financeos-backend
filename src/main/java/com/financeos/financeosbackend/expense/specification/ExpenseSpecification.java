package com.financeos.financeosbackend.expense.specification;

import com.financeos.financeosbackend.expense.entity.Expense;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSpecification {

    public static Specification<Expense> hasCategory(String category) {

        return (root, query, criteriaBuilder) -> {

            if (category == null || category.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Expense> hasMinAmount(BigDecimal minAmount) {

        return (root, query, criteriaBuilder) -> {

            if (minAmount == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount);
        };
    }

    public static Specification<Expense> hasMaxAmount(BigDecimal maxAmount) {

        return (root, query, criteriaBuilder) -> {

            if (maxAmount == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount);
        };
    }

    public static Specification<Expense> hasStartDate(LocalDate startDate) {

        return (root, query, criteriaBuilder) -> {

            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), startDate);
        };
    }

    public static Specification<Expense> hasEndDate(LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {

            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), endDate);
        };
    }
}