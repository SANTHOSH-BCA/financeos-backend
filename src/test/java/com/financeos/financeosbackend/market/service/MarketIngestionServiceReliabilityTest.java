package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.provider.MarketDataProvider;
import com.financeos.financeosbackend.market.validation.MarketDataValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MarketIngestionServiceReliabilityTest {

    @Test
    void shouldPropagateProviderFailure() {

        MarketDataProvider provider = new MarketDataProvider() {

            @Override
            public List<com.financeos.financeosbackend.market.dto.MarketData>
            fetchMarketData(List<String> symbols) {
                throw new RuntimeException("Provider unavailable");
            }

            @Override
            public String getProviderName() {
                return "TEST";
            }
        };

        MarketDataValidator validator =
                new MarketDataValidator();

        MarketInstrumentService instrumentService =
                org.mockito.Mockito.mock(
                        MarketInstrumentService.class
                );

        MarketObservationService observationService =
                org.mockito.Mockito.mock(
                        MarketObservationService.class
                );

        MarketIngestionService service =
                new MarketIngestionService(
                        provider,
                        validator,
                        instrumentService,
                        observationService
                );

        assertThrows(
                RuntimeException.class,
                () -> service.ingest(List.of("NIFTY50"))
        );
    }
}