package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "financial_profile_priorities",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"financial_profile_id", "priority"}
                )
        }
)
public class FinancialPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "financial_profile_id",
            nullable = false
    )
    private FinancialProfile financialProfile;

    @Column(name = "priority", nullable = false, length = 50)
    private String priority;

    @Column(name = "priority_rank", nullable = false)
    private Integer priorityRank;

    public FinancialPriority() {
    }

    public FinancialPriority(
            FinancialProfile financialProfile,
            String priority,
            Integer priorityRank
    ) {
        this.financialProfile = financialProfile;
        this.priority = priority;
        this.priorityRank = priorityRank;
    }

    public Long getId() {
        return id;
    }

    public FinancialProfile getFinancialProfile() {
        return financialProfile;
    }

    public void setFinancialProfile(FinancialProfile financialProfile) {
        this.financialProfile = financialProfile;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getPriorityRank() {
        return priorityRank;
    }

    public void setPriorityRank(Integer priorityRank) {
        this.priorityRank = priorityRank;
    }
}