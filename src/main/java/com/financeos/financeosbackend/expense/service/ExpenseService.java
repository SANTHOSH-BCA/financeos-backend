package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financeos.financeosbackend.expense.specification.ExpenseSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.financeos.financeosbackend.expense.dto.ExpenseFilterRequest;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import com.financeos.financeosbackend.transaction.enums.TransactionStatus;
import com.financeos.financeosbackend.transaction.enums.TransactionType;import com.financeos.financeosbackend.expense.dto.MonthlyExpenseResponse;import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;import com.financeos.financeosbackend.expense.dto.MonthlyExpenseResponse;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseService.class);

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    public ExpenseService(ExpenseRepository expenseRepository,
                          UserRepository userRepository,
                          CurrentUserService currentUserService) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public ExpenseResponse addExpense(AddExpenseRequest request) {

        validateExpenseDate(request.getExpenseDate());

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        User user = currentUserService.getCurrentUser();

        expense.setUser(user);

        // Before saving
        logger.info(
                "Creating expense '{}' for user: {}",
                request.getTitle(),
                user.getEmail()
        );

        Expense savedExpense = expenseRepository.save(expense);

        // After successful save
        logger.info(
                "Expense created successfully with ID: {}",
                savedExpense.getId()
        );

        return mapToResponse(savedExpense);
    }

    public Page<ExpenseResponse> getMyExpenses(Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        return expenseRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public ExpenseResponse updateExpense(Long id, AddExpenseRequest request) {

        validateExpenseDate(request.getExpenseDate());

        User user = currentUserService.getCurrentUser();

        logger.info("Updating expense with ID: {} for user: {}", id, user.getEmail());

        Optional<Expense> optionalExpense =
                expenseRepository.findByIdAndUser(id, user);

        if (optionalExpense.isEmpty()) {
            throw new ResourceNotFoundException("Expense not found");
        }

        Expense expense = optionalExpense.get();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        Expense updatedExpense = expenseRepository.save(expense);

        logger.info("Expense updated successfully with ID: {}", updatedExpense.getId());

        return mapToResponse(updatedExpense);
    }

    public void deleteExpense(Long id) {

        User user = currentUserService.getCurrentUser();

        logger.info("Deleting expense with ID: {} for user: {}", id, user.getEmail());

        Optional<Expense> optionalExpense =
                expenseRepository.findByIdAndUser(id, user);

        if (optionalExpense.isEmpty()) {
            throw new ResourceNotFoundException("Expense not found");
        }

        expenseRepository.delete(optionalExpense.get());

        logger.info("Expense deleted successfully with ID: {}", id);


    }

    public Page<ExpenseResponse> filterExpenses(ExpenseFilterRequest request,
                                                Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        Specification<Expense> specification =
                ExpenseSpecification.hasCategory(request.getCategory())
                        .and(ExpenseSpecification.hasMinAmount(request.getMinAmount()))
                        .and(ExpenseSpecification.hasMaxAmount(request.getMaxAmount()))
                        .and(ExpenseSpecification.hasStartDate(request.getStartDate()))
                        .and(ExpenseSpecification.hasEndDate(request.getEndDate()))
                        .and((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("user"), user));

        return expenseRepository.findAll(specification, pageable)
                .map(this::mapToResponse);
    }

    public List<MonthlyExpenseResponse> getMonthlyExpenseHistory() {

        User user = currentUserService.getCurrentUser();

        return expenseRepository.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        expense -> YearMonth.from(
                                expense.getExpenseDate()
                        ),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        new MonthlyExpenseResponse(
                                entry.getKey().toString(),
                                entry.getValue()
                        )
                )
                .toList();
    }

    private ExpenseResponse mapToResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());

        response.setTransactionId(
                expense.getTransaction() != null
                        ? expense.getTransaction().getId()
                        : null
        );

        return response;
    }

    private void validateExpenseDate(LocalDate expenseDate) {

        if (expenseDate == null) {
            throw new IllegalArgumentException("Expense date is required");
        }

        if (expenseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Expense date cannot be in the future"
            );
        }
    }

    public ExpenseResponse createExpenseFromTransaction(
            FinancialTransaction transaction) {

        User user = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        if (expenseRepository.findByTransaction(transaction).isPresent()) {
            throw new IllegalStateException(
                    "Transaction has already been converted to an expense"
            );
        }

        if (transaction.getStatus() != TransactionStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed transactions can become expenses"
            );
        }

        if (transaction.getType() != TransactionType.EXPENSE) {
            throw new IllegalStateException(
                    "Only expense transactions can become expenses"
            );
        }

        Expense expense = new Expense();

        expense.setTitle(
                transaction.getMerchantPayee() != null
                        ? transaction.getMerchantPayee()
                        : "Transaction Expense"
        );

        expense.setAmount(transaction.getAmount());

        expense.setCategory(
                transaction.getCategory() != null
                        ? transaction.getCategory()
                        : "Uncategorized"
        );

        expense.setExpenseDate(
                transaction.getTransactionDateTime().toLocalDate()
        );

        expense.setUser(user);
        expense.setTransaction(transaction);

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

    public ExpenseResponse createExpenseFromHelpTransaction(
            FinancialTransaction transaction) {

        User user = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        if (expenseRepository.findByTransaction(transaction).isPresent()) {
            throw new IllegalStateException(
                    "Help transaction has already been converted to an expense"
            );
        }

        if (transaction.getType() != TransactionType.HELP_GIVEN) {
            throw new IllegalStateException(
                    "Only help transactions can be converted to expenses"
            );
        }

        if (transaction.getStatus() != TransactionStatus.HELP_OVERDUE) {
            throw new IllegalStateException(
                    "Only overdue help transactions can be converted to expenses"
            );
        }

        Expense expense = new Expense();

        expense.setTitle(
                transaction.getMerchantPayee() != null
                        ? transaction.getMerchantPayee()
                        : "Help Expense"
        );

        expense.setAmount(transaction.getAmount());

        expense.setCategory(
                transaction.getCategory() != null
                        ? transaction.getCategory()
                        : "Uncategorized"
        );

        expense.setExpenseDate(
                transaction.getTransactionDateTime().toLocalDate()
        );

        expense.setUser(user);
        expense.setTransaction(transaction);

        Expense savedExpense =
                expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }



}