package com.financeos.financeosbackend.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GoalContributionResponse {

    private Long id;
    private Long goalId;
    private BigDecimal amount;
    private LocalDate contributionDate;
    private Long transactionId;

    public GoalContributionResponse() {
    }

    public GoalContributionResponse(
            Long id,
            Long goalId,
            BigDecimal amount,
            LocalDate contributionDate,
            Long transactionId) {
        this.id = id;
        this.goalId = goalId;
        this.amount = amount;
        this.contributionDate = contributionDate;
        this.transactionId = transactionId;
    }

    public Long getId() {
        return id;
    }

    public Long getGoalId() {
        return goalId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getContributionDate() {
        return contributionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}