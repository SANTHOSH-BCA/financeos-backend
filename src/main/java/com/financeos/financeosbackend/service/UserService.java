package com.financeos.financeosbackend.service;

import com.financeos.financeosbackend.dto.RegisterUserRequest;
import com.financeos.financeosbackend.entity.User;
import com.financeos.financeosbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.financeos.financeosbackend.dto.UserResponse;
import com.financeos.financeosbackend.dto.LoginRequest;
import com.financeos.financeosbackend.dto.LoginResponse;
import com.financeos.financeosbackend.service.JwtService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public UserResponse registerUser(RegisterUserRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFinancialProfile(request.getFinancialProfile());

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setFinancialProfile(savedUser.getFinancialProfile());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());

        return response;
    }

    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Login Successful"
        );
    }
}