package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MarketInstrumentServiceTest {

    @Test
    void shouldReturnInstrumentBySymbol() {

        MarketInstrumentRepository repository =
                Mockito.mock(MarketInstrumentRepository.class);

        MarketInstrument instrument = new MarketInstrument();
        instrument.setSymbol("NIFTY50");

        Mockito.when(repository.findBySymbol("NIFTY50"))
                .thenReturn(Optional.of(instrument));

        MarketInstrumentService service =
                new MarketInstrumentService(repository);

        MarketInstrument result =
                service.getBySymbol("NIFTY50");

        assertEquals("NIFTY50", result.getSymbol());
    }

    @Test
    void shouldThrowWhenInstrumentDoesNotExist() {

        MarketInstrumentRepository repository =
                Mockito.mock(MarketInstrumentRepository.class);

        Mockito.when(repository.findBySymbol("UNKNOWN"))
                .thenReturn(Optional.empty());

        MarketInstrumentService service =
                new MarketInstrumentService(repository);

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getBySymbol("UNKNOWN")
        );
    }
}