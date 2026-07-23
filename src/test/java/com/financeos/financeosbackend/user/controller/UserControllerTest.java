package com.financeos.financeosbackend.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.user.dto.LoginRequest;
import com.financeos.financeosbackend.user.dto.LoginResponse;
import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import com.financeos.financeosbackend.user.dto.UserResponse;
import com.financeos.financeosbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.financeos.financeosbackend.filter.JwtAuthenticationFilter;
import com.financeos.financeosbackend.security.JwtService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void test_ShouldReturnWorkingMessage() throws Exception {

        mockMvc.perform(get("/api/users/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("FinanceOS User Module Working!"));
    }

    @Test
    void registerUser_ShouldReturnUserResponse() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Santhosh");
        request.setEmail("santhosh@gmail.com");
        request.setPassword("Password@123");
        request.setFinancialProfile("STUDENT");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setFullName("Santhosh");
        response.setEmail("santhosh@gmail.com");
        response.setFinancialProfile("STUDENT");

        when(userService.registerUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Santhosh"))
                .andExpect(jsonPath("$.email").value("santhosh@gmail.com"))
                .andExpect(jsonPath("$.financialProfile").value("STUDENT"));
    }

    @Test
    void loginUser_ShouldReturnLoginResponse() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("santhosh@gmail.com");
        request.setPassword("Password@123");

        LoginResponse response = new LoginResponse();
        response.setToken("jwt-test-token");
        response.setMessage("Login successful");

        when(userService.loginUser(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-test-token"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

}