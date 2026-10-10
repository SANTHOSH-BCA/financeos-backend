package com.financeos.financeosbackend.insurance.repository;

import com.financeos.financeosbackend.insurance.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import com.financeos.financeosbackend.insurance.enums.PremiumPaymentStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.financeos.financeosbackend.insurance.enums.InsuranceStatus;
public interface InsurancePolicyRepository
        extends JpaRepository<InsurancePolicy, Long> {

    List<InsurancePolicy> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<InsurancePolicy> findByIdAndUser_Id(Long id, Long userId);

    boolean existsByPolicyNumberAndUser_Id(String policyNumber, Long userId);

    List<InsurancePolicy>
    findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateLessThanEqualOrderByNextPremiumDueDateAsc(
            Long userId,
            PremiumPaymentStatus status,
            LocalDate date
    );

    List<InsurancePolicy>
    findAllByUser_IdAndPremiumPaymentStatusAndNextPremiumDueDateBetweenOrderByNextPremiumDueDateAsc(
            Long userId,
            PremiumPaymentStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    List<InsurancePolicy>
    findAllByUser_IdAndStatusAndExpiryDateBeforeOrderByExpiryDateAsc(
            Long userId,
            com.financeos.financeosbackend.insurance.enums.InsuranceStatus status,
            LocalDate date
    );

    List<InsurancePolicy>
    findAllByUser_IdAndStatusAndExpiryDateBetweenOrderByExpiryDateAsc(
            Long userId,
            com.financeos.financeosbackend.insurance.enums.InsuranceStatus status,
            LocalDate startDate,
            LocalDate endDate
    );
}