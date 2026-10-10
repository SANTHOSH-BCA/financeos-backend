package com.financeos.financeosbackend.user.repository;

import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save user successfully")
    void save_ShouldPersistUser() {
        User user = new User();
        user.setFullName("Santhosh");
        user.setEmail("save@gmail.com");
        user.setPassword("Password@123");
        user.setFinancialProfile("STUDENT");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Santhosh", savedUser.getFullName());
        assertEquals("save@gmail.com", savedUser.getEmail());
        assertEquals("STUDENT", savedUser.getFinancialProfile());
    }

    @Test
    @DisplayName("Should return user when email exists")
    void findByEmail_ShouldReturnUser() {
        User user = new User();
        user.setFullName("Santhosh");
        user.setEmail("find@gmail.com");
        user.setPassword("Password@123");
        user.setFinancialProfile("STUDENT");

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("find@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("Santhosh", result.get().getFullName());
        assertEquals("find@gmail.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void findByEmail_ShouldReturnEmpty() {
        Optional<User> result = userRepository.findByEmail("unknown@gmail.com");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return user by id")
    void findById_ShouldReturnUser() {
        User user = new User();
        user.setFullName("Santhosh");
        user.setEmail("findid@gmail.com");
        user.setPassword("Password@123");
        user.setFinancialProfile("STUDENT");

        User savedUser = userRepository.save(user);

        Optional<User> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should return user IDs in ascending order with pagination")
    void findNextUserIds_ShouldReturnIdsInAscendingOrder() {
        User first = new User();
        first.setFullName("First User");
        first.setEmail("first-user@gmail.com");
        first.setPassword("Password@123");
        first.setFinancialProfile("STUDENT");

        User second = new User();
        second.setFullName("Second User");
        second.setEmail("second-user@gmail.com");
        second.setPassword("Password@123");
        second.setFinancialProfile("STUDENT");

        User third = new User();
        third.setFullName("Third User");
        third.setEmail("third-user@gmail.com");
        third.setPassword("Password@123");
        third.setFinancialProfile("STUDENT");

        User savedFirst = userRepository.save(first);
        User savedSecond = userRepository.save(second);
        User savedThird = userRepository.save(third);

        Long cursor = Math.min(
                savedFirst.getId(),
                Math.min(savedSecond.getId(), savedThird.getId())
        ) - 1L;

        List<Long> result = userRepository.findNextUserIds(
                cursor,
                PageRequest.of(0, 2)
        );

        List<Long> expectedIds = List.of(
                savedFirst.getId(),
                savedSecond.getId(),
                savedThird.getId()
        ).stream().sorted().limit(2).toList();

        assertEquals(2, result.size());
        assertEquals(expectedIds, result);
    }

    @Test
    @DisplayName("Should return no user IDs when no later users exist")
    void findNextUserIds_ShouldReturnEmptyList() {
        List<Long> result = userRepository.findNextUserIds(
                Long.MAX_VALUE,
                PageRequest.of(0, 10)
        );

        assertTrue(result.isEmpty());
    }
}