package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.FxInstrumentResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FxMarketServiceTest {

    private final FxMarketService fxMarketService = new FxMarketService();

    @Test
    void shouldReturnSupportedFxPairs() {

        List<FxInstrumentResponse> pairs =
                fxMarketService.getSupportedPairs();

        assertEquals(3, pairs.size());

        assertTrue(
                pairs.stream()
                        .anyMatch(pair -> pair.symbol().equals("USD/INR"))
        );

        assertTrue(
                pairs.stream()
                        .anyMatch(pair -> pair.symbol().equals("EUR/INR"))
        );

        assertTrue(
                pairs.stream()
                        .anyMatch(pair -> pair.symbol().equals("GBP/INR"))
        );
    }
}