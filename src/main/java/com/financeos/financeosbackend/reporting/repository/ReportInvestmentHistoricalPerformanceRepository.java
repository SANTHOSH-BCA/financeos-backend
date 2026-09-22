package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentHistoricalPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportInvestmentHistoricalPerformanceRepository
        extends JpaRepository<ReportInvestmentHistoricalPerformance, Long> {

    List<ReportInvestmentHistoricalPerformance> findByReport(
            FinancialReport report
    );
}