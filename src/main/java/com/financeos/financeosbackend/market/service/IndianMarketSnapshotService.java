package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.IndianMarketSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndianMarketSnapshotService {

    private final MarketInstrumentService marketInstrumentService;
    private final MarketObservationRepository observationRepository;

    public IndianMarketSnapshotService(
            MarketInstrumentService marketInstrumentService,
            MarketObservationRepository observationRepository) {

        this.marketInstrumentService = marketInstrumentService;
        this.observationRepository = observationRepository;
    }

    public IndianMarketSnapshotResponse getSnapshot(String symbol) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(symbol);

        MarketObservation observation =
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {

            return new IndianMarketSnapshotResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
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

        return new IndianMarketSnapshotResponse(
                instrument.getSymbol(),
                instrument.getName(),
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

    public List<IndianMarketSnapshotResponse> getSnapshots() {

        return List.of(
                getSnapshot("NIFTY50"),
                getSnapshot("SENSEX")
        );
    }
}