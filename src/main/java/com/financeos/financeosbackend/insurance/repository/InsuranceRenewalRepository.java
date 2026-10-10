package com.financeos.financeosbackend.insurance.repository;

import com.financeos.financeosbackend.insurance.entity.InsuranceRenewal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRenewalRepository
        extends JpaRepository<InsuranceRenewal, Long> {

    List<InsuranceRenewal>
    findAllByInsurancePolicy_IdAndInsurancePolicy_User_IdOrderByRenewedAtDesc(
            Long policyId,
            Long userId
    );

    boolean existsByInsurancePolicy_Id(Long policyId);
}
