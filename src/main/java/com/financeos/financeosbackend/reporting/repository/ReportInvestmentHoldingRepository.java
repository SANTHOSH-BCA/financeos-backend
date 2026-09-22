package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportInvestmentHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportInvestmentHoldingRepository
        extends JpaRepository<ReportInvestmentHolding, Long> {

    List<ReportInvestmentHolding> findByReport(
            FinancialReport report
    );
}