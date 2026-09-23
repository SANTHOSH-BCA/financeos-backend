package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.GlobalMarketSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GlobalMarketSnapshotService {

    private final MarketInstrumentService marketInstrumentService;
    private final MarketObservationRepository observationRepository;

    public GlobalMarketSnapshotService(
            MarketInstrumentService marketInstrumentService,
            MarketObservationRepository observationRepository) {

        this.marketInstrumentService = marketInstrumentService;
        this.observationRepository = observationRepository;
    }

    public GlobalMarketSnapshotResponse getSnapshot(String symbol) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(symbol);

        MarketObservation observation =
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {
            return new GlobalMarketSnapshotResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
                    instrument.getCurrency(),
                    instrument.getRegion().name(),
                    null,
                    null,
                    null,
                    null,
                    MarketStatus.UNKNOWN,
                    null,
                    null,
                    false
            );
        }

        return new GlobalMarketSnapshotResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
                instrument.getRegion().name(),
                observation.getCurrentValue(),
                observation.getDailyChange(),
                observation.getDailyChangePercentage(),
                observation.getPreviousClose(),
                MarketStatus.UNKNOWN,
                observation.getObservedAt(),
                observation.getSource(),
                observation.isDelayed()
        );
    }

    public List<GlobalMarketSnapshotResponse> getSnapshots() {

        return Arrays.stream(
                        com.financeos.financeosbackend.market.enums
                                .GlobalMarketIndex.values()
                )
                .map(index -> getSnapshot(index.getSymbol()))
                .toList();
    }
}