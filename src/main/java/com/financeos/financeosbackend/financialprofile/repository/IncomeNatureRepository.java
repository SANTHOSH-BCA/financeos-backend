package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.IncomeNature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeNatureRepository
        extends JpaRepository<IncomeNature, Long> {

    List<IncomeNature> findByFinancialProfile(
            FinancialProfile financialProfile
    );
}