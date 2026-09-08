package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import com.financeos.financeosbackend.financialprofile.entity.ProtectionContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProtectionContextRepository
        extends JpaRepository<ProtectionContext, Long> {

    Optional<ProtectionContext>
    findByFinancialProfile(FinancialProfile financialProfile);
}