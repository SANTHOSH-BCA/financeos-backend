package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketData;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class MarketObservationServiceTest {

    @Test
    void shouldSaveMarketObservation() {

        MarketObservationRepository repository =
                Mockito.mock(MarketObservationRepository.class);

        MarketObservationService service =
                new MarketObservationService(repository);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");

        MarketData data = new MarketData(
                "NIFTY50",
                "NIFTY 50",
                new BigDecimal("25000"),
                new BigDecimal("150"),
                new BigDecimal("0.60"),
                new BigDecimal("24850"),
                LocalDateTime.now(),
                "TEST",
                false
        );

        service.createObservation(instrument, data);

        verify(repository).save(any());
    }
}