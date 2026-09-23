package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketData;
import com.financeos.financeosbackend.market.dto.MarketIngestionResult;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.provider.MarketDataProvider;
import com.financeos.financeosbackend.market.validation.MarketDataValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketIngestionService {

    private final MarketDataProvider marketDataProvider;
    private final MarketDataValidator marketDataValidator;
    private final MarketInstrumentService marketInstrumentService;
    private final MarketObservationService marketObservationService;

    public MarketIngestionService(
            MarketDataProvider marketDataProvider,
            MarketDataValidator marketDataValidator,
            MarketInstrumentService marketInstrumentService,
            MarketObservationService marketObservationService) {

        this.marketDataProvider = marketDataProvider;
        this.marketDataValidator = marketDataValidator;
        this.marketInstrumentService = marketInstrumentService;
        this.marketObservationService = marketObservationService;
    }

    public MarketIngestionResult ingest(List<String> symbols) {

        if (symbols == null || symbols.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one market symbol is required"
            );
        }

        List<MarketData> marketData =
                marketDataProvider.fetchMarketData(symbols);

        List<String> processedSymbols = new ArrayList<>();

        for (MarketData data : marketData) {

            marketDataValidator.validate(data);

            MarketInstrument instrument =
                    marketInstrumentService.getBySymbol(data.symbol());

            marketObservationService.createObservation(
                    instrument,
                    data
            );

            processedSymbols.add(data.symbol());
        }

        return new MarketIngestionResult(
                symbols.size(),
                processedSymbols.size(),
                processedSymbols
        );
    }
}