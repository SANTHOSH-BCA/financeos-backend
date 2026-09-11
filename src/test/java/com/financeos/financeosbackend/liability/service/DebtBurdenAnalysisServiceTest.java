package com.financeos.financeosbackend.liability.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DebtBurdenAnalysisServiceTest {

    private DebtBurdenAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new DebtBurdenAnalysisService();
    }

    @Test
    void shouldCalculateDebtPaymentRatio() {

        BigDecimal ratio =
                service.calculateDebtPaymentRatio(
                        new BigDecimal("70000"),
                        new BigDecimal("25000")
                );

        assertEquals(
                0,
                new BigDecimal("35.71")
                        .compareTo(ratio)
        );
    }

    @Test
    void shouldReturnLowBurden() {

        assertEquals(
                "LOW",
                service.determineBurdenLevel(
                        new BigDecimal("20.00")
                )
        );
    }

    @Test
    void shouldReturnModerateBurden() {

        assertEquals(
                "MODERATE",
                service.determineBurdenLevel(
                        new BigDecimal("35.00")
                )
        );
    }

    @Test
    void shouldReturnHighBurden() {

        assertEquals(
                "HIGH",
                service.determineBurdenLevel(
                        new BigDecimal("45.00")
                )
        );
    }

    @Test
    void shouldReturnUnknownWhenRatioUnavailable() {

        assertEquals(
                "UNKNOWN",
                service.determineBurdenLevel(null)
        );
    }

    @Test
    void shouldNotCalculateRatioWithoutIncome() {

        assertNull(
                service.calculateDebtPaymentRatio(
                        null,
                        new BigDecimal("25000")
                )
        );
    }

    @Test
    void shouldExplainDebtBurden() {

        String explanation =
                service.buildExplanation(
                        new BigDecimal("70000"),
                        new BigDecimal("25000"),
                        new BigDecimal("35.71")
                );

        assertTrue(
                explanation.contains("25000.00")
        );

        assertTrue(
                explanation.contains("35.71%")
        );

        assertTrue(
                explanation.contains("70000.00")
        );

        assertTrue(
                explanation.contains(
                        "not a universal financial rule"
                )
        );
    }
}