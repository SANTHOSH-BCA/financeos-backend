package com.financeos.financeosbackend.notification.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.notification.dto.NotificationPageResponse;
import com.financeos.financeosbackend.notification.enums.NotificationStatus;
import com.financeos.financeosbackend.notification.management.NotificationManagementResult;
import com.financeos.financeosbackend.notification.management.NotificationManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryService queryService;

    private final NotificationManagementService
            managementService;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationPageResponse>>
    getNotifications(
            @RequestParam Long userId,
            @RequestParam(required = false)
            NotificationStatus status,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "20")
            int size
    ) {

        NotificationPageResponse response =
                queryService.getNotifications(
                        userId,
                        status,
                        page,
                        size
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Notifications retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>>
    getUnreadCount(
            @RequestParam Long userId
    ) {

        long count =
                queryService.getUnreadCount(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Unread notification count retrieved successfully",
                        count
                )
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationManagementResult>>
    markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId
    ) {

        NotificationManagementResult result =
                managementService.markAsRead(
                        userId,
                        notificationId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        result.message(),
                        result
                )
        );
    }

    @PatchMapping("/{notificationId}/action")
    public ResponseEntity<ApiResponse<NotificationManagementResult>>
    markAsActioned(
            @PathVariable Long notificationId,
            @RequestParam Long userId
    ) {

        NotificationManagementResult result =
                managementService.markAsActioned(
                        userId,
                        notificationId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        result.message(),
                        result
                )
        );
    }

    @PatchMapping("/{notificationId}/dismiss")
    public ResponseEntity<ApiResponse<NotificationManagementResult>>
    dismiss(
            @PathVariable Long notificationId,
            @RequestParam Long userId
    ) {

        NotificationManagementResult result =
                managementService.dismiss(
                        userId,
                        notificationId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        result.message(),
                        result
                )
        );
    }
}