package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.expense.dto.MonthlyExpenseResponse;
import com.financeos.financeosbackend.expense.service.ExpenseService;
import com.financeos.financeosbackend.goal.dto.GoalIntelligenceResponse;
import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.enums.GoalStatus;
import com.financeos.financeosbackend.goal.repository.GoalRepository;
import com.financeos.financeosbackend.income.dto.MonthlyIncomeResponse;
import com.financeos.financeosbackend.income.service.IncomeService;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalIntelligenceServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private IncomeService incomeService;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private GoalIntelligenceService goalIntelligenceService;

    @Test
    void getGoalIntelligence_ShouldCalculateFinancialCapacity() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        Goal goal = new Goal();
        goal.setGoalName("Buy Laptop");
        goal.setTargetAmount(new BigDecimal("80000"));
        goal.setCurrentAmount(new BigDecimal("10000"));
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setGoalStatus(GoalStatus.ON_TRACK);
        goal.setUser(user);

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of(goal));

        when(incomeService.getMonthlyIncomeHistory())
                .thenReturn(List.of(
                        new MonthlyIncomeResponse(
                                "2026-09",
                                new BigDecimal("55000")
                        )
                ));

        when(expenseService.getMonthlyExpenseHistory())
                .thenReturn(List.of(
                        new MonthlyExpenseResponse(
                                "2026-09",
                                new BigDecimal("7800")
                        )
                ));

        GoalIntelligenceResponse response =
                goalIntelligenceService.getGoalIntelligence();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("55000"),
                response.getMonthlyIncome()
        );

        assertEquals(
                new BigDecimal("7800"),
                response.getMonthlyExpenses()
        );

        assertEquals(
                new BigDecimal("47200"),
                response.getFinancialCapacity()
        );

        assertEquals(
                1L,
                response.getFinanciallyAchievableGoals()
        );

        verify(currentUserService).getCurrentUser();
        verify(goalRepository).findByUser(user);
        verify(incomeService).getMonthlyIncomeHistory();
        verify(expenseService).getMonthlyExpenseHistory();
    }

    @Test
    void getGoalIntelligence_ShouldHandleNoGoals() {

        User user = new User();
        user.setEmail("santhosh@gmail.com");

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(goalRepository.findByUser(user))
                .thenReturn(List.of());

        when(incomeService.getMonthlyIncomeHistory())
                .thenReturn(List.of(
                        new MonthlyIncomeResponse(
                                "2026-09",
                                new BigDecimal("55000")
                        )
                ));

        when(expenseService.getMonthlyExpenseHistory())
                .thenReturn(List.of(
                        new MonthlyExpenseResponse(
                                "2026-09",
                                new BigDecimal("7800")
                        )
                ));

        GoalIntelligenceResponse response =
                goalIntelligenceService.getGoalIntelligence();

        assertNotNull(response);
        assertEquals(0L, response.getTotalGoals());
        assertEquals(
                new BigDecimal("47200"),
                response.getFinancialCapacity()
        );
        assertEquals(
                0L,
                response.getFinanciallyAchievableGoals()
        );
    }
}