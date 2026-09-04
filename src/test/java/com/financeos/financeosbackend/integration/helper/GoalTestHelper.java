package com.financeos.financeosbackend.integration.helper;

import com.financeos.financeosbackend.goal.dto.AddGoalRequest;
import com.financeos.financeosbackend.goal.enums.GoalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class GoalTestHelper {

    private GoalTestHelper() {
    }

    public static AddGoalRequest validGoal() {

        AddGoalRequest request = new AddGoalRequest();

        request.setGoalName("Laptop");
        request.setTargetAmount(new BigDecimal("100000"));
        request.setCurrentAmount(new BigDecimal("25000"));
        request.setTargetDate(LocalDate.now().plusMonths(12));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        return request;
    }

    public static AddGoalRequest updatedGoal() {

        AddGoalRequest request = new AddGoalRequest();

        request.setGoalName("Car");
        request.setTargetAmount(new BigDecimal("500000"));
        request.setCurrentAmount(new BigDecimal("100000"));
        request.setTargetDate(LocalDate.now().plusYears(2));
        request.setGoalStatus(GoalStatus.ON_TRACK);

        return request;
    }

    public static AddGoalRequest invalidGoal() {

        AddGoalRequest request = new AddGoalRequest();

        request.setGoalName("");
        request.setTargetAmount(BigDecimal.ZERO);
        request.setCurrentAmount(new BigDecimal("-1"));
        request.setTargetDate(null);
        request.setGoalStatus(null);

        return request;
    }
}