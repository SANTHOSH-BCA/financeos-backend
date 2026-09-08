package com.financeos.financeosbackend.financialprofile.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "financial_profile_income_natures",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"financial_profile_id", "income_nature"}
                )
        }
)
public class IncomeNature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "financial_profile_id",
            nullable = false
    )
    private FinancialProfile financialProfile;

    @Column(name = "income_nature", nullable = false, length = 50)
    private String incomeNature;

    public IncomeNature() {
    }

    public IncomeNature(
            FinancialProfile financialProfile,
            String incomeNature
    ) {
        this.financialProfile = financialProfile;
        this.incomeNature = incomeNature;
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

    public String getIncomeNature() {
        return incomeNature;
    }

    public void setIncomeNature(String incomeNature) {
        this.incomeNature = incomeNature;
    }
}