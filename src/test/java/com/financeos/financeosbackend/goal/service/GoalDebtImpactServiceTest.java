package com.financeos.financeosbackend.goal.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.service.DebtBurdenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalDebtImpactServiceTest {

    @Mock
    private DebtBurdenService debtBurdenService;

    @InjectMocks
    private GoalDebtImpactService goalDebtImpactService;

    @Test
    void getMonthlyDebtPayment_ShouldReturnDebtPayment() {

        DebtBurdenResponse response = new DebtBurdenResponse();
        response.setTotalMonthlyPayment(
                new BigDecimal("20000.00")
        );

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(response);

        BigDecimal result =
                goalDebtImpactService.getMonthlyDebtPayment();

        assertEquals(
                0,
                new BigDecimal("20000.00")
                        .compareTo(result)
        );

        verify(debtBurdenService)
                .getMyDebtBurden();
    }

    @Test
    void getMonthlyDebtPayment_ShouldReturnZero_WhenDebtPaymentIsNull() {

        DebtBurdenResponse response = new DebtBurdenResponse();
        response.setTotalMonthlyPayment(null);

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(response);

        BigDecimal result =
                goalDebtImpactService.getMonthlyDebtPayment();

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(result)
        );
    }

    @Test
    void calculateAvailableAfterDebt_ShouldSubtractDebtPayment() {

        DebtBurdenResponse response = new DebtBurdenResponse();
        response.setTotalMonthlyPayment(
                new BigDecimal("20000.00")
        );

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(response);

        BigDecimal result =
                goalDebtImpactService.calculateAvailableAfterDebt(
                        new BigDecimal("55000.00")
                );

        assertEquals(
                0,
                new BigDecimal("35000.00")
                        .compareTo(result)
        );
    }

    @Test
    void calculateAvailableAfterDebt_ShouldReturnNull_WhenIncomeIsNull() {

        BigDecimal result =
                goalDebtImpactService.calculateAvailableAfterDebt(null);

        assertNull(result);

        verify(
                debtBurdenService,
                never()
        ).getMyDebtBurden();
    }

    @Test
    void calculateContributionGap_ShouldReturnZero_WhenAvailableAmountIsEnough() {

        BigDecimal result =
                goalDebtImpactService.calculateContributionGap(
                        new BigDecimal("15000.00"),
                        new BigDecimal("35000.00")
                );

        assertEquals(
                0,
                BigDecimal.ZERO
                        .setScale(2)
                        .compareTo(result)
        );
    }

    @Test
    void calculateContributionGap_ShouldReturnShortfall_WhenAvailableAmountIsInsufficient() {

        BigDecimal result =
                goalDebtImpactService.calculateContributionGap(
                        new BigDecimal("40000.00"),
                        new BigDecimal("35000.00")
                );

        assertEquals(
                0,
                new BigDecimal("5000.00")
                        .compareTo(result)
        );
    }

    @Test
    void calculateContributionGap_ShouldReturnNull_WhenRequiredContributionIsNull() {

        BigDecimal result =
                goalDebtImpactService.calculateContributionGap(
                        null,
                        new BigDecimal("35000.00")
                );

        assertNull(result);
    }

    @Test
    void calculateContributionGap_ShouldReturnNull_WhenAvailableAmountIsNull() {

        BigDecimal result =
                goalDebtImpactService.calculateContributionGap(
                        new BigDecimal("40000.00"),
                        null
                );

        assertNull(result);
    }

    @Test
    void hasDebtImpact_ShouldReturnTrue_WhenContributionExceedsAvailableAmount() {

        boolean result =
                goalDebtImpactService.hasDebtImpact(
                        new BigDecimal("40000.00"),
                        new BigDecimal("35000.00")
                );

        assertTrue(result);
    }

    @Test
    void hasDebtImpact_ShouldReturnFalse_WhenAvailableAmountIsEnough() {

        boolean result =
                goalDebtImpactService.hasDebtImpact(
                        new BigDecimal("30000.00"),
                        new BigDecimal("35000.00")
                );

        assertFalse(result);
    }

    @Test
    void hasDebtImpact_ShouldReturnFalse_WhenValuesAreNull() {

        assertFalse(
                goalDebtImpactService.hasDebtImpact(
                        null,
                        new BigDecimal("35000.00")
                )
        );

        assertFalse(
                goalDebtImpactService.hasDebtImpact(
                        new BigDecimal("30000.00"),
                        null
                )
        );
    }
}