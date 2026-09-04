package com.financeos.financeosbackend.goal.controller;

import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalContributionResponse;
import com.financeos.financeosbackend.goal.dto.GoalDataQualityResponse;
import com.financeos.financeosbackend.goal.dto.GoalIntelligenceResponse;
import com.financeos.financeosbackend.goal.dto.GoalInsightResponse;
import com.financeos.financeosbackend.goal.dto.GoalPerformanceResponse;
import com.financeos.financeosbackend.goal.dto.GoalProgressResponse;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.service.GoalDataQualityService;
import com.financeos.financeosbackend.goal.service.GoalInsightService;
import com.financeos.financeosbackend.goal.service.GoalIntelligenceService;
import com.financeos.financeosbackend.goal.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;
    private final GoalIntelligenceService goalIntelligenceService;
    private final GoalInsightService goalInsightService;
    private final GoalDataQualityService goalDataQualityService;

    public GoalController(
            GoalService goalService,
            GoalIntelligenceService goalIntelligenceService,
            GoalInsightService goalInsightService,
            GoalDataQualityService goalDataQualityService) {

        this.goalService = goalService;
        this.goalIntelligenceService = goalIntelligenceService;
        this.goalInsightService = goalInsightService;
        this.goalDataQualityService = goalDataQualityService;
    }

    @GetMapping("/test")
    public String test() {
        return "Goal Controller Working";
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GoalResponse>> addGoal(
            @Valid @RequestBody AddGoalRequest request) {

        GoalResponse response = goalService.addGoal(request);

        return ResponseBuilder.created(
                "Goal created successfully",
                response
        );
    }

    @GetMapping
    public Page<GoalResponse> getMyGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return goalService.getMyGoals(pageable);
    }

    @PutMapping("/{id}")
    public GoalResponse updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody AddGoalRequest request) {

        return goalService.updateGoal(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGoal(
            @PathVariable Long id) {

        goalService.deleteGoal(id);

        return ResponseBuilder.success(
                "Goal deleted successfully",
                null
        );
    }

    @GetMapping("/{id}/progress")
    public GoalProgressResponse getGoalProgress(
            @PathVariable Long id) {

        return goalService.getGoalProgress(id);
    }

    @GetMapping("/{id}/contributions")
    public List<GoalContributionResponse> getContributions(
            @PathVariable Long id) {

        return goalService.getContributions(id);
    }

    @PostMapping("/{id}/contributions")
    public GoalContributionResponse addContribution(
            @PathVariable Long id,
            @RequestParam(name = "amount") BigDecimal amount,
            @RequestParam(name = "contributionDate") LocalDate contributionDate) {

        return goalService.addContribution(
                id,
                amount,
                contributionDate
        );
    }

    @GetMapping("/performance")
    public GoalPerformanceResponse getGoalPerformance() {
        return goalService.getGoalPerformance();
    }

    @GetMapping("/intelligence")
    public GoalIntelligenceResponse getGoalIntelligence() {
        return goalIntelligenceService.getGoalIntelligence();
    }

    @GetMapping("/insights")
    public List<GoalInsightResponse> getGoalInsights() {
        return goalInsightService.getGoalInsights();
    }

    @GetMapping("/{id}/data-quality")
    public GoalDataQualityResponse getGoalDataQuality(
            @PathVariable Long id) {

        return goalDataQualityService.validateGoal(id);
    }
}