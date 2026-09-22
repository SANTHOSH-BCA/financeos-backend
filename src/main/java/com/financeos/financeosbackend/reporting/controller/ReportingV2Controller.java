package com.financeos.financeosbackend.reporting.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodRequest;
import com.financeos.financeosbackend.reporting.dto.ReportPeriodResponse;
import com.financeos.financeosbackend.reporting.dto.v2.FinancialReportV2Response;
import com.financeos.financeosbackend.reporting.service.ReportPeriodService;
import com.financeos.financeosbackend.reporting.service.ReportSnapshotRetrievalService;
import com.financeos.financeosbackend.reporting.service.ReportingV2Service;
import com.financeos.financeosbackend.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/reports")
public class ReportingV2Controller {

    private final CurrentUserService currentUserService;
    private final ReportPeriodService reportPeriodService;
    private final ReportingV2Service reportingV2Service;
    private final ReportSnapshotRetrievalService reportSnapshotRetrievalService;

    public ReportingV2Controller(
            CurrentUserService currentUserService,
            ReportPeriodService reportPeriodService,
            ReportingV2Service reportingV2Service,
            ReportSnapshotRetrievalService reportSnapshotRetrievalService
    ) {
        this.currentUserService = currentUserService;
        this.reportPeriodService = reportPeriodService;
        this.reportingV2Service = reportingV2Service;
        this.reportSnapshotRetrievalService =
                reportSnapshotRetrievalService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FinancialReportV2Response>>
    generateReport(
            @Valid @RequestBody ReportPeriodRequest request
    ) {
        ReportPeriodResponse period =
                reportPeriodService.resolvePeriod(request);

        FinancialReportV2Response response =
                reportingV2Service.generateReport(period);

        return ResponseBuilder.success(
                "Financial report generated successfully",
                response
        );
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ApiResponse<FinancialReportV2Response>>
    getReport(
            @PathVariable Long reportId
    ) {
        User user =
                currentUserService.getCurrentUser();

        FinancialReportV2Response response =
                reportSnapshotRetrievalService.getReport(
                        user,
                        reportId
                );

        return ResponseBuilder.success(
                "Financial report retrieved successfully",
                response
        );
    }
}