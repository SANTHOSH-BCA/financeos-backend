package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketInstrumentService {

    private final MarketInstrumentRepository instrumentRepository;

    public MarketInstrumentService(
            MarketInstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    public MarketInstrument getBySymbol(String symbol) {

        return instrumentRepository.findBySymbol(symbol)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Market instrument not found: " + symbol
                        ));
    }

    public List<MarketInstrument> getActiveInstruments() {

        return instrumentRepository.findByActiveTrue();
    }
}