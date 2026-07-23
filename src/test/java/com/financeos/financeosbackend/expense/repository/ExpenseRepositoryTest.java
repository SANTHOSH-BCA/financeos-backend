package com.financeos.financeosbackend.expense.repository;

import com.financeos.financeosbackend.expense.entity.Expense;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private com.financeos.financeosbackend.user.repository.UserRepository userRepository;

    private User createUser() {
        User user = new User();
        user.setFullName("Santhosh");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setPassword("Password@123");
        user.setFinancialProfile("STUDENT");
        return userRepository.save(user);
    }

    private Expense createExpense(User user) {
        Expense expense = new Expense();
        expense.setTitle("Lunch");
        expense.setAmount(new BigDecimal("250.00"));
        expense.setCategory("Food");
        expense.setExpenseDate(LocalDate.now());
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    @Test
    @DisplayName("Should find expenses by user")
    void findByUser_ShouldReturnExpenses() {

        User user = createUser();
        createExpense(user);

        List<Expense> expenses = expenseRepository.findByUser(user);

        assertFalse(expenses.isEmpty());
        assertEquals(1, expenses.size());
    }

    @Test
    @DisplayName("Should return paginated expenses")
    void findByUser_WithPageable_ShouldReturnPage() {

        User user = createUser();
        createExpense(user);

        Page<Expense> page =
                expenseRepository.findByUser(user, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Lunch", page.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Should find expense by id and user")
    void findByIdAndUser_ShouldReturnExpense() {

        User user = createUser();
        Expense savedExpense = createExpense(user);

        Optional<Expense> result =
                expenseRepository.findByIdAndUser(savedExpense.getId(), user);

        assertTrue(result.isPresent());
        assertEquals(savedExpense.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should calculate total expense")
    void getTotalExpenseByUser_ShouldReturnTotal() {

        User user = createUser();

        createExpense(user);

        Expense expense2 = new Expense();
        expense2.setTitle("Taxi");
        expense2.setAmount(new BigDecimal("150.00"));
        expense2.setCategory("Travel");
        expense2.setExpenseDate(LocalDate.now());
        expense2.setUser(user);

        expenseRepository.save(expense2);

        BigDecimal total =
                expenseRepository.getTotalExpenseByUser(user);

        assertEquals(new BigDecimal("400.00"), total);
    }

    @Test
    @DisplayName("Should count expenses by user")
    void countExpensesByUser_ShouldReturnCount() {

        User user = createUser();

        createExpense(user);

        Expense expense2 = new Expense();
        expense2.setTitle("Coffee");
        expense2.setAmount(new BigDecimal("100.00"));
        expense2.setCategory("Food");
        expense2.setExpenseDate(LocalDate.now());
        expense2.setUser(user);

        expenseRepository.save(expense2);

        Long count = expenseRepository.countExpensesByUser(user);

        assertEquals(2L, count);
    }
}