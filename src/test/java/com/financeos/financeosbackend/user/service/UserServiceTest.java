package com.financeos.financeosbackend.user.service;

import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import com.financeos.financeosbackend.security.JwtService;
import com.financeos.financeosbackend.user.dto.LoginRequest;
import com.financeos.financeosbackend.user.dto.LoginResponse;
import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import com.financeos.financeosbackend.user.dto.UserResponse;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private FinancialProfileRepository financialProfileRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldRegisterSuccessfully() {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Santhosh");
        request.setEmail("santhosh@gmail.com");
        request.setPassword("password123");


        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("Santhosh");
        savedUser.setEmail("santhosh@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setFinancialProfile("STUDENT");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Santhosh", response.getFullName());
        assertEquals("santhosh@gmail.com", response.getEmail());


        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setFullName("Santhosh");
        request.setEmail("santhosh@gmail.com");
        request.setPassword("password123");


        User existingUser = new User();
        existingUser.setEmail("santhosh@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existingUser));

        ResourceAlreadyExistsException exception =
                assertThrows(ResourceAlreadyExistsException.class,
                        () -> userService.registerUser(request));

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void loginUser_ShouldLoginSuccessfully() {

        LoginRequest request = new LoginRequest();
        request.setEmail("santhosh@gmail.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("santhosh@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
                .thenReturn(true);

        when(jwtService.generateToken(user.getEmail()))
                .thenReturn("jwt-token");

        LoginResponse response =
                userService.loginUser(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Login Successful", response.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder)
                .matches(request.getPassword(), user.getPassword());
        verify(jwtService).generateToken(user.getEmail());
    }

    @Test
    void loginUser_ShouldThrowException_WhenEmailNotFound() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> userService.loginUser(request));

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void loginUser_ShouldThrowException_WhenPasswordIsIncorrect() {

        LoginRequest request = new LoginRequest();
        request.setEmail("santhosh@gmail.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("santhosh@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> userService.loginUser(request));

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder)
                .matches(request.getPassword(), user.getPassword());
        verify(jwtService, never()).generateToken(anyString());
    }


}