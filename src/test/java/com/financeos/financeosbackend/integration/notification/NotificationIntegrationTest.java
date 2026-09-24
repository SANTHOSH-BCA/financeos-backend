package com.financeos.financeosbackend.integration.notification;

import com.financeos.financeosbackend.expense.dto.AddExpenseRequest;
import com.financeos.financeosbackend.integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.JsonNode;
class NotificationIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should create notification when expense is created")
    void shouldCreateNotificationWhenExpenseIsCreated()
            throws Exception {

        // 1. Create authenticated user
        String email = createUniqueEmail();

        registerUser(email);

        String token = loginAndGetToken(email);

        // 2. Create expense through real API
        AddExpenseRequest request = createExpenseRequest();

        mockMvc.perform(
                        post("/api/v1/expenses")
                                .header(
                                        "Authorization",
                                        bearer(token)
                                )
                                .contentType(APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                );

        // 3. Read notifications through authenticated user's scope
        mockMvc.perform(
                        get("/api/notifications")
                                .header(
                                        "Authorization",
                                        bearer(token)
                                )
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Notifications retrieved successfully"
                                )
                )
                .andExpect(
                        jsonPath("$.data.totalElements")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.data.notifications.length()")
                                .value(1)
                )
                .andExpect(
                        jsonPath(
                                "$.data.notifications[0].status"
                        )
                                .value("UNREAD")
                )
                .andExpect(
                        jsonPath(
                                "$.data.notifications[0].relatedEntityType"
                        )
                                .value("EXPENSE")
                )
                .andExpect(
                        jsonPath(
                                "$.data.notifications[0].relatedEntityId"
                        )
                                .isNumber()
                );
    }

    @Test
    @DisplayName(
            "Should isolate notifications between authenticated users"
    )
    void shouldIsolateNotificationsBetweenAuthenticatedUsers()
            throws Exception {

        // ---------------------------------------------------------
        // User A
        // ---------------------------------------------------------

        String emailA = createUniqueEmail();

        registerUser(emailA);

        String tokenA = loginAndGetToken(emailA);

        // Create notification for User A
        AddExpenseRequest expenseRequestA =
                createExpenseRequest();

        mockMvc.perform(
                        post("/api/v1/expenses")
                                .header(
                                        "Authorization",
                                        bearer(tokenA)
                                )
                                .contentType(APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                expenseRequestA
                                        )
                                )
                )
                .andExpect(status().isCreated());

        // ---------------------------------------------------------
        // User B
        // ---------------------------------------------------------

        String emailB = createUniqueEmail();

        registerUser(emailB);

        String tokenB = loginAndGetToken(emailB);

        // ---------------------------------------------------------
        // User B should NOT see User A's notification
        // ---------------------------------------------------------

        mockMvc.perform(
                        get("/api/notifications")
                                .header(
                                        "Authorization",
                                        bearer(tokenB)
                                )
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$.data.totalElements")
                                .value(0)
                )
                .andExpect(
                        jsonPath("$.data.notifications.length()")
                                .value(0)
                );
    }

    @Test
    @DisplayName(
            "Should prevent another user from modifying a notification"
    )
    void shouldPreventAnotherUserFromModifyingNotification()
            throws Exception {

        // ---------------------------------------------------------
        // User A
        // ---------------------------------------------------------

        String emailA = createUniqueEmail();

        registerUser(emailA);

        String tokenA = loginAndGetToken(emailA);

        // Create notification for User A
        AddExpenseRequest expenseRequest =
                createExpenseRequest();

        mockMvc.perform(
                        post("/api/v1/expenses")
                                .header(
                                        "Authorization",
                                        bearer(tokenA)
                                )
                                .contentType(APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                expenseRequest
                                        )
                                )
                )
                .andExpect(status().isCreated());

        // Get User A's notification ID
        String notificationId =
                mockMvc.perform(
                                get("/api/notifications")
                                        .header(
                                                "Authorization",
                                                bearer(tokenA)
                                        )
                                        .param("page", "0")
                                        .param("size", "20")
                        )
                        .andExpect(status().isOk())
                        .andExpect(
                                jsonPath(
                                        "$.data.totalElements"
                                ).value(1)
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode notificationJson =
                objectMapper.readTree(notificationId);

        long userANotificationId =
                notificationJson
                        .path("data")
                        .path("notifications")
                        .get(0)
                        .path("id")
                        .asLong();

        // ---------------------------------------------------------
        // User B
        // ---------------------------------------------------------

        String emailB = createUniqueEmail();

        registerUser(emailB);

        String tokenB = loginAndGetToken(emailB);

        // ---------------------------------------------------------
        // User B → READ User A's notification
        // ---------------------------------------------------------

        mockMvc.perform(
                        patch(
                                "/api/notifications/"
                                        + userANotificationId
                                        + "/read"
                        )
                                .header(
                                        "Authorization",
                                        bearer(tokenB)
                                )
                )
                .andExpect(status().isNotFound());

        // ---------------------------------------------------------
        // User B → ACTION User A's notification
        // ---------------------------------------------------------

        mockMvc.perform(
                        patch(
                                "/api/notifications/"
                                        + userANotificationId
                                        + "/action"
                        )
                                .header(
                                        "Authorization",
                                        bearer(tokenB)
                                )
                )
                .andExpect(status().isNotFound());

        // ---------------------------------------------------------
        // User B → DISMISS User A's notification
        // ---------------------------------------------------------

        mockMvc.perform(
                        patch(
                                "/api/notifications/"
                                        + userANotificationId
                                        + "/dismiss"
                        )
                                .header(
                                        "Authorization",
                                        bearer(tokenB)
                                )
                )
                .andExpect(status().isNotFound());

        // ---------------------------------------------------------
        // Verify User A's notification is still UNREAD
        // ---------------------------------------------------------

        mockMvc.perform(
                        get("/api/notifications")
                                .header(
                                        "Authorization",
                                        bearer(tokenA)
                                )
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath(
                                "$.data.notifications[0].status"
                        )
                                .value("UNREAD")
                );
    }
}