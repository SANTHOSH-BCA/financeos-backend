package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.FinancialSummarySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialSummarySnapshotRepository
        extends JpaRepository<FinancialSummarySnapshot, Long> {

    Optional<FinancialSummarySnapshot> findByReport(
            FinancialReport report
    );
}