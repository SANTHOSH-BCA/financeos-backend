package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.FinancialReport;
import com.financeos.financeosbackend.reporting.entity.ReportExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportExpenseCategoryRepository
        extends JpaRepository<ReportExpenseCategory, Long> {

    List<ReportExpenseCategory> findByReport(
            FinancialReport report
    );
}