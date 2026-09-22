package com.financeos.financeosbackend.liability.repository;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LiabilityRepaymentRepository
        extends JpaRepository<LiabilityRepayment, Long> {

    List<LiabilityRepayment> findAllByLiabilityOrderByRepaymentDateDesc(
            Liability liability
    );

    List<LiabilityRepayment> findAllByLiabilityAndRepaymentDateBetweenOrderByRepaymentDateDesc(
            Liability liability,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<LiabilityRepayment> findByIdAndLiability(
            Long id,
            Liability liability
    );
}