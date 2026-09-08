package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.FinancialResponsibilityContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialResponsibilityContextRepository
        extends JpaRepository<FinancialResponsibilityContext, Long> {

    Optional<FinancialResponsibilityContext>
    findByFinancialProfile(FinancialProfile financialProfile);
}