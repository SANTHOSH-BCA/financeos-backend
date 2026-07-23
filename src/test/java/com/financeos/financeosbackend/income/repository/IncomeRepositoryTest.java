package com.financeos.financeosbackend.income.repository;

import com.financeos.financeosbackend.income.entity.Income;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
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
class IncomeRepositoryTest {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    private User createUser() {
        User user = new User();
        user.setFullName("Santhosh");
        user.setEmail(UUID.randomUUID() + "@gmail.com");
        user.setPassword("Password@123");
        user.setFinancialProfile("STUDENT");
        return userRepository.save(user);
    }

    private Income createIncome(User user) {
        Income income = new Income();
        income.setSource("Salary");
        income.setAmount(new BigDecimal("1000.00"));
        income.setIncomeDate(LocalDate.now());
        income.setUser(user);
        return incomeRepository.save(income);
    }

    @Test
    @DisplayName("Should find income by user")
    void findByUser_ShouldReturnIncome() {

        User user = createUser();
        createIncome(user);

        List<Income> incomes = incomeRepository.findByUser(user);

        assertFalse(incomes.isEmpty());
        assertEquals(1, incomes.size());
    }

    @Test
    @DisplayName("Should return paginated income")
    void findByUser_WithPageable_ShouldReturnPage() {

        User user = createUser();
        createIncome(user);

        Page<Income> page =
                incomeRepository.findByUser(user, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Salary", page.getContent().get(0).getSource());
    }

    @Test
    @DisplayName("Should find income by id and user")
    void findByIdAndUser_ShouldReturnIncome() {

        User user = createUser();
        Income savedIncome = createIncome(user);

        Optional<Income> result =
                incomeRepository.findByIdAndUser(savedIncome.getId(), user);

        assertTrue(result.isPresent());
        assertEquals(savedIncome.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should calculate total income")
    void getTotalIncomeByUser_ShouldReturnTotal() {

        User user = createUser();

        createIncome(user);

        Income income2 = new Income();
        income2.setSource("Freelancing");
        income2.setAmount(new BigDecimal("500.00"));
        income2.setIncomeDate(LocalDate.now());
        income2.setUser(user);

        incomeRepository.save(income2);

        BigDecimal total = incomeRepository.getTotalIncomeByUser(user);

        assertEquals(new BigDecimal("1500.00"), total);
    }

    @Test
    @DisplayName("Should count income by user")
    void countIncomeByUser_ShouldReturnCount() {

        User user = createUser();

        createIncome(user);

        Income income2 = new Income();
        income2.setSource("Bonus");
        income2.setAmount(new BigDecimal("300.00"));
        income2.setIncomeDate(LocalDate.now());
        income2.setUser(user);

        incomeRepository.save(income2);

        Long count = incomeRepository.countIncomeByUser(user);

        assertEquals(2L, count);
    }
}