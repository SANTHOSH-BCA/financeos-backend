package com.financeos.financeosbackend.market;

import com.financeos.financeosbackend.market.dto.IndianMarketSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import com.financeos.financeosbackend.market.service.IndianMarketSnapshotService;
import com.financeos.financeosbackend.market.service.MarketInstrumentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndianMarketSnapshotServiceTest {

    @Test
    void shouldReturnEmptySnapshotWhenNoObservationExists() {

        MarketInstrumentService instrumentService =
                mock(MarketInstrumentService.class);

        MarketObservationRepository observationRepository =
                mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol("NIFTY50");
        instrument.setName("NIFTY 50");
        instrument.setCurrency("INR");

        when(instrumentService.getBySymbol("NIFTY50"))
                .thenReturn(instrument);

        when(observationRepository
                .findTopByInstrumentOrderByObservedAtDesc(instrument))
                .thenReturn(java.util.Optional.empty());

        IndianMarketSnapshotService service =
                new IndianMarketSnapshotService(
                        instrumentService,
                        observationRepository
                );

        IndianMarketSnapshotResponse result =
                service.getSnapshot("NIFTY50");

        assertEquals("NIFTY50", result.symbol());
        assertEquals("NIFTY 50", result.name());
        assertNull(result.currentValue());
        assertNull(result.observedAt());
    }
}