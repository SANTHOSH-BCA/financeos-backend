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
import org.slf4j.LoggerFactory;

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

        if (request.getExpenseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Expense date cannot be in the future");
        }

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());
        User user = currentUserService.getCurrentUser();

        expense.setUser(user);

// Before saving
        logger.info("Creating expense '{}' for user: {}", request.getTitle(), user.getEmail());

        Expense savedExpense = expenseRepository.save(expense);

// After successful save
        logger.info("Expense created successfully with ID: {}", savedExpense.getId());

        ExpenseResponse response = new ExpenseResponse();

        response.setId(savedExpense.getId());
        response.setTitle(savedExpense.getTitle());
        response.setAmount(savedExpense.getAmount());
        response.setCategory(savedExpense.getCategory());
        response.setExpenseDate(savedExpense.getExpenseDate());

        return response;
    }

    public Page<ExpenseResponse> getMyExpenses(Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        return expenseRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public ExpenseResponse updateExpense(Long id, AddExpenseRequest request) {

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

        ExpenseResponse response = new ExpenseResponse();

        response.setId(updatedExpense.getId());
        response.setTitle(updatedExpense.getTitle());
        response.setAmount(updatedExpense.getAmount());
        response.setCategory(updatedExpense.getCategory());
        response.setExpenseDate(updatedExpense.getExpenseDate());

        return response;
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

    private ExpenseResponse mapToResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());

        return response;
    }



}