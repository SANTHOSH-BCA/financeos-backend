package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.InvestmentMarketContextResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.DataFreshness;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

@Service
public class InvestmentMarketContextService {

    private final MarketInstrumentRepository marketInstrumentRepository;
    private final MarketObservationRepository marketObservationRepository;
    private final MarketFreshnessService marketFreshnessService;

    public InvestmentMarketContextService(
            MarketInstrumentRepository marketInstrumentRepository,
            MarketObservationRepository marketObservationRepository,
            MarketFreshnessService marketFreshnessService
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
        this.marketObservationRepository = marketObservationRepository;
        this.marketFreshnessService = marketFreshnessService;
    }

    public InvestmentMarketContextResponse getContext(String symbol) {

        MarketInstrument instrument =
                marketInstrumentRepository
                        .findBySymbol(symbol)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Market instrument not found: " + symbol
                                ));

        MarketObservation observation =
                marketObservationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {
            return new InvestmentMarketContextResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
                    instrument.getCategory(),
                    null,
                    null,
                    null,
                    DataFreshness.UNAVAILABLE,
                    null
            );
        }

        return new InvestmentMarketContextResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCategory(),
                observation.getCurrentValue(),
                observation.getDailyChangePercentage(),
                observation.getObservedAt(),
                marketFreshnessService.evaluate(
                        observation.getObservedAt()
                ),
                observation.getSource()
        );
    }
}