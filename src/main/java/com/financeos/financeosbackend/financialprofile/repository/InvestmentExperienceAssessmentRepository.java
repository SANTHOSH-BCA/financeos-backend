package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.InvestmentExperienceAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestmentExperienceAssessmentRepository
        extends JpaRepository<InvestmentExperienceAssessment, Long> {

    Optional<InvestmentExperienceAssessment>
    findByFinancialProfile(FinancialProfile financialProfile);
}