package com.financeos.financeosbackend.goal.controller;

import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.financeos.financeosbackend.common.dto.ApiResponse;
import com.financeos.financeosbackend.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
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
    public Page<GoalResponse> getMyGoals(Pageable pageable) {

        return goalService.getMyGoals(pageable);

    }

    @PutMapping("/{id}")
    public GoalResponse updateGoal(@PathVariable Long id,
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
}