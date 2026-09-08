package com.financeos.financeosbackend.financialprofile.repository;

import com.financeos.financeosbackend.financialprofile.entity.EmergencyFundContext;
import com.financeos.financeosbackend.financialprofile.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmergencyFundContextRepository
        extends JpaRepository<EmergencyFundContext, Long> {

    Optional<EmergencyFundContext>
    findByFinancialProfile(FinancialProfile financialProfile);
}