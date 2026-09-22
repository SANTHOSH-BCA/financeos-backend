package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportGoalSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportGoalSnapshotRepository
        extends JpaRepository<ReportGoalSnapshot, Long> {

    List<ReportGoalSnapshot> findByReport(
            FinancialReport report
    );
}