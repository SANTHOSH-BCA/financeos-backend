package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketMoverEdgeCaseTest {

    @Test
    void shouldIgnoreInstrumentsWithoutObservation() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");
        instrument.setName("NIFTY 50");

        Mockito.when(instrumentRepository.findByActiveTrue())
                .thenReturn(List.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.empty());

        MarketMoverService service =
                new MarketMoverService(
                        instrumentRepository,
                        observationRepository
                );

        var gainers = service.getTopGainers();

        assertTrue(gainers.isEmpty());
    }

    @Test
    void shouldSortMixedMovementsCorrectly() {

        MarketInstrument first = new MarketInstrument();
        first.setSymbol("NIFTY50");
        first.setName("NIFTY 50");

        MarketInstrument second = new MarketInstrument();
        second.setSymbol("SENSEX");
        second.setName("BSE SENSEX");

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketObservation firstObservation =
                new MarketObservation();

        firstObservation.setInstrument(first);
        firstObservation.setCurrentValue(
                new BigDecimal("25000")
        );
        firstObservation.setDailyChangePercentage(
                new BigDecimal("2.00")
        );

        MarketObservation secondObservation =
                new MarketObservation();

        secondObservation.setInstrument(second);
        secondObservation.setCurrentValue(
                new BigDecimal("80000")
        );
        secondObservation.setDailyChangePercentage(
                new BigDecimal("-1.00")
        );

        Mockito.when(instrumentRepository.findByActiveTrue())
                .thenReturn(List.of(first, second));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(first)
        ).thenReturn(Optional.of(firstObservation));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(second)
        ).thenReturn(Optional.of(secondObservation));

        MarketMoverService service =
                new MarketMoverService(
                        instrumentRepository,
                        observationRepository
                );

        var gainers = service.getTopGainers();
        var decliners = service.getTopDecliners();

        assertEquals("NIFTY50", gainers.get(0).symbol());
        assertEquals("SENSEX", decliners.get(0).symbol());
    }
}