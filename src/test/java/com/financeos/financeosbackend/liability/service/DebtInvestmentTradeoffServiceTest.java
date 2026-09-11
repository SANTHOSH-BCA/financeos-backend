package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.common.service.CurrentUserService;
import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtInvestmentTradeoffResponse;
import com.financeos.financeosbackend.investment.repository.InvestmentRepository;
import com.financeos.financeosbackend.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtInvestmentTradeoffServiceTest {

    @Mock
    private DebtBurdenService debtBurdenService;

    @Mock
    private InvestmentRepository investmentRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private DebtInvestmentTradeoffService
            debtInvestmentTradeoffService;

    @Test
    void getTradeoff_ShouldCalculateDebtInvestmentRatio() {

        User user = new User();
        user.setId(1L);

        DebtBurdenResponse debtBurden =
                new DebtBurdenResponse();

        debtBurden.setTotalOutstanding(
                new BigDecimal("500000.00")
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(debtBurden);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(new BigDecimal("1000000.00"));

        DebtInvestmentTradeoffResponse response =
                debtInvestmentTradeoffService.getTradeoff();

        assertNotNull(response);

        assertEquals(
                0,
                new BigDecimal("500000.00")
                        .compareTo(
                                response.getTotalOutstandingDebt()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("1000000.00")
                        .compareTo(
                                response.getTotalInvestmentValue()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("50.00")
                        .compareTo(
                                response.getDebtToInvestmentRatio()
                        )
        );

        assertEquals(
                "LOW",
                response.getTradeoffLevel()
        );

        assertNotNull(
                response.getExplanation()
        );

        verify(currentUserService)
                .getCurrentUser();

        verify(debtBurdenService)
                .getMyDebtBurden();

        verify(investmentRepository)
                .getTotalCurrentValueByUser(user);
    }

    @Test
    void calculateDebtToInvestmentRatio_ShouldReturnNull_WhenInvestmentIsZero() {

        BigDecimal result =
                debtInvestmentTradeoffService
                        .calculateDebtToInvestmentRatio(
                                new BigDecimal("500000"),
                                BigDecimal.ZERO
                        );

        assertNull(result);
    }

    @Test
    void calculateDebtToInvestmentRatio_ShouldCalculatePercentage() {

        BigDecimal result =
                debtInvestmentTradeoffService
                        .calculateDebtToInvestmentRatio(
                                new BigDecimal("750000"),
                                new BigDecimal("1000000")
                        );

        assertEquals(
                0,
                new BigDecimal("75.00")
                        .compareTo(result)
        );
    }

    @Test
    void determineTradeoffLevel_ShouldReturnLow() {

        assertEquals(
                "LOW",
                debtInvestmentTradeoffService
                        .determineTradeoffLevel(
                                new BigDecimal("50.00")
                        )
        );
    }

    @Test
    void determineTradeoffLevel_ShouldReturnModerate() {

        assertEquals(
                "MODERATE",
                debtInvestmentTradeoffService
                        .determineTradeoffLevel(
                                new BigDecimal("75.00")
                        )
        );
    }

    @Test
    void determineTradeoffLevel_ShouldReturnHigh() {

        assertEquals(
                "HIGH",
                debtInvestmentTradeoffService
                        .determineTradeoffLevel(
                                new BigDecimal("125.00")
                        )
        );
    }

    @Test
    void determineTradeoffLevel_ShouldReturnUnknown_WhenRatioIsNull() {

        assertEquals(
                "UNKNOWN",
                debtInvestmentTradeoffService
                        .determineTradeoffLevel(null)
        );
    }

    @Test
    void getTradeoff_ShouldHandleZeroInvestmentValue() {

        User user = new User();
        user.setId(1L);

        DebtBurdenResponse debtBurden =
                new DebtBurdenResponse();

        debtBurden.setTotalOutstanding(
                new BigDecimal("500000.00")
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(debtBurden);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(BigDecimal.ZERO);

        DebtInvestmentTradeoffResponse response =
                debtInvestmentTradeoffService.getTradeoff();

        assertNotNull(response);

        assertEquals(
                0,
                BigDecimal.ZERO.setScale(2)
                        .compareTo(
                                response.getTotalInvestmentValue()
                        )
        );

        assertNull(
                response.getDebtToInvestmentRatio()
        );

        assertEquals(
                "UNKNOWN",
                response.getTradeoffLevel()
        );
    }

    @Test
    void getTradeoff_ShouldHandleNullInvestmentValue() {

        User user = new User();
        user.setId(1L);

        DebtBurdenResponse debtBurden =
                new DebtBurdenResponse();

        debtBurden.setTotalOutstanding(
                new BigDecimal("500000.00")
        );

        when(currentUserService.getCurrentUser())
                .thenReturn(user);

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(debtBurden);

        when(investmentRepository.getTotalCurrentValueByUser(user))
                .thenReturn(null);

        DebtInvestmentTradeoffResponse response =
                debtInvestmentTradeoffService.getTradeoff();

        assertNotNull(response);

        assertEquals(
                "UNKNOWN",
                response.getTradeoffLevel()
        );

        assertNull(
                response.getDebtToInvestmentRatio()
        );
    }
}