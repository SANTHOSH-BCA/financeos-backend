package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.LiabilityStatus;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DebtBurdenService {

    private final LiabilityRepository liabilityRepository;
    private final LiabilityRepaymentRepository repaymentRepository;
    private final CurrentUserService currentUserService;
    private final DebtCalculationService debtCalculationService;
    private final DebtBurdenAnalysisService debtBurdenAnalysisService;

    public DebtBurdenService(
            LiabilityRepository liabilityRepository,
            LiabilityRepaymentRepository repaymentRepository,
            CurrentUserService currentUserService,
            DebtCalculationService debtCalculationService,
            DebtBurdenAnalysisService debtBurdenAnalysisService
    ) {
        this.liabilityRepository = liabilityRepository;
        this.repaymentRepository = repaymentRepository;
        this.currentUserService = currentUserService;
        this.debtCalculationService = debtCalculationService;
        this.debtBurdenAnalysisService = debtBurdenAnalysisService;
    }

    @Transactional(readOnly = true)
    public DebtBurdenResponse getMyDebtBurden() {

        User user = currentUserService.getCurrentUser();

        List<Liability> liabilities =
                liabilityRepository.findAllByUser(user);

        BigDecimal totalOutstanding = BigDecimal.ZERO;
        BigDecimal totalMonthlyPayment = BigDecimal.ZERO;
        BigDecimal totalPrincipalPaid = BigDecimal.ZERO;
        BigDecimal totalInterestPaid = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;

        int activeLiabilityCount = 0;

        for (Liability liability : liabilities) {

            if (!isActive(liability)) {
                continue;
            }

            BigDecimal recognizedOutstanding =
                    calculateRecognizedAmount(liability);

            totalOutstanding =
                    totalOutstanding.add(recognizedOutstanding);

            totalMonthlyPayment =
                    totalMonthlyPayment.add(
                            calculateRecognizedMonthlyPayment(liability)
                    );

            activeLiabilityCount++;

            List<LiabilityRepayment> repayments =
                    repaymentRepository
                            .findAllByLiabilityOrderByRepaymentDateDesc(
                                    liability
                            );

            totalPrincipalPaid =
                    totalPrincipalPaid.add(
                            sumRecognizedPrincipal(
                                    repayments,
                                    liability
                            )
                    );

            totalInterestPaid =
                    totalInterestPaid.add(
                            sumRecognizedInterest(
                                    repayments,
                                    liability
                            )
                    );

            totalPaid =
                    totalPaid.add(
                            sumRecognizedPayments(
                                    repayments,
                                    liability
                            )
                    );
        }

        DebtBurdenResponse response = new DebtBurdenResponse();

        response.setTotalOutstanding(scale(totalOutstanding));
        response.setTotalMonthlyPayment(scale(totalMonthlyPayment));
        response.setTotalPrincipalPaid(scale(totalPrincipalPaid));
        response.setTotalInterestPaid(scale(totalInterestPaid));
        response.setTotalPaid(scale(totalPaid));
        response.setActiveLiabilityCount(activeLiabilityCount);

        /*
         * Income integration will be connected through the
         * Income module contract. Until that integration is
         * introduced, monthlyIncome remains unavailable.
         */
        response.setMonthlyIncome(null);
        response.setDebtPaymentRatio(null);
        response.setBurdenLevel("UNKNOWN");
        response.setExplanation(
                "Debt burden requires monthly income data to calculate " +
                        "the debt-payment ratio."
        );

        return response;
    }

    private boolean isActive(Liability liability) {

        return liability.getStatus() == LiabilityStatus.ACTIVE
                || liability.getStatus() == LiabilityStatus.OVERDUE;
    }

    private BigDecimal calculateRecognizedAmount(
            Liability liability
    ) {

        if (liability.getResponsibilityType()
                == ResponsibilityType.FAMILY_UNCLEAR) {

            return BigDecimal.ZERO;
        }

        BigDecimal percentage =
                liability.getResponsibilityPercentage();

        if (percentage == null) {
            percentage = BigDecimal.valueOf(100);
        }

        return liability.getOutstandingAmount()
                .multiply(percentage)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal calculateRecognizedMonthlyPayment(
            Liability liability
    ) {

        BigDecimal monthlyPayment =
                debtCalculationService
                        .calculateMonthlyPayment(liability);

        if (liability.getResponsibilityType()
                == ResponsibilityType.FAMILY_UNCLEAR) {

            return BigDecimal.ZERO;
        }

        BigDecimal percentage =
                liability.getResponsibilityPercentage();

        if (percentage == null) {
            percentage = BigDecimal.valueOf(100);
        }

        return monthlyPayment
                .multiply(percentage)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal sumRecognizedPrincipal(
            List<LiabilityRepayment> repayments,
            Liability liability
    ) {

        return applyResponsibility(
                repayments.stream()
                        .map(LiabilityRepayment::getPrincipalAmount)
                        .filter(value -> value != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                liability
        );
    }

    private BigDecimal sumRecognizedInterest(
            List<LiabilityRepayment> repayments,
            Liability liability
    ) {

        return applyResponsibility(
                repayments.stream()
                        .map(LiabilityRepayment::getInterestAmount)
                        .filter(value -> value != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                liability
        );
    }

    private BigDecimal sumRecognizedPayments(
            List<LiabilityRepayment> repayments,
            Liability liability
    ) {

        return applyResponsibility(
                repayments.stream()
                        .map(LiabilityRepayment::getPaymentAmount)
                        .filter(value -> value != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                liability
        );
    }

    private BigDecimal applyResponsibility(
            BigDecimal amount,
            Liability liability
    ) {

        if (liability.getResponsibilityType()
                == ResponsibilityType.FAMILY_UNCLEAR) {

            return BigDecimal.ZERO;
        }

        BigDecimal percentage =
                liability.getResponsibilityPercentage();

        if (percentage == null) {
            percentage = BigDecimal.valueOf(100);
        }

        return amount
                .multiply(percentage)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal scale(BigDecimal value) {

        return value
                .setScale(2, RoundingMode.HALF_UP);
    }
}