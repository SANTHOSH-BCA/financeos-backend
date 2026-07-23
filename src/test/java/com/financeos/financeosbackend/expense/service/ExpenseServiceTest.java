package com.financeos.financeosbackend.expense.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseFilterRequest;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void addExpense_ShouldAddExpenseSuccessfully() {

        AddExpenseRequest request = new AddExpenseRequest();
        request.setTitle("Food");
        request.setAmount(new BigDecimal("500"));
        request.setCategory("Food");
        request.setExpenseDate(LocalDate.now());

        User user = new User();
        user.setId(1L);
        user.setEmail("santhosh@gmail.com");

        Expense savedExpense = new Expense();
        savedExpense.setTitle("Food");
        savedExpense.setAmount(new BigDecimal("500"));
        savedExpense.setCategory("Food");
        savedExpense.setExpenseDate(LocalDate.now());

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(savedExpense);

        ExpenseResponse response =
                expenseService.addExpense(request);

        assertNotNull(response);
        assertEquals("Food", response.getTitle());
        assertEquals(new BigDecimal("500"), response.getAmount());
        assertEquals("Food", response.getCategory());
        assertEquals(LocalDate.now(), response.getExpenseDate());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void addExpense_ShouldThrowException_WhenExpenseDateIsFuture() {

        AddExpenseRequest request = new AddExpenseRequest();
        request.setTitle("Food");
        request.setAmount(new BigDecimal("500"));
        request.setCategory("Food");
        request.setExpenseDate(LocalDate.now().plusDays(1));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> expenseService.addExpense(request));

        assertEquals(
                "Expense date cannot be in the future",
                exception.getMessage());

        verify(expenseRepository, never()).save(any());
    }

    @Test
    void getMyExpenses_ShouldReturnExpenses() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(new BigDecimal("500"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.now());

        Page<Expense> page =
                new PageImpl<>(List.of(expense));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByUser(
                eq(user),
                any(PageRequest.class)))
                .thenReturn(page);

        Page<ExpenseResponse> response =
                expenseService.getMyExpenses(PageRequest.of(0,5));

        assertEquals(1, response.getTotalElements());

        ExpenseResponse first =
                response.getContent().get(0);

        assertEquals("Food", first.getTitle());
        assertEquals(new BigDecimal("500"), first.getAmount());
        assertEquals("Food", first.getCategory());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository)
                .findByUser(eq(user), any(PageRequest.class));
    }

    @Test
    void updateExpense_ShouldUpdateSuccessfully() {

        AddExpenseRequest request = new AddExpenseRequest();
        request.setTitle("Rent");
        request.setAmount(new BigDecimal("12000"));
        request.setCategory("Housing");
        request.setExpenseDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(new BigDecimal("500"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.now().minusDays(1));
        expense.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(expense));

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(expense);

        ExpenseResponse response =
                expenseService.updateExpense(1L, request);

        assertNotNull(response);
        assertEquals("Rent", response.getTitle());
        assertEquals(new BigDecimal("12000"), response.getAmount());
        assertEquals("Housing", response.getCategory());
        assertEquals(LocalDate.now(), response.getExpenseDate());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByIdAndUser(1L, user);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void updateExpense_ShouldThrowException_WhenExpenseNotFound() {

        AddExpenseRequest request = new AddExpenseRequest();
        request.setTitle("Food");
        request.setAmount(new BigDecimal("500"));
        request.setCategory("Food");
        request.setExpenseDate(LocalDate.now());

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> expenseService.updateExpense(1L, request));

        assertEquals("Expense not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByIdAndUser(1L, user);
        verify(expenseRepository, never()).save(any());
    }

    @Test
    void deleteExpense_ShouldDeleteSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(new BigDecimal("500"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.now());
        expense.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpense(1L);

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByIdAndUser(1L, user);
        verify(expenseRepository).delete(expense);
    }

    @Test
    void deleteExpense_ShouldThrowException_WhenExpenseNotFound() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> expenseService.deleteExpense(1L));

        assertEquals("Expense not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository).findByIdAndUser(1L, user);
        verify(expenseRepository, never()).delete(any(Expense.class));
    }

    @Test
    void filterExpenses_ShouldReturnFilteredExpenses() {

        ExpenseFilterRequest request = new ExpenseFilterRequest();
        request.setCategory("Food");

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Expense expense = new Expense();
        expense.setTitle("Lunch");
        expense.setAmount(new BigDecimal("250"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.now());

        Page<Expense> page =
                new PageImpl<>(List.of(expense));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(expenseRepository.findAll(
                any(Specification.class),
                any(PageRequest.class)))
                .thenReturn(page);

        Page<ExpenseResponse> response =
                expenseService.filterExpenses(
                        request,
                        PageRequest.of(0, 5));

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        ExpenseResponse first =
                response.getContent().get(0);

        assertEquals("Lunch", first.getTitle());
        assertEquals(new BigDecimal("250"), first.getAmount());
        assertEquals("Food", first.getCategory());

        verify(currentUserService).getCurrentUser();
        verify(expenseRepository)
                .findAll(any(Specification.class), any(PageRequest.class));
    }

}