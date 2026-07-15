package com.financeos.financeosbackend.controller;

import com.financeos.financeosbackend.dto.RegisterUserRequest;
import com.financeos.financeosbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.financeos.financeosbackend.dto.UserResponse;
import com.financeos.financeosbackend.dto.LoginRequest;
import com.financeos.financeosbackend.dto.LoginResponse;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test() {
        return "FinanceOS User Module Working!";
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterUserRequest request) {

        return userService.registerUser(request);

    }

    @PostMapping("/login")
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {

        return userService.loginUser(request);

    }
}