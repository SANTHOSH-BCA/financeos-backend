package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.LiabilityStatus;
import com.financeos.financeosbackend.liability.enums.PaymentFrequency;
import com.financeos.financeosbackend.liability.enums.ResponsibilityType;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtBurdenServiceTest {

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private LiabilityRepaymentRepository repaymentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private DebtCalculationService debtCalculationService;

    @Mock
    private DebtBurdenAnalysisService debtBurdenAnalysisService;

    private DebtBurdenService service;

    private User user;

    @BeforeEach
    void setUp() {

        service = new DebtBurdenService(
                liabilityRepository,
                repaymentRepository,
                currentUserService,
                debtCalculationService,
                debtBurdenAnalysisService
        );

        user = new User();
    }

    @Test
    void shouldAggregateActiveDebtBurden() {

        Liability liability =
                createLiability(
                        1L,
                        "Home Loan",
                        new BigDecimal("500000"),
                        new BigDecimal("30000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.ACTIVE,
                        ResponsibilityType.INDIVIDUAL,
                        new BigDecimal("100")
                );

        LiabilityRepayment repayment =
                createRepayment(
                        "20000",
                        "10000",
                        "30000"
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("30000.00"));

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment));

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(
                0,
                new BigDecimal("500000.00")
                        .compareTo(response.getTotalOutstanding())
        );

        assertEquals(
                0,
                new BigDecimal("30000.00")
                        .compareTo(response.getTotalMonthlyPayment())
        );

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(response.getTotalPrincipalPaid())
        );

        assertEquals(
                0,
                new BigDecimal("10000.00")
                        .compareTo(response.getTotalInterestPaid())
        );

        assertEquals(
                0,
                new BigDecimal("30000.00")
                        .compareTo(response.getTotalPaid())
        );

        assertEquals(1, response.getActiveLiabilityCount());
        assertEquals("UNKNOWN", response.getBurdenLevel());
    }

    @Test
    void shouldIncludeOverdueLiabilityAsActiveDebt() {

        Liability liability =
                createLiability(
                        1L,
                        "Personal Loan",
                        new BigDecimal("100000"),
                        new BigDecimal("10000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.OVERDUE,
                        ResponsibilityType.INDIVIDUAL,
                        new BigDecimal("100")
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("10000.00"));

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of());

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(1, response.getActiveLiabilityCount());

        assertEquals(
                0,
                new BigDecimal("100000.00")
                        .compareTo(response.getTotalOutstanding())
        );

        assertEquals(
                0,
                new BigDecimal("10000.00")
                        .compareTo(response.getTotalMonthlyPayment())
        );
    }

    @Test
    void shouldExcludePaidAndClosedLiabilities() {

        Liability paid =
                createLiability(
                        1L,
                        "Paid Loan",
                        new BigDecimal("0"),
                        new BigDecimal("10000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.PAID,
                        ResponsibilityType.INDIVIDUAL,
                        new BigDecimal("100")
                );

        Liability closed =
                createLiability(
                        2L,
                        "Closed Loan",
                        new BigDecimal("0"),
                        new BigDecimal("15000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.CLOSED,
                        ResponsibilityType.INDIVIDUAL,
                        new BigDecimal("100")
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(paid, closed));

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(0, response.getActiveLiabilityCount());

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalOutstanding())
        );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalMonthlyPayment())
        );
    }

    @Test
    void shouldApplySharedResponsibilityPercentage() {

        Liability liability =
                createLiability(
                        1L,
                        "Shared Loan",
                        new BigDecimal("400000"),
                        new BigDecimal("20000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.ACTIVE,
                        ResponsibilityType.SHARED,
                        new BigDecimal("50")
                );

        LiabilityRepayment repayment =
                createRepayment(
                        "12000",
                        "8000",
                        "20000"
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("20000.00"));

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment));

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(
                0,
                new BigDecimal("200000.00")
                        .compareTo(response.getTotalOutstanding())
        );

        assertEquals(
                0,
                new BigDecimal("10000.00")
                        .compareTo(response.getTotalMonthlyPayment())
        );

        assertEquals(
                0,
                new BigDecimal("6000.00")
                        .compareTo(response.getTotalPrincipalPaid())
        );

        assertEquals(
                0,
                new BigDecimal("4000.00")
                        .compareTo(response.getTotalInterestPaid())
        );

        assertEquals(
                0,
                new BigDecimal("10000.00")
                        .compareTo(response.getTotalPaid())
        );

        assertEquals(1, response.getActiveLiabilityCount());
    }

    @Test
    void shouldExcludeFamilyUnclearLiabilityFromRecognizedBurden() {

        Liability liability =
                createLiability(
                        1L,
                        "Family Loan",
                        new BigDecimal("300000"),
                        new BigDecimal("15000"),
                        PaymentFrequency.MONTHLY,
                        LiabilityStatus.ACTIVE,
                        ResponsibilityType.FAMILY_UNCLEAR,
                        null
                );

        LiabilityRepayment repayment =
                createRepayment(
                        "10000",
                        "5000",
                        "15000"
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("15000.00"));

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment));

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalOutstanding())
        );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalMonthlyPayment())
        );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalPrincipalPaid())
        );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalInterestPaid())
        );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(response.getTotalPaid())
        );

        assertEquals(1, response.getActiveLiabilityCount());
    }

    @Test
    void shouldNormalizePaymentFrequencyThroughCalculationService() {

        Liability liability =
                createLiability(
                        1L,
                        "Quarterly Loan",
                        new BigDecimal("240000"),
                        new BigDecimal("60000"),
                        PaymentFrequency.QUARTERLY,
                        LiabilityStatus.ACTIVE,
                        ResponsibilityType.INDIVIDUAL,
                        new BigDecimal("100")
                );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findAllByUser(user))
                .thenReturn(List.of(liability));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("20000.00"));

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of());

        DebtBurdenResponse response =
                service.getMyDebtBurden();

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(response.getTotalMonthlyPayment())
        );
    }

    private Liability createLiability(
            Long id,
            String name,
            BigDecimal outstanding,
            BigDecimal payment,
            PaymentFrequency frequency,
            LiabilityStatus status,
            ResponsibilityType responsibilityType,
            BigDecimal responsibilityPercentage
    ) {
        Liability liability = new Liability();

        liability.setId(id);
        liability.setUser(user);
        liability.setLiabilityName(name);
        liability.setOutstandingAmount(outstanding);
        liability.setPaymentAmount(payment);
        liability.setPaymentFrequency(frequency);
        liability.setStatus(status);
        liability.setResponsibilityType(responsibilityType);
        liability.setResponsibilityPercentage(
                responsibilityPercentage
        );

        return liability;
    }

    private LiabilityRepayment createRepayment(
            String principal,
            String interest,
            String payment
    ) {
        LiabilityRepayment repayment =
                new LiabilityRepayment();

        repayment.setPrincipalAmount(
                new BigDecimal(principal)
        );

        repayment.setInterestAmount(
                new BigDecimal(interest)
        );

        repayment.setPaymentAmount(
                new BigDecimal(payment)
        );

        return repayment;
    }
}