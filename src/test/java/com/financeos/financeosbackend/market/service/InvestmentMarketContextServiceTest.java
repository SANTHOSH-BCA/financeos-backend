package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentMarketContextServiceTest {

    @Test
    void shouldReturnUnavailableContextWithoutObservation() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");
        instrument.setName("NIFTY 50");

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.empty());

        InvestmentMarketContextService service =
                new InvestmentMarketContextService(
                        instrumentRepository,
                        observationRepository,
                        new MarketFreshnessService()
                );

        var response = service.getContext("NIFTY50");

        assertEquals("NIFTY50", response.symbol());
        assertNull(response.currentValue());
        assertNull(response.observedAt());
        assertEquals(
                com.financeos.financeosbackend.market.enums.DataFreshness.UNAVAILABLE,
                response.freshness()
        );
    }

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

    @Test
    void shouldReturnLatestMarketObservationAsInvestmentContext() {

        MarketInstrumentRepository instrumentRepository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketObservationRepository observationRepository =
                Mockito.mock(MarketObservationRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");
        instrument.setName("NIFTY 50");

        var observation = new com.financeos.financeosbackend.market.entity.MarketObservation();
        observation.setInstrument(instrument);
        observation.setCurrentValue(new java.math.BigDecimal("25000"));
        observation.setDailyChangePercentage(
                new java.math.BigDecimal("1.25")
        );
        observation.setObservedAt(
                java.time.LocalDateTime.now().minusMinutes(5)
        );
        observation.setSource("TEST");
        observation.setDelayed(false);

        Mockito.when(instrumentRepository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        Mockito.when(
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
        ).thenReturn(Optional.of(observation));

        InvestmentMarketContextService service =
                new InvestmentMarketContextService(
                        instrumentRepository,
                        observationRepository,
                        new MarketFreshnessService()
                );

        var response = service.getContext("NIFTY50");

        assertEquals("NIFTY50", response.symbol());
        assertEquals(
                new java.math.BigDecimal("25000"),
                response.currentValue()
        );
        assertEquals(
                new java.math.BigDecimal("1.25"),
                response.dailyChangePercentage()
        );
        assertEquals("TEST", response.source());

        assertEquals(
                com.financeos.financeosbackend.market.enums.DataFreshness.FRESH,
                response.freshness()
        );
    }
}