package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketHistoryResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketHistoryService {

    private final MarketInstrumentRepository marketInstrumentRepository;
    private final MarketObservationRepository marketObservationRepository;

    public MarketHistoryService(
            MarketInstrumentRepository marketInstrumentRepository,
            MarketObservationRepository marketObservationRepository
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
        this.marketObservationRepository = marketObservationRepository;
    }

    public List<MarketHistoryResponse> getHistory(String symbol) {

        MarketInstrument instrument = marketInstrumentRepository
                .findBySymbol(symbol)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Market instrument not found: " + symbol
                        ));

        return marketObservationRepository
                .findByInstrumentOrderByObservedAtDesc(instrument)
                .stream()
                .map(observation -> toResponse(symbol, observation))
                .toList();
    }

    private MarketHistoryResponse toResponse(
            String symbol,
            MarketObservation observation
    ) {
        return new MarketHistoryResponse(
                symbol,
                observation.getCurrentValue(),
                observation.getDailyChange(),
                observation.getDailyChangePercentage(),
                observation.getPreviousClose(),
                observation.getObservedAt(),
                observation.getSource(),
                observation.isDelayed()
        );
    }
}