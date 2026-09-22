package com.financeos.financeosbackend.investment.repository;

import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.entity.InvestmentValuationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvestmentValuationHistoryRepository
        extends JpaRepository<InvestmentValuationHistory, Long> {

    List<InvestmentValuationHistory> findByInvestmentOrderByValuationDateDesc(
            Investment investment
    );

    Optional<InvestmentValuationHistory>
    findTopByInvestmentAndValuationDateLessThanEqualOrderByValuationDateDesc(
            Investment investment,
            LocalDate endDate
    );
}