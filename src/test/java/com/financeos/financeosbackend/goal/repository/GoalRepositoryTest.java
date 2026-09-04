package com.financeos.financeosbackend.goal.repository;

import com.financeos.financeosbackend.goal.entity.Goal;
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

import static org.junit.jupiter.api.Assertions.*;import com.financeos.financeosbackend.goal.enums.GoalStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

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

    private Goal createGoal(User user) {
        Goal goal = new Goal();
        goal.setGoalName("Buy Laptop");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setGoalStatus(GoalStatus.ON_TRACK);
        goal.setUser(user);

        return goalRepository.save(goal);
    }

    @Test
    @DisplayName("Should find goals by user")
    void findByUser_ShouldReturnGoals() {

        User user = createUser();
        createGoal(user);

        List<Goal> goals = goalRepository.findByUser(user);

        assertFalse(goals.isEmpty());
        assertEquals(1, goals.size());
    }

    @Test
    @DisplayName("Should return paginated goals")
    void findByUser_WithPageable_ShouldReturnPage() {

        User user = createUser();
        createGoal(user);

        Page<Goal> page =
                goalRepository.findByUser(user, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Buy Laptop", page.getContent().get(0).getGoalName());
    }

    @Test
    @DisplayName("Should find goal by id and user")
    void findByIdAndUser_ShouldReturnGoal() {

        User user = createUser();
        Goal savedGoal = createGoal(user);

        Optional<Goal> result =
                goalRepository.findByIdAndUser(savedGoal.getId(), user);

        assertTrue(result.isPresent());
        assertEquals(savedGoal.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should count goals by user")
    void countGoalsByUser_ShouldReturnCount() {

        User user = createUser();

        createGoal(user);

        Goal goal2 = new Goal();
        goal2.setGoalName("Buy Bike");
        goal2.setTargetAmount(new BigDecimal("80000"));
        goal2.setCurrentAmount(new BigDecimal("10000"));
        goal2.setTargetDate(LocalDate.now().plusMonths(4));
        goal2.setGoalStatus(GoalStatus.ON_TRACK);
        goal2.setUser(user);

        goalRepository.save(goal2);

        Long count = goalRepository.countGoalsByUser(user);

        assertEquals(2L, count);
    }
}