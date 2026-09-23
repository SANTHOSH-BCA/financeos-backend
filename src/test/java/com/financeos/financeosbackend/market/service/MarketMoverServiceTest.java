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

class MarketMoverServiceTest {

    @Test
    void shouldSortTopGainersByDailyChangePercentage() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument first = new MarketInstrument();
        first.setSymbol("NIFTY50");
        first.setName("NIFTY 50");

        MarketInstrument second = new MarketInstrument();
        second.setSymbol("SENSEX");
        second.setName("BSE SENSEX");

        MarketObservation firstObservation = new MarketObservation();
        firstObservation.setInstrument(first);
        firstObservation.setCurrentValue(new BigDecimal("25000"));
        firstObservation.setDailyChangePercentage(
                new BigDecimal("1.50")
        );

        MarketObservation secondObservation = new MarketObservation();
        secondObservation.setInstrument(second);
        secondObservation.setCurrentValue(new BigDecimal("80000"));
        secondObservation.setDailyChangePercentage(
                new BigDecimal("2.50")
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

        MarketMoverService service = new MarketMoverService(
                instrumentRepository,
                observationRepository
        );

        var gainers = service.getTopGainers();

        assertEquals(2, gainers.size());
        assertEquals("SENSEX", gainers.get(0).symbol());
        assertEquals("NIFTY50", gainers.get(1).symbol());
    }

    @Test
    void shouldSortTopDeclinersByDailyChangePercentage() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument first = new MarketInstrument();
        first.setSymbol("NIFTY50");
        first.setName("NIFTY 50");

        MarketInstrument second = new MarketInstrument();
        second.setSymbol("SENSEX");
        second.setName("BSE SENSEX");

        MarketObservation firstObservation = new MarketObservation();
        firstObservation.setInstrument(first);
        firstObservation.setCurrentValue(new BigDecimal("25000"));
        firstObservation.setDailyChangePercentage(
                new BigDecimal("-1.50")
        );

        MarketObservation secondObservation = new MarketObservation();
        secondObservation.setInstrument(second);
        secondObservation.setCurrentValue(new BigDecimal("80000"));
        secondObservation.setDailyChangePercentage(
                new BigDecimal("-2.50")
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

        MarketMoverService service = new MarketMoverService(
                instrumentRepository,
                observationRepository
        );

        var decliners = service.getTopDecliners();

        assertEquals(2, decliners.size());
        assertEquals("SENSEX", decliners.get(0).symbol());
        assertEquals("NIFTY50", decliners.get(1).symbol());
    }
}