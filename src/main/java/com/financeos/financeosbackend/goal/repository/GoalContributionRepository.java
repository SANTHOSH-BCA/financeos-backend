package com.financeos.financeosbackend.goal.repository;

import com.financeos.financeosbackend.goal.entity.Goal;
import com.financeos.financeosbackend.goal.entity.GoalContribution;
import com.financeos.financeosbackend.transaction.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalContributionRepository
        extends JpaRepository<GoalContribution, Long> {

    List<GoalContribution> findByGoalOrderByContributionDateDesc(Goal goal);

    Optional<GoalContribution> findByTransaction(
            FinancialTransaction transaction
    );
}