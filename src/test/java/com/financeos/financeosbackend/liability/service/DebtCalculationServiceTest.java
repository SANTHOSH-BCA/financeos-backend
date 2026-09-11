package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.entity.LiabilityRepayment;
import com.financeos.financeosbackend.liability.enums.PaymentFrequency;
import com.financeos.financeosbackend.liability.repository.LiabilityRepaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtCalculationServiceTest {

    @Mock
    private LiabilityRepaymentRepository repaymentRepository;

    private DebtCalculationService service;

    private Liability liability;

    @BeforeEach
    void setUp() {
        service = new DebtCalculationService(repaymentRepository);

        liability = new Liability();
        liability.setId(1L);
        liability.setOutstandingAmount(new BigDecimal("500000"));
        liability.setPaymentAmount(new BigDecimal("20000"));
        liability.setPaymentFrequency(PaymentFrequency.MONTHLY);
        liability.setInterestRate(new BigDecimal("10.00"));
        liability.setStartDate(LocalDate.of(2026, 1, 1));
    }

    @Test
    void shouldCalculateTotalPrincipalPaid() {

        LiabilityRepayment repayment1 = repayment(
                "18000", "7000", "25000"
        );

        LiabilityRepayment repayment2 = repayment(
                "18500", "6500", "25000"
        );

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment1, repayment2));

        assertEquals(
                0,
                new BigDecimal("36500.00")
                        .compareTo(
                                service.calculateTotalPrincipalPaid(liability)
                        )
        );
    }

    @Test
    void shouldCalculateTotalInterestPaid() {

        LiabilityRepayment repayment1 = repayment(
                "18000", "7000", "25000"
        );

        LiabilityRepayment repayment2 = repayment(
                "18500", "6500", "25000"
        );

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment1, repayment2));

        assertEquals(
                0,
                new BigDecimal("13500.00")
                        .compareTo(
                                service.calculateTotalInterestPaid(liability)
                        )
        );
    }

    @Test
    void shouldCalculateTotalPaid() {

        LiabilityRepayment repayment1 = repayment(
                "18000", "7000", "25000"
        );

        LiabilityRepayment repayment2 = repayment(
                "18500", "6500", "25000"
        );

        when(repaymentRepository
                .findAllByLiabilityOrderByRepaymentDateDesc(liability))
                .thenReturn(List.of(repayment1, repayment2));

        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(
                                service.calculateTotalPaid(liability)
                        )
        );
    }

    @Test
    void shouldCalculateMonthlyPaymentForMonthlyFrequency() {

        liability.setPaymentAmount(new BigDecimal("20000"));
        liability.setPaymentFrequency(PaymentFrequency.MONTHLY);

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(
                                service.calculateMonthlyPayment(liability)
                        )
        );
    }

    @Test
    void shouldNormalizeQuarterlyPayment() {

        liability.setPaymentAmount(new BigDecimal("60000"));
        liability.setPaymentFrequency(PaymentFrequency.QUARTERLY);

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(
                                service.calculateMonthlyPayment(liability)
                        )
        );
    }

    @Test
    void shouldNormalizeHalfYearlyPayment() {

        liability.setPaymentAmount(new BigDecimal("120000"));
        liability.setPaymentFrequency(PaymentFrequency.HALF_YEARLY);

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(
                                service.calculateMonthlyPayment(liability)
                        )
        );
    }

    @Test
    void shouldNormalizeYearlyPayment() {

        liability.setPaymentAmount(new BigDecimal("240000"));
        liability.setPaymentFrequency(PaymentFrequency.YEARLY);

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(
                                service.calculateMonthlyPayment(liability)
                        )
        );
    }

    @Test
    void shouldReturnZeroMonthlyEquivalentForIrregularPayment() {

        liability.setPaymentAmount(new BigDecimal("50000"));
        liability.setPaymentFrequency(PaymentFrequency.IRREGULAR);

        assertEquals(
                new BigDecimal("0.00"),
                service.calculateMonthlyPayment(liability)
        );
    }

    @Test
    void shouldCalculateRemainingTenureWithoutInterest() {

        liability.setOutstandingAmount(new BigDecimal("100000"));
        liability.setPaymentAmount(new BigDecimal("20000"));
        liability.setPaymentFrequency(PaymentFrequency.MONTHLY);
        liability.setInterestRate(BigDecimal.ZERO);

        assertEquals(
                5,
                service.calculateRemainingTenureMonths(liability)
        );
    }

    @Test
    void shouldReturnZeroTenureWhenFullyPaid() {

        liability.setOutstandingAmount(BigDecimal.ZERO);

        assertEquals(
                0,
                service.calculateRemainingTenureMonths(liability)
        );
    }

    @Test
    void shouldCalculateNextMonthlyPaymentDate() {

        liability.setNextPaymentDate(null);
        liability.setStartDate(LocalDate.of(2026, 1, 1));
        liability.setPaymentFrequency(PaymentFrequency.MONTHLY);

        assertEquals(
                LocalDate.of(2026, 10, 1),
                service.calculateNextPaymentDate(
                        liability,
                        LocalDate.of(2026, 9, 15)
                )
        );
    }

    @Test
    void shouldCalculateNextQuarterlyPaymentDate() {

        liability.setNextPaymentDate(null);
        liability.setStartDate(LocalDate.of(2026, 1, 1));
        liability.setPaymentFrequency(PaymentFrequency.QUARTERLY);

        assertEquals(
                LocalDate.of(2026, 10, 1),
                service.calculateNextPaymentDate(
                        liability,
                        LocalDate.of(2026, 9, 15)
                )
        );
    }

    @Test
    void shouldReturnConfiguredNextPaymentDateWhenAvailable() {

        LocalDate configuredDate =
                LocalDate.of(2026, 9, 25);

        liability.setNextPaymentDate(configuredDate);

        assertEquals(
                configuredDate,
                service.calculateNextPaymentDate(
                        liability,
                        LocalDate.of(2026, 9, 15)
                )
        );
    }

    private LiabilityRepayment repayment(
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