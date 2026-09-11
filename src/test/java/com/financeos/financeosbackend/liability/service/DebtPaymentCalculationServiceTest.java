package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import com.financeos.financeosbackend.liability.dto.DebtPaymentCalculationResponse;
import com.financeos.financeosbackend.liability.entity.Liability;
import com.financeos.financeosbackend.liability.enums.PaymentFrequency;
import com.financeos.financeosbackend.liability.repository.LiabilityRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPaymentCalculationServiceTest {

    @Mock
    private LiabilityRepository liabilityRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private DebtCalculationService debtCalculationService;

    private DebtPaymentCalculationService service;

    private User user;
    private Liability liability;

    @BeforeEach
    void setUp() {

        service = new DebtPaymentCalculationService(
                liabilityRepository,
                currentUserService,
                debtCalculationService
        );

        user = new User();

        liability = new Liability();
        liability.setId(10L);
        liability.setOutstandingAmount(
                new BigDecimal("500000")
        );
        liability.setPaymentAmount(
                new BigDecimal("25000")
        );
        liability.setPaymentFrequency(
                PaymentFrequency.MONTHLY
        );
    }

    @Test
    void shouldCalculateDebtPaymentDetails() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.of(liability));

        when(debtCalculationService.calculateRemainingAmount(liability))
                .thenReturn(new BigDecimal("500000.00"));

        when(debtCalculationService.calculateMonthlyPayment(liability))
                .thenReturn(new BigDecimal("25000.00"));

        when(debtCalculationService.calculateTotalPrincipalPaid(liability))
                .thenReturn(new BigDecimal("100000.00"));

        when(debtCalculationService.calculateTotalInterestPaid(liability))
                .thenReturn(new BigDecimal("20000.00"));

        when(debtCalculationService.calculateTotalPaid(liability))
                .thenReturn(new BigDecimal("120000.00"));

        when(debtCalculationService.calculateRemainingTenureMonths(liability))
                .thenReturn(24);

        LocalDate nextPayment =
                LocalDate.of(2026, 10, 1);

        when(debtCalculationService.calculateNextPaymentDate(
                org.mockito.ArgumentMatchers.eq(liability),
                org.mockito.ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(nextPayment);

        DebtPaymentCalculationResponse response =
                service.calculate(10L);

        assertEquals(10L, response.getLiabilityId());
        assertEquals(
                new BigDecimal("500000.00"),
                response.getOutstandingAmount()
        );
        assertEquals(
                new BigDecimal("25000"),
                response.getPaymentAmount()
        );
        assertEquals(
                new BigDecimal("25000.00"),
                response.getMonthlyPayment()
        );
        assertEquals(
                new BigDecimal("100000.00"),
                response.getTotalPrincipalPaid()
        );
        assertEquals(
                new BigDecimal("20000.00"),
                response.getTotalInterestPaid()
        );
        assertEquals(
                new BigDecimal("120000.00"),
                response.getTotalPaid()
        );
        assertEquals(24, response.getRemainingTenureMonths());
        assertEquals(nextPayment, response.getNextPaymentDate());
    }

    @Test
    void shouldThrowWhenLiabilityDoesNotBelongToUser() {

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(liabilityRepository.findByIdAndUser(10L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.calculate(10L)
        );
    }
}