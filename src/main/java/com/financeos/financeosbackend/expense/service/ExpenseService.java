package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    public ExpenseService(ExpenseRepository expenseRepository,
                          UserRepository userRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
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
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        ExpenseResponse response = new ExpenseResponse();

        response.setId(savedExpense.getId());
        response.setTitle(savedExpense.getTitle());
        response.setAmount(savedExpense.getAmount());
        response.setCategory(savedExpense.getCategory());
        response.setExpenseDate(savedExpense.getExpenseDate());

        return response;
    }

    public Page<ExpenseResponse> getMyExpenses(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return expenseRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public ExpenseResponse updateExpense(Long id, AddExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Optional<Expense> optionalExpense =
                expenseRepository.findByIdAndUser(id, user);

        if (optionalExpense.isEmpty()) {
            throw new RuntimeException("Expense not found");
        }

        Expense expense = optionalExpense.get();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());

        Expense updatedExpense = expenseRepository.save(expense);

        ExpenseResponse response = new ExpenseResponse();

        response.setId(updatedExpense.getId());
        response.setTitle(updatedExpense.getTitle());
        response.setAmount(updatedExpense.getAmount());
        response.setCategory(updatedExpense.getCategory());
        response.setExpenseDate(updatedExpense.getExpenseDate());

        return response;
    }

    public String deleteExpense(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Optional<Expense> optionalExpense =
                expenseRepository.findByIdAndUser(id, user);

        if (optionalExpense.isEmpty()) {
            throw new RuntimeException("Expense not found");
        }

        expenseRepository.delete(optionalExpense.get());

        return "Expense Deleted Successfully";

    }

    public Page<ExpenseResponse> filterExpenses(ExpenseFilterRequest request,
                                                Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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