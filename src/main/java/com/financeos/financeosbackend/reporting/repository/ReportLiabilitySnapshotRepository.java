package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportLiabilitySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportLiabilitySnapshotRepository
        extends JpaRepository<ReportLiabilitySnapshot, Long> {

    List<ReportLiabilitySnapshot> findByReport(
            FinancialReport report
    );
}