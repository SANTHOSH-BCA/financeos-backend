package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.liability.dto.DebtPaymentCalculationResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DebtPaymentCalculationService {

    private final LiabilityRepository liabilityRepository;
    private final CurrentUserService currentUserService;
    private final DebtCalculationService debtCalculationService;

    public DebtPaymentCalculationService(
            LiabilityRepository liabilityRepository,
            CurrentUserService currentUserService,
            DebtCalculationService debtCalculationService
    ) {
        this.liabilityRepository = liabilityRepository;
        this.currentUserService = currentUserService;
        this.debtCalculationService = debtCalculationService;
    }

    @Transactional(readOnly = true)
    public DebtPaymentCalculationResponse calculate(Long liabilityId) {

        User user = currentUserService.getCurrentUser();

        Liability liability =
                liabilityRepository.findByIdAndUser(
                        liabilityId,
                        user
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Liability not found"
                        ));

        DebtPaymentCalculationResponse response =
                new DebtPaymentCalculationResponse();

        response.setLiabilityId(liability.getId());
        response.setOutstandingAmount(
                debtCalculationService.calculateRemainingAmount(liability)
        );
        response.setPaymentAmount(liability.getPaymentAmount());
        response.setMonthlyPayment(
                debtCalculationService.calculateMonthlyPayment(liability)
        );
        response.setTotalPrincipalPaid(
                debtCalculationService.calculateTotalPrincipalPaid(liability)
        );
        response.setTotalInterestPaid(
                debtCalculationService.calculateTotalInterestPaid(liability)
        );
        response.setTotalPaid(
                debtCalculationService.calculateTotalPaid(liability)
        );
        response.setRemainingTenureMonths(
                debtCalculationService
                        .calculateRemainingTenureMonths(liability)
        );
        response.setNextPaymentDate(
                debtCalculationService.calculateNextPaymentDate(
                        liability,
                        LocalDate.now()
                )
        );

        return response;
    }
}