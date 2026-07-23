package com.financeos.financeosbackend.integration;

import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.integration.helper.GoalTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
class GoalIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create goal successfully")
    void shouldCreateGoalSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        AddGoalRequest request = GoalTestHelper.validGoal();

        mockMvc.perform(post("/api/goals")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.goalName").value("Laptop"))
                .andExpect(jsonPath("$.data.targetAmount").value(100000))
                .andExpect(jsonPath("$.data.currentAmount").value(25000))
                .andExpect(jsonPath("$.data.goalStatus").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should get all goals")
    void shouldGetAllGoals() throws Exception {

        String token = createAuthenticatedUser();

        createGoal(token);

        mockMvc.perform(get("/api/goals")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].goalName").value("Laptop"));
    }

    @Test
    @DisplayName("Should update goal successfully")
    void shouldUpdateGoalSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long goalId = createGoal(token);

        AddGoalRequest request = GoalTestHelper.updatedGoal();

        mockMvc.perform(put("/api/goals/{id}", goalId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalName").value("Car"))
                .andExpect(jsonPath("$.targetAmount").value(500000))
                .andExpect(jsonPath("$.currentAmount").value(100000))
                .andExpect(jsonPath("$.goalStatus").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should delete goal successfully")
    void shouldDeleteGoalSuccessfully() throws Exception {

        String token = createAuthenticatedUser();

        Long goalId = createGoal(token);

        mockMvc.perform(delete("/api/goals/{id}", goalId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should reject invalid goal request")
    void shouldRejectInvalidGoalRequest() throws Exception {

        String token = createAuthenticatedUser();

        AddGoalRequest request = GoalTestHelper.invalidGoal();

        mockMvc.perform(post("/api/goals")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject past target date")
    void shouldRejectPastTargetDate() throws Exception {

        String token = createAuthenticatedUser();

        AddGoalRequest request = GoalTestHelper.validGoal();

        request.setTargetDate(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/goals")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should reject unauthorized request")
    void shouldRejectUnauthorizedRequest() throws Exception {

        AddGoalRequest request = GoalTestHelper.validGoal();

        mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 404 for invalid goal update")
    void shouldReturn404ForInvalidGoalUpdate() throws Exception {

        String token = createAuthenticatedUser();

        AddGoalRequest request = GoalTestHelper.updatedGoal();

        mockMvc.perform(put("/api/goals/{id}", 999999L)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 for invalid goal delete")
    void shouldReturn404ForInvalidGoalDelete() throws Exception {

        String token = createAuthenticatedUser();

        mockMvc.perform(delete("/api/goals/{id}", 999999L)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paginated goals")
    void shouldReturnPaginatedGoals() throws Exception {

        String token = createAuthenticatedUser();

        createGoal(token);

        mockMvc.perform(get("/api/goals")
                        .header("Authorization", bearer(token))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("Should return only current user's goals")
    void shouldReturnOnlyCurrentUsersGoals() throws Exception {

        String userOneToken = createAuthenticatedUser();
        createGoal(userOneToken);

        String userTwoToken = createAuthenticatedUser();

        mockMvc.perform(get("/api/goals")
                        .header("Authorization", bearer(userTwoToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

}