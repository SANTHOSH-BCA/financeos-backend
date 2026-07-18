package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalService(GoalRepository goalRepository,
                       UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public GoalResponse addGoal(AddGoalRequest request) {

        if (request.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Target date cannot be in the past");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = new Goal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());
        goal.setUser(user);

        Goal savedGoal = goalRepository.save(goal);

        return mapToResponse(savedGoal);
    }

    public Page<GoalResponse> getMyGoals(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return goalRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public GoalResponse updateGoal(Long id, AddGoalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());

        Goal updatedGoal = goalRepository.save(goal);

        return mapToResponse(updatedGoal);
    }

    public void deleteGoal(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goalRepository.delete(goal);
    }

    private GoalResponse mapToResponse(Goal goal) {

        GoalResponse response = new GoalResponse();

        response.setId(goal.getId());
        response.setGoalName(goal.getGoalName());
        response.setTargetAmount(goal.getTargetAmount());
        response.setCurrentAmount(goal.getCurrentAmount());
        response.setTargetDate(goal.getTargetDate());
        response.setGoalStatus(goal.getGoalStatus());

        return response;
    }
}