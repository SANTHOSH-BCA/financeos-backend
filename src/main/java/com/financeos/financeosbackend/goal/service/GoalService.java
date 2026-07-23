package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import com.financeos.financeosbackend.common.service.CurrentUserService;



@Service
public class GoalService {

    private static final Logger logger =
            LoggerFactory.getLogger(GoalService.class);

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public GoalService(GoalRepository goalRepository,
                       UserRepository userRepository,
                       CurrentUserService currentUserService) {

        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }
    public GoalResponse addGoal(AddGoalRequest request) {

        if (request.getTargetDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Target date cannot be in the past");
        }

        User user = currentUserService.getCurrentUser();

        Goal goal = new Goal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());
        goal.setUser(user);

        logger.info("Creating goal '{}' for user: {}", request.getGoalName(), user.getEmail());

        Goal savedGoal = goalRepository.save(goal);

        logger.info("Goal created successfully with ID: {}", savedGoal.getId());

        return mapToResponse(savedGoal);
    }

    public Page<GoalResponse> getMyGoals(Pageable pageable) {

        User user = currentUserService.getCurrentUser();

        return goalRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    public GoalResponse updateGoal(Long id, AddGoalRequest request) {

        User user = currentUserService.getCurrentUser();

        logger.info("Updating goal with ID: {} for user: {}", id, user.getEmail());

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setCurrentAmount(request.getCurrentAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setGoalStatus(request.getGoalStatus());

        Goal updatedGoal = goalRepository.save(goal);

        logger.info("Goal updated successfully with ID: {}", updatedGoal.getId());

        return mapToResponse(updatedGoal);
    }

    public void deleteGoal(Long id) {

        User user = currentUserService.getCurrentUser();

        logger.info("Deleting goal with ID: {} for user: {}", id, user.getEmail());

        Goal goal = goalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        goalRepository.delete(goal);

        logger.info("Goal deleted successfully with ID: {}", id);
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