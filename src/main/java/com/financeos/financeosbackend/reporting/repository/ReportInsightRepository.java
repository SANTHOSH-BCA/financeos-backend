package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportInsightRepository
        extends JpaRepository<ReportInsight, Long> {

    List<ReportInsight> findByReport(FinancialReport report);
}