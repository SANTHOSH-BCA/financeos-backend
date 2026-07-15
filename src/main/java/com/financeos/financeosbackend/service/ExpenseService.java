package com.financeos.financeosbackend.service;

import com.financeos.financeosbackend.dto.AddExpenseRequest;
import com.financeos.financeosbackend.dto.ExpenseResponse;
import com.financeos.financeosbackend.entity.Expense;
import com.financeos.financeosbackend.repository.ExpenseRepository;
import com.financeos.financeosbackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    public ExpenseResponse addExpense(AddExpenseRequest request) {

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setExpenseDate(request.getExpenseDate());
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

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

    public List<ExpenseResponse> getMyExpenses() {
        System.out.println("Inside getMyExpenses()");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

        List<Expense> expenses = expenseRepository.findByUser(user);

        List<ExpenseResponse> responses = new ArrayList<>();

        for (Expense expense : expenses) {

            ExpenseResponse response = new ExpenseResponse();

            response.setId(expense.getId());
            response.setTitle(expense.getTitle());
            response.setAmount(expense.getAmount());
            response.setCategory(expense.getCategory());
            response.setExpenseDate(expense.getExpenseDate());

            responses.add(response);
        }

        return responses;
    }

    public ExpenseResponse updateExpense(Long id, AddExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).get();

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

        User user = userRepository.findByEmail(email).get();

        Optional<Expense> optionalExpense =
                expenseRepository.findByIdAndUser(id, user);

        if (optionalExpense.isEmpty()) {
            throw new RuntimeException("Expense not found");
        }

        expenseRepository.delete(optionalExpense.get());

        return "Expense Deleted Successfully";

    }

}