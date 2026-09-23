package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MarketSnapshotServiceTest {

    @Test
    void shouldReturnEmptySnapshotWhenObservationDoesNotExist() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.empty());

        MarketFreshnessService freshnessService =
                new MarketFreshnessService();

        MarketSnapshotService service = new MarketSnapshotService(
                instrumentRepository,
                observationRepository,
                freshnessService
        );

        var response = service.getSnapshot("NIFTY50");

        assertEquals("NIFTY50", response.symbol());
        assertNull(response.currentValue());
        assertNull(response.observedAt());
        assertNull(response.source());
        assertEquals(
                com.financeos.financeosbackend.market.enums.MarketStatus.UNKNOWN,
                response.status()
        );
    }

    @Test
    void shouldRejectUnknownInstrument() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        Mockito.when(instrumentRepository.findBySymbol("UNKNOWN"))
                .thenReturn(Optional.empty());

        MarketFreshnessService freshnessService =
                new MarketFreshnessService();

        MarketSnapshotService service = new MarketSnapshotService(
                instrumentRepository,
                observationRepository,
                freshnessService
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getSnapshot("UNKNOWN")
        );
    }
}