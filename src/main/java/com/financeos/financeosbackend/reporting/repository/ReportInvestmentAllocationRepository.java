package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportInvestmentAllocationRepository
        extends JpaRepository<ReportInvestmentAllocation, Long> {

    List<ReportInvestmentAllocation> findByReport(
            FinancialReport report
    );
}