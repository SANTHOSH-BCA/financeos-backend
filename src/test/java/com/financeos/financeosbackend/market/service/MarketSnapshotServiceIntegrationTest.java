package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketSnapshotServiceIntegrationTest {

    @Test
    void shouldReturnSnapshotsOnlyForActiveInstruments() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument active = new MarketInstrument();
        active.setSymbol("NIFTY50");
        active.setName("NIFTY 50");

        Mockito.when(instrumentRepository.findByActiveTrue())
                .thenReturn(List.of(active));

        MarketFreshnessService freshnessService =
                new MarketFreshnessService();

        MarketSnapshotService service = new MarketSnapshotService(
                instrumentRepository,
                observationRepository,
                freshnessService
        );

        var snapshots = service.getSnapshots();

        assertEquals(1, snapshots.size());
        assertEquals("NIFTY50", snapshots.get(0).symbol());
    }
}