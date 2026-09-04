package com.financeos.financeosbackend.investment.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.investment.dto.InvestmentDataQualityResponse;
import com.financeos.financeosbackend.investment.entity.Investment;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentDataQualityService {

    private final InvestmentRepository investmentRepository;
    private final CurrentUserService currentUserService;

    public InvestmentDataQualityService(
            InvestmentRepository investmentRepository,
            CurrentUserService currentUserService) {

        this.investmentRepository = investmentRepository;
        this.currentUserService = currentUserService;
    }

    public InvestmentDataQualityResponse validateInvestments() {

        User user = currentUserService.getCurrentUser();

        List<Investment> investments =
                investmentRepository.findByUser(
                        user,
                        org.springframework.data.domain.Pageable.unpaged()
                ).getContent();

        List<String> warnings = new ArrayList<>();

        for (Investment investment : investments) {

            if (investment.getAmount() == null
                    || investment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                warnings.add(
                        "Investment " + investment.getId()
                                + " has an invalid invested amount"
                );
            }

            if (investment.getCurrentValue() == null) {
                warnings.add(
                        "Investment " + investment.getId()
                                + " has no current valuation"
                );
            }

            if (investment.getValuationDate() == null) {
                warnings.add(
                        "Investment " + investment.getId()
                                + " has no valuation date"
                );
            } else if (investment.getValuationDate()
                    .isBefore(LocalDate.now().minusDays(30))) {

                warnings.add(
                        "Investment " + investment.getId()
                                + " has a stale valuation"
                );
            }
        }

        return new InvestmentDataQualityResponse(
                warnings.isEmpty(),
                warnings
        );
    }
}