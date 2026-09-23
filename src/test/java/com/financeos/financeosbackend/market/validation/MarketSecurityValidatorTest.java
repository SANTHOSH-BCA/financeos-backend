package com.financeos.financeosbackend.market.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketSecurityValidatorTest {

    private final MarketSecurityValidator validator =
            new MarketSecurityValidator();

    @Test
    void shouldAcceptValidSymbol() {

        assertDoesNotThrow(
                () -> validator.validateSymbol("NIFTY50")
        );
    }

    @Test
    void shouldRejectBlankSymbol() {

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSymbol(" ")
        );
    }

    @Test
    void shouldRejectOverlongSymbol() {

        String symbol = "A".repeat(51);

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSymbol(symbol)
        );
    }
}