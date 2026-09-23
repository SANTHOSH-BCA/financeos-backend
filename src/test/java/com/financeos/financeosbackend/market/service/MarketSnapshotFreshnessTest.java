package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketSnapshotFreshnessTest {

    @Test
    void shouldMarkRecentObservationAsFresh() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");
        instrument.setName("NIFTY 50");

        MarketObservation observation = new MarketObservation();
        observation.setInstrument(instrument);
        observation.setObservedAt(
                LocalDateTime.now().minusMinutes(5)
        );
        observation.setSource("TEST");
        observation.setDelayed(false);

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.of(observation));

        MarketSnapshotService service =
                new MarketSnapshotService(
                        instrumentRepository,
                        observationRepository,
                        new MarketFreshnessService()
                );

        var response = service.getSnapshot("NIFTY50");

        assertEquals(
                com.financeos.financeosbackend.market.enums.DataFreshness.FRESH,
                response.freshness()
        );
    }
}