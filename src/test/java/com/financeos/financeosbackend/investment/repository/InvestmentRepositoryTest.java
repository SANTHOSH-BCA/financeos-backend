package com.financeos.financeosbackend.investment.repository;

import com.financeos.financeosbackend.investment.entity.Investment;
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
class InvestmentRepositoryTest {

    @Autowired
    private InvestmentRepository investmentRepository;

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

    private Investment createInvestment(User user) {
        Investment investment = new Investment();
        investment.setInvestmentName("Nifty 50");
        investment.setInvestmentType("MUTUAL_FUND");
        investment.setAmount(new BigDecimal("5000"));
        investment.setInvestmentDate(LocalDate.now());
        investment.setUser(user);

        return investmentRepository.save(investment);
    }

    @Test
    @DisplayName("Should return paginated investments")
    void findByUser_ShouldReturnPage() {

        User user = createUser();
        createInvestment(user);

        Page<Investment> page =
                investmentRepository.findByUser(user, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Nifty 50",
                page.getContent().get(0).getInvestmentName());
    }

    @Test
    @DisplayName("Should return investment by id and user")
    void findByIdAndUser_ShouldReturnInvestment() {

        User user = createUser();

        Investment savedInvestment = createInvestment(user);

        Optional<Investment> result =
                investmentRepository.findByIdAndUser(savedInvestment.getId(), user);

        assertTrue(result.isPresent());
        assertEquals(savedInvestment.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should calculate total investment")
    void getTotalInvestmentByUser_ShouldReturnTotal() {

        User user = createUser();

        createInvestment(user);

        Investment investment2 = new Investment();
        investment2.setInvestmentName("Gold ETF");
        investment2.setInvestmentType("ETF");
        investment2.setAmount(new BigDecimal("3000"));
        investment2.setInvestmentDate(LocalDate.now());
        investment2.setUser(user);

        investmentRepository.save(investment2);

        BigDecimal total =
                investmentRepository.getTotalInvestmentByUser(user);

        assertEquals(0, total.compareTo(new BigDecimal("8000")));
    }

    @Test
    @DisplayName("Should count investments by user")
    void countInvestmentsByUser_ShouldReturnCount() {

        User user = createUser();

        createInvestment(user);

        Investment investment2 = new Investment();
        investment2.setInvestmentName("Gold ETF");
        investment2.setInvestmentType("ETF");
        investment2.setAmount(new BigDecimal("3000"));
        investment2.setInvestmentDate(LocalDate.now());
        investment2.setUser(user);

        investmentRepository.save(investment2);

        Long count =
                investmentRepository.countInvestmentsByUser(user);

        assertEquals(2L, count);
    }

    @Test
    @DisplayName("Should return investment distribution by type")
    void getInvestmentDistributionByUser_ShouldReturnDistribution() {

        User user = createUser();

        createInvestment(user);

        Investment investment2 = new Investment();
        investment2.setInvestmentName("Gold ETF");
        investment2.setInvestmentType("ETF");
        investment2.setAmount(new BigDecimal("3000"));
        investment2.setInvestmentDate(LocalDate.now());
        investment2.setUser(user);

        investmentRepository.save(investment2);

        List<Object[]> distribution =
                investmentRepository.getInvestmentDistributionByUser(user);

        assertEquals(2, distribution.size());
    }
}