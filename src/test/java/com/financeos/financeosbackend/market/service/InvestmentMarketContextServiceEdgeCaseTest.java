package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InvestmentMarketContextServiceEdgeCaseTest {

    @Test
    void shouldRejectUnknownMarketInstrument() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        Mockito.when(instrumentRepository.findBySymbol("UNKNOWN"))
                .thenReturn(Optional.empty());

        InvestmentMarketContextService service =
                new InvestmentMarketContextService(
                        instrumentRepository,
                        observationRepository,
                        new MarketFreshnessService()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getContext("UNKNOWN")
        );
    }
}