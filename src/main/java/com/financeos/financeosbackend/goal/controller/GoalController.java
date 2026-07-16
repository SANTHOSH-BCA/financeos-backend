package com.financeos.financeosbackend.goal.controller;

import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

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
    public GoalResponse addGoal(@Valid @RequestBody AddGoalRequest request) {
        return goalService.addGoal(request);
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
    public void deleteGoal(@PathVariable Long id) {

        goalService.deleteGoal(id);

    }
}