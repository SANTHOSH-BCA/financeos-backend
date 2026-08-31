package com.financeos.financeosbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.financeos.financeosbackend.user.dto.LoginRequest;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should register user successfully")
    void registerUser_ShouldReturnSuccess() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setFullName("Santhosh");
        request.setEmail(UUID.randomUUID() + "@gmail.com");
        request.setPassword("Password@123");

        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("Santhosh"))
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    @DisplayName("Should validate invalid request")
    void registerUser_ShouldFailValidation() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setFullName("");
        request.setEmail("abc");
        request.setPassword("");

        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return test endpoint")
    void testEndpoint_ShouldReturnMessage() throws Exception {

        mockMvc.perform(get("/api/users/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("FinanceOS User Module Working!"));
    }

    @Test
    @DisplayName("Should return conflict when email already exists")
    void registerUser_ShouldFail_WhenEmailAlreadyExists() throws Exception {

        String email = UUID.randomUUID() + "@gmail.com";

        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Santhosh");
        request.setEmail(email);
        request.setPassword("Password@123");

        // First Registration
        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Second Registration
        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    @DisplayName("Should login user successfully")
    void loginUser_ShouldReturnToken() throws Exception {

        String email = UUID.randomUUID() + "@gmail.com";

        RegisterUserRequest register = new RegisterUserRequest();
        register.setFullName("Santhosh");
        register.setEmail(email);
        register.setPassword("Password@123");

        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("Password@123");

        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Login Successful"));
    }

    @Test
    @DisplayName("Should reject invalid password")
    void loginUser_ShouldFail_WhenPasswordIsWrong() throws Exception {

        String email = UUID.randomUUID() + "@gmail.com";

        RegisterUserRequest register = new RegisterUserRequest();
        register.setFullName("Santhosh");
        register.setEmail(email);
        register.setPassword("Password@123");

        mockMvc.perform(post("/api/users/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("WrongPassword");

        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should reject unknown email")
    void loginUser_ShouldFail_WhenEmailDoesNotExist() throws Exception {

        LoginRequest login = new LoginRequest();
        login.setEmail(UUID.randomUUID() + "@gmail.com");
        login.setPassword("Password@123");

        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should validate login request")
    void loginUser_ShouldFailValidation() throws Exception {

        LoginRequest login = new LoginRequest();
        login.setEmail("");
        login.setPassword("");

        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }
}