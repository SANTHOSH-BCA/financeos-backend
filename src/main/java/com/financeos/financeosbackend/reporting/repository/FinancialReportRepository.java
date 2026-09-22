package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialReportRepository
        extends JpaRepository<FinancialReport, Long> {

    List<FinancialReport> findByUserOrderByGeneratedAtDesc(User user);

    Optional<FinancialReport> findByIdAndUser(
            Long id,
            User user
    );
}