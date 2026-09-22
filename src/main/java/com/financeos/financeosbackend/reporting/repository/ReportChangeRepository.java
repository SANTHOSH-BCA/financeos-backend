package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportChangeRepository
        extends JpaRepository<ReportChange, Long> {

    List<ReportChange> findByReport(FinancialReport report);
}