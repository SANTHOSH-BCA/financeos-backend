package com.financeos.financeosbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.expense.repository.ExpenseRepository;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.repository.IncomeRepository;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.dto.LoginRequest;
import com.financeos.financeosbackend.user.dto.LoginResponse;
import com.financeos.financeosbackend.user.dto.RegisterUserRequest;
import com.financeos.financeosbackend.integration.helper.ExpenseTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.expense.dto.ExpenseResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.integration.helper.InvestmentTestHelper;
import java.util.UUID;
import com.financeos.financeosbackend.expense.entity.Expense;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.financeos.financeosbackend.income.dto.AddIncomeRequest;
import com.financeos.financeosbackend.integration.helper.IncomeTestHelper;
import com.financeos.financeosbackend.income.entity.Income;

import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.integration.helper.GoalTestHelper;import com.financeos.financeosbackend.asset.repository.AssetRepository;import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {

    protected static final String DEFAULT_PASSWORD = "Password@123";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ExpenseRepository expenseRepository;

    @Autowired
    protected IncomeRepository incomeRepository;

    @Autowired
    protected GoalRepository goalRepository;

    @Autowired
    protected InvestmentRepository investmentRepository;

    @Autowired
    protected AssetRepository assetRepository;

    @Autowired
    protected LiabilityRepository liabilityRepository;

    /**
     * Creates a unique email for every integration test.
     */
    protected String createUniqueEmail() {
        return "integration-" + UUID.randomUUID() + "@gmail.com";
    }

    /**
     * Returns Authorization header value.
     */
    protected String bearer(String token) {
        return "Bearer " + token;
    }

    /**
     * Creates a registration request.
     */
    protected RegisterUserRequest createUserRequest(String email) {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setFullName("Integration User");
        request.setEmail(email);
        request.setPassword(DEFAULT_PASSWORD);


        return request;
    }

    /**
     * Registers a new user.
     */
    protected void registerUser(String email) throws Exception {

        RegisterUserRequest request = createUserRequest(email);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    /**
     * Logs in and returns JWT token.
     */
    protected String loginAndGetToken(String email) throws Exception {

        LoginRequest loginRequest =
                new LoginRequest(email, DEFAULT_PASSWORD);

        String response =
                mockMvc.perform(post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        LoginResponse loginResponse =
                objectMapper.readValue(response, LoginResponse.class);

        return loginResponse.getToken();
    }

    /**
     * Creates a user and returns JWT.
     */
    protected String createAuthenticatedUser() throws Exception {

        String email = createUniqueEmail();

        registerUser(email);

        return loginAndGetToken(email);
    }

    /**
     * Returns a valid expense request.
     */
    protected AddExpenseRequest createExpenseRequest() {
        return ExpenseTestHelper.validExpense();
    }

    /**
     * Creates an expense and returns its database ID.
     */
    protected Long createExpense(String token) throws Exception {

        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createExpenseRequest())))
                .andExpect(status().isCreated());

        return expenseRepository.findAll()
                .stream()
                .filter(expense -> "Petrol".equals(expense.getTitle()))
                .map(Expense::getId)
                .max(Long::compareTo)
                .orElseThrow();
    }

    protected Long createIncome(String token) throws Exception {

        AddIncomeRequest request = IncomeTestHelper.validIncome();

        mockMvc.perform(post("/api/incomes")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        return incomeRepository.findAll()
                .stream()
                .map(Income::getId)
                .max(Long::compareTo)
                .orElseThrow();
    }

    protected Long createInvestment(String token) throws Exception {

        AddInvestmentRequest request = InvestmentTestHelper.validInvestment();

        mockMvc.perform(post("/api/investments")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        return investmentRepository.findAll()
                .stream()
                .map(Investment::getId)
                .max(Long::compareTo)
                .orElseThrow();
    }

    protected Long createGoal(String token) throws Exception {

        AddGoalRequest request = GoalTestHelper.validGoal();

        mockMvc.perform(post("/api/goals")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        return goalRepository.findAll()
                .stream()
                .map(Goal::getId)
                .max(Long::compareTo)
                .orElseThrow();
    }

}