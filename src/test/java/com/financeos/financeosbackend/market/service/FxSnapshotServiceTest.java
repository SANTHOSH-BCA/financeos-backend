package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FxSnapshotServiceTest {

    @Test
    void shouldRejectUnsupportedPair() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        FxSnapshotService service =
                new FxSnapshotService(
                        instrumentRepository,
                        observationRepository
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getSnapshot("JPY/INR")
        );
    }

    @Test
    void shouldReturnUnavailableWhenObservationDoesNotExist() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("USD/INR");

        Mockito.when(instrumentRepository.findBySymbol("USD/INR"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.empty());

        FxSnapshotService service =
                new FxSnapshotService(
                        instrumentRepository,
                        observationRepository
                );

        var response = service.getSnapshot("USD/INR");

        assertEquals("USD/INR", response.symbol());
        assertNull(response.currentValue());
        assertEquals(
                com.financeos.financeosbackend.market.enums.MarketStatus.UNKNOWN,
                response.status()
        );
    }
}