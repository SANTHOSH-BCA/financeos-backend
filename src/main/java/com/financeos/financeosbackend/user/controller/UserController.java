package com.financeos.financeosbackend.user.controller;

import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import com.financeos.financeosbackend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.financeos.financeosbackend.user.dto.UserResponse;
import com.financeos.financeosbackend.user.dto.LoginRequest;
import com.financeos.financeosbackend.user.dto.LoginResponse;


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