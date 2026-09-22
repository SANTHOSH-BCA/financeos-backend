package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportComparisonSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportComparisonSnapshotRepository
        extends JpaRepository<ReportComparisonSnapshot, Long> {

    Optional<ReportComparisonSnapshot> findByReport(
            FinancialReport report
    );
}