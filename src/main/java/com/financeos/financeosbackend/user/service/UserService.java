package com.financeos.financeosbackend.user.service;
import com.financeos.financeosbackend.exception.InvalidCredentialsException;
import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import com.financeos.financeosbackend.security.JwtService;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financeos.financeosbackend.exception.ResourceAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.financeos.financeosbackend.user.dto.UserResponse;
import com.financeos.financeosbackend.user.dto.LoginRequest;
import com.financeos.financeosbackend.user.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.repository.FinancialProfileRepository;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private FinancialProfileRepository financialProfileRepository;



    public UserResponse registerUser(RegisterUserRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFinancialProfile("V2");


        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {}", savedUser.getEmail());

        UserResponse response = new UserResponse();

        FinancialProfile profile = new FinancialProfile();
        profile.setUser(savedUser);

        financialProfileRepository.save(profile);

        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());

        return response;
    }

    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Login failed. Email not found: {}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed. Invalid password for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        logger.info("User logged in successfully: {}", user.getEmail());

        return new LoginResponse(
                token,
                "Login Successful"
        );
    }
}