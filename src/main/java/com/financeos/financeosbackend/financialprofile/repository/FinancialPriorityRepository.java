package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.FinancialPriority;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialPriorityRepository
        extends JpaRepository<FinancialPriority, Long> {

    List<FinancialPriority> findByFinancialProfileOrderByPriorityRankAsc(
            FinancialProfile financialProfile
    );
}