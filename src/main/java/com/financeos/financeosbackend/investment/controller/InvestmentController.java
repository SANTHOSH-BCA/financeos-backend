package com.financeos.financeosbackend.investment.controller;

import com.financeos.financeosbackend.investment.dto.AddInvestmentRequest;
import com.financeos.financeosbackend.investment.dto.InvestmentResponse;
import com.financeos.financeosbackend.investment.service.InvestmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.common.dto.PagedResponse;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;import com.financeos.financeosbackend.investment.dto.InvestmentPerformanceResponse;import com.financeos.financeosbackend.investment.dto.InvestmentHoldingPerformanceResponse;import com.financeos.financeosbackend.investment.dto.InvestmentAllocationResponse;import com.financeos.financeosbackend.investment.dto.InvestmentValuationHistoryResponse;import com.financeos.financeosbackend.investment.dto.InvestmentIntelligenceResponse;
import com.financeos.financeosbackend.investment.service.InvestmentIntelligenceService;import com.financeos.financeosbackend.investment.dto.InvestmentInsightResponse;
import com.financeos.financeosbackend.investment.service.InvestmentInsightService;import com.financeos.financeosbackend.investment.dto.InvestmentDataQualityResponse;
import com.financeos.financeosbackend.investment.service.InvestmentDataQualityService;import org.springdoc.core.annotations.ParameterObject;import org.springframework.data.domain.PageRequest;import com.financeos.financeosbackend.investment.dto.InvestmentExposureResponse;
@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final InvestmentIntelligenceService investmentIntelligenceService;
    private final InvestmentInsightService investmentInsightService;
    private final InvestmentDataQualityService investmentDataQualityService;

    public InvestmentController(InvestmentService investmentService,
                                InvestmentIntelligenceService investmentIntelligenceService,
                                InvestmentInsightService investmentInsightService,
                                InvestmentDataQualityService investmentDataQualityService) {

        this.investmentService = investmentService;
        this.investmentIntelligenceService = investmentIntelligenceService;
        this.investmentInsightService = investmentInsightService;
        this.investmentDataQualityService = investmentDataQualityService;
    }

    @GetMapping("/test")
    public String test() {
        return "Investment Controller Working";
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvestmentResponse>> addInvestment(
            @Valid @RequestBody AddInvestmentRequest request) {

        System.out.println(">>> NEW InvestmentController is running <<<");

        InvestmentResponse response = investmentService.addInvestment(request);

        return ResponseBuilder.created(
                "Investment created successfully",
                response
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<InvestmentResponse>> getMyInvestments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<InvestmentResponse> response =
                investmentService.getMyInvestments(pageable);

        return ResponseBuilder.paged(
                "Investments retrieved successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvestmentResponse>> updateInvestment(
            @PathVariable Long id,
            @Valid @RequestBody AddInvestmentRequest request) {

        InvestmentResponse response =
                investmentService.updateInvestment(id, request);

        return ResponseBuilder.success(
                "Investment updated successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInvestment(
            @PathVariable Long id) {

        investmentService.deleteInvestment(id);

        return ResponseBuilder.success(
                "Investment deleted successfully",
                null
        );
    }

    @GetMapping("/performance")
    public ResponseEntity<ApiResponse<InvestmentPerformanceResponse>> getPortfolioPerformance() {

        InvestmentPerformanceResponse response =
                investmentService.getPortfolioPerformance();

        return ResponseBuilder.success(
                "Investment portfolio performance retrieved successfully",
                response
        );
    }

    @GetMapping("/performance/holdings")
    public ResponseEntity<ApiResponse<List<InvestmentHoldingPerformanceResponse>>>
    getHoldingPerformance() {

        List<InvestmentHoldingPerformanceResponse> response =
                investmentService.getHoldingPerformance();

        return ResponseBuilder.success(
                "Investment holding performance retrieved successfully",
                response
        );
    }

    @GetMapping("/allocation")
    public ResponseEntity<ApiResponse<List<InvestmentAllocationResponse>>>
    getAssetAllocation() {

        List<InvestmentAllocationResponse> response =
                investmentService.getAssetAllocation();

        return ResponseBuilder.success(
                "Investment asset allocation retrieved successfully",
                response
        );
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<InvestmentValuationHistoryResponse>>>
    getInvestmentHistory(@PathVariable Long id) {

        List<InvestmentValuationHistoryResponse> response =
                investmentService.getInvestmentHistory(id);

        return ResponseBuilder.success(
                "Investment history retrieved successfully",
                response
        );
    }

    @GetMapping("/intelligence")
    public ResponseEntity<ApiResponse<InvestmentIntelligenceResponse>>
    getInvestmentIntelligence() {

        InvestmentIntelligenceResponse response =
                investmentIntelligenceService.getInvestmentIntelligence();

        return ResponseBuilder.success(
                "Investment intelligence retrieved successfully",
                response
        );
    }

    @GetMapping("/insights")
    public ResponseEntity<ApiResponse<List<InvestmentInsightResponse>>>
    getInvestmentInsights() {

        List<InvestmentInsightResponse> response =
                investmentInsightService.getInvestmentInsights();

        return ResponseBuilder.success(
                "Investment insights retrieved successfully",
                response
        );
    }

    @GetMapping("/data-quality")
    public ResponseEntity<ApiResponse<InvestmentDataQualityResponse>>
    getInvestmentDataQuality() {

        InvestmentDataQualityResponse response =
                investmentDataQualityService.validateInvestments();

        return ResponseBuilder.success(
                "Investment data quality retrieved successfully",
                response
        );
    }

    @GetMapping("/exposure")
    public ResponseEntity<List<InvestmentExposureResponse>> getInvestmentExposure() {
        return ResponseEntity.ok(
                investmentService.getInvestmentExposure()
        );
    }

}