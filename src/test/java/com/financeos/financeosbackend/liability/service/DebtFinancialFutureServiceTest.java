package com.financeos.financeosbackend.liability.service;

import com.financeos.financeosbackend.liability.dto.DebtBurdenResponse;
import com.financeos.financeosbackend.liability.dto.DebtFinancialFutureResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtFinancialFutureServiceTest {

    @Mock
    private DebtBurdenService debtBurdenService;

    @InjectMocks
    private DebtFinancialFutureService service;

    @Test
    void getDebtFinancialFuture_ShouldCalculateProjection() {

        DebtBurdenResponse burden = new DebtBurdenResponse();

        burden.setTotalOutstanding(new BigDecimal("120000.00"));
        burden.setTotalMonthlyPayment(new BigDecimal("20000.00"));

        when(debtBurdenService.getMyDebtBurden())
                .thenReturn(burden);

        DebtFinancialFutureResponse response =
                service.getDebtFinancialFuture();

        assertEquals(
                new BigDecimal("120000.00"),
                response.getTotalOutstandingDebt()
        );

        assertEquals(
                new BigDecimal("20000.00"),
                response.getMonthlyDebtPayment()
        );

        assertEquals(
                6,
                response.getEstimatedRemainingMonths()
        );

        assertEquals(
                new BigDecimal("120000.00"),
                response.getEstimatedRemainingPayments()
        );

        assertEquals(
                "PROJECTABLE",
                response.getProjectionStatus()
        );
    }

    @Test
    void calculateRemainingMonths_ShouldRoundUp() {

        Integer result =
                service.calculateRemainingMonths(
                        new BigDecimal("100001.00"),
                        new BigDecimal("20000.00")
                );

        assertEquals(6, result);
    }

    @Test
    void calculateRemainingMonths_ShouldReturnNull_WhenPaymentUnavailable() {

        Integer result =
                service.calculateRemainingMonths(
                        new BigDecimal("100000.00"),
                        BigDecimal.ZERO
                );

        assertNull(result);
    }

    @Test
    void calculateRemainingPayments_ShouldReturnZero_WhenMonthsUnavailable() {

        BigDecimal result =
                service.calculateRemainingPayments(
                        new BigDecimal("20000.00"),
                        null
                );

        assertEquals(
                new BigDecimal("0.00"),
                result
        );
    }

    @Test
    void determineProjectionStatus_ShouldReturnNoDebt() {

        String result =
                service.determineProjectionStatus(
                        BigDecimal.ZERO,
                        new BigDecimal("20000.00")
                );

        assertEquals(
                "NO_OUTSTANDING_DEBT",
                result
        );
    }

    @Test
    void determineProjectionStatus_ShouldReturnInsufficientData() {

        String result =
                service.determineProjectionStatus(
                        new BigDecimal("100000.00"),
                        BigDecimal.ZERO
                );

        assertEquals(
                "INSUFFICIENT_DATA",
                result
        );
    }
}