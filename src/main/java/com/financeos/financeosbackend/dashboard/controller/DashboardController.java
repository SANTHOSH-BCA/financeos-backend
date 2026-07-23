package com.financeos.financeosbackend.dashboard.controller;

import com.financeos.financeosbackend.dashboard.dto.DashboardResponse;
import com.financeos.financeosbackend.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {

        DashboardResponse response = dashboardService.getDashboard();

        return ResponseBuilder.success(
                "Dashboard retrieved successfully",
                response
        );
    }
}