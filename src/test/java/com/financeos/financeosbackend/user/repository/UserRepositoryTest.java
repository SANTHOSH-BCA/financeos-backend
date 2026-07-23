package com.financeos.financeosbackend.user.repository;

import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        Optional<User> result =
                userRepository.findByEmail("unknown@gmail.com");

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

        Optional<User> result =
                userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getId());
    }
}