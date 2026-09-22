package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportIncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportIncomeSourceRepository
        extends JpaRepository<ReportIncomeSource, Long> {

    List<ReportIncomeSource> findByReport(
            FinancialReport report
    );
}