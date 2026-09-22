package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportAssetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportAssetAllocationRepository
        extends JpaRepository<ReportAssetAllocation, Long> {

    List<ReportAssetAllocation> findByReport(
            FinancialReport report
    );
}