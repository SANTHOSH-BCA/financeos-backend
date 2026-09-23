package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketHistoryServiceEdgeCaseTest {

    @Test
    void shouldReturnEmptyHistoryWhenNoObservationsExist() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("SENSEX");

        Mockito.when(instrumentRepository.findBySymbol("SENSEX"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(List.of());

        MarketHistoryService service =
                new MarketHistoryService(
                        instrumentRepository,
                        observationRepository
                );

        var result = service.getHistory("SENSEX");

        assertTrue(result.isEmpty());
    }
}