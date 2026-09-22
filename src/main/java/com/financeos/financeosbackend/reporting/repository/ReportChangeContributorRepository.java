package com.financeos.financeosbackend.reporting.repository;

import com.financeos.financeosbackend.reporting.entity.ReportChange;
import com.financeos.financeosbackend.reporting.entity.ReportChangeContributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportChangeContributorRepository
        extends JpaRepository<ReportChangeContributor, Long> {

    List<ReportChangeContributor> findByReportChange(
            ReportChange reportChange
    );
}