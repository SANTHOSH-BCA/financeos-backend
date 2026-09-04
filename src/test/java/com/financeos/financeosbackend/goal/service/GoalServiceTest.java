package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.dto.GoalResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.user.entity.User;
import com.financeos.financeosbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private GoalService goalService;

    @Test
    void addGoal_ShouldAddGoalSuccessfully() {

        AddGoalRequest request = new AddGoalRequest();
        request.setGoalName("Buy Bike");
        request.setTargetAmount(new BigDecimal("100000"));
        request.setCurrentAmount(new BigDecimal("25000"));
        request.setTargetDate(LocalDate.now().plusMonths(6));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        User user = new User();
        user.setId(1L);
        user.setEmail("santhosh@gmail.com");

        Goal savedGoal = new Goal();
        savedGoal.setGoalName("Buy Bike");
        savedGoal.setTargetAmount(new BigDecimal("100000"));
        savedGoal.setCurrentAmount(new BigDecimal("25000"));
        savedGoal.setTargetDate(LocalDate.now().plusMonths(6));
        savedGoal.setGoalStatus(GoalStatus.ON_TRACK);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(savedGoal);

        GoalResponse response =
                goalService.addGoal(request);

        assertNotNull(response);
        assertEquals("Buy Bike", response.getGoalName());
        assertEquals(new BigDecimal("100000"), response.getTargetAmount());
        assertEquals(new BigDecimal("25000"), response.getCurrentAmount());
        assertEquals(GoalStatus.ON_TRACK, response.getGoalStatus());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void addGoal_ShouldThrowException_WhenTargetDateIsPast() {

        AddGoalRequest request = new AddGoalRequest();
        request.setGoalName("Buy Bike");
        request.setTargetAmount(new BigDecimal("100000"));
        request.setCurrentAmount(new BigDecimal("25000"));
        request.setTargetDate(LocalDate.now().minusDays(1));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> goalService.addGoal(request));

        assertEquals(
                "Target date cannot be in the past",
                exception.getMessage());

        verify(goalRepository, never()).save(any());
    }

    @Test
    void getMyGoals_ShouldReturnGoals() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Buy Bike");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setGoalStatus(GoalStatus.ON_TRACK);

        Page<Goal> page =
                new PageImpl<>(List.of(goal));

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(
                eq(user),
                any(PageRequest.class)))
                .thenReturn(page);

        Page<GoalResponse> response =
                goalService.getMyGoals(PageRequest.of(0, 5));

        assertEquals(1, response.getTotalElements());

        GoalResponse first =
                response.getContent().get(0);

        assertEquals("Buy Bike", first.getGoalName());
        assertEquals(new BigDecimal("100000"), first.getTargetAmount());
        assertEquals(new BigDecimal("25000"), first.getCurrentAmount());
        assertEquals(GoalStatus.ON_TRACK, first.getGoalStatus());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository)
                .findByUser(eq(user), any(PageRequest.class));
    }

    @Test
    void updateGoal_ShouldUpdateSuccessfully() {

        AddGoalRequest request = new AddGoalRequest();
        request.setGoalName("Buy Car");
        request.setTargetAmount(new BigDecimal("800000"));
        request.setCurrentAmount(new BigDecimal("100000"));
        request.setTargetDate(LocalDate.now().plusYears(1));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Buy Bike");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setGoalStatus(GoalStatus.ON_TRACK);
        goal.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(goal));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(goal);

        GoalResponse response =
                goalService.updateGoal(1L, request);

        assertNotNull(response);
        assertEquals("Buy Car", response.getGoalName());
        assertEquals(new BigDecimal("800000"), response.getTargetAmount());
        assertEquals(new BigDecimal("100000"), response.getCurrentAmount());
        assertEquals(
                LocalDate.now().plusYears(1),
                response.getTargetDate());
        assertEquals(GoalStatus.ON_TRACK, response.getGoalStatus());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void updateGoal_ShouldThrowException_WhenGoalNotFound() {

        AddGoalRequest request = new AddGoalRequest();
        request.setGoalName("Buy Car");
        request.setTargetAmount(new BigDecimal("800000"));
        request.setCurrentAmount(new BigDecimal("100000"));
        request.setTargetDate(LocalDate.now().plusYears(1));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> goalService.updateGoal(1L, request));

        assertEquals("Goal not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
        verify(goalRepository, never()).save(any());
    }

    @Test
    void deleteGoal_ShouldDeleteSuccessfully() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Buy Bike");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setGoalStatus(GoalStatus.ON_TRACK);
        goal.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(goal));

        goalService.deleteGoal(1L);

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
        verify(goalRepository).delete(goal);
    }

    @Test
    void deleteGoal_ShouldThrowException_WhenGoalNotFound() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> goalService.deleteGoal(1L));

        assertEquals("Goal not found", exception.getMessage());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
        verify(goalRepository, never()).delete(any(Goal.class));
    }

    @Test
    void getGoalProgress_ShouldCalculateProgressCorrectly() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Buy Laptop");
        goal.setTargetAmount(new BigDecimal("100000"));
        goal.setCurrentAmount(new BigDecimal("25000"));
        goal.setTargetDate(LocalDate.now().plusMonths(5));
        goal.setGoalStatus(GoalStatus.ON_TRACK);
        goal.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(goal));

        var response = goalService.getGoalProgress(1L);

        assertNotNull(response);
        assertEquals(new BigDecimal("100000"), response.getTargetAmount());
        assertEquals(new BigDecimal("25000"), response.getCurrentAmount());
        assertEquals(new BigDecimal("75000"), response.getRemainingAmount());

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
    }

    @Test
    void getGoalProgress_ShouldThrowException_WhenGoalNotFound() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.getGoalProgress(1L)
        );

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByIdAndUser(1L, user);
    }
}