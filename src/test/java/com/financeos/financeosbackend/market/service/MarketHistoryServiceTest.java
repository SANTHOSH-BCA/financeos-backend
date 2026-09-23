package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MarketHistoryServiceTest {

    @Test
    void shouldReturnHistoricalObservations() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");

        MarketObservation observation = new MarketObservation();
        observation.setInstrument(instrument);
        observation.setCurrentValue(new BigDecimal("25000"));
        observation.setDailyChange(new BigDecimal("150"));
        observation.setDailyChangePercentage(new BigDecimal("0.60"));
        observation.setPreviousClose(new BigDecimal("24850"));
        observation.setObservedAt(LocalDateTime.now());
        observation.setSource("TEST");
        observation.setDelayed(false);

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(List.of(observation));

        MarketHistoryService service = new MarketHistoryService(
                instrumentRepository,
                observationRepository
        );

        var history = service.getHistory("NIFTY50");

        assertEquals(1, history.size());
        assertEquals("NIFTY50", history.get(0).symbol());
        assertEquals(
                new BigDecimal("25000"),
                history.get(0).currentValue()
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

        MarketHistoryService service = new MarketHistoryService(
                instrumentRepository,
                observationRepository
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getHistory("UNKNOWN")
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoHistoricalObservationsExist() {

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
                        .findByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(List.of());

        MarketHistoryService service = new MarketHistoryService(
                instrumentRepository,
                observationRepository
        );

        var history = service.getHistory("NIFTY50");

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void shouldPreserveRepositoryHistoricalOrder() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");

        MarketObservation latest = new MarketObservation();
        latest.setInstrument(instrument);
        latest.setCurrentValue(new BigDecimal("25000"));
        latest.setObservedAt(
                LocalDateTime.of(2026, 9, 23, 10, 0)
        );
        latest.setSource("TEST");
        latest.setDelayed(false);

        MarketObservation previous = new MarketObservation();
        previous.setInstrument(instrument);
        previous.setCurrentValue(new BigDecimal("24800"));
        previous.setObservedAt(
                LocalDateTime.of(2026, 9, 22, 10, 0)
        );
        previous.setSource("TEST");
        previous.setDelayed(false);

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(List.of(latest, previous));

        MarketHistoryService service = new MarketHistoryService(
                instrumentRepository,
                observationRepository
        );

        var history = service.getHistory("NIFTY50");

        assertEquals(2, history.size());
        assertEquals(
                new BigDecimal("25000"),
                history.get(0).currentValue()
        );
        assertEquals(
                new BigDecimal("24800"),
                history.get(1).currentValue()
        );
    }
}