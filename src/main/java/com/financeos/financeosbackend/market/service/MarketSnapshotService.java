package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.DataFreshness;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketSnapshotService {

    private final MarketInstrumentRepository marketInstrumentRepository;
    private final MarketObservationRepository marketObservationRepository;
    private final MarketFreshnessService marketFreshnessService;

    public MarketSnapshotService(
            MarketInstrumentRepository marketInstrumentRepository,
            MarketObservationRepository marketObservationRepository,
            MarketFreshnessService marketFreshnessService
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
        this.marketObservationRepository = marketObservationRepository;
        this.marketFreshnessService = marketFreshnessService;
    }

    public List<MarketSnapshotResponse> getSnapshots() {

        return marketInstrumentRepository.findByActiveTrue()
                .stream()
                .map(this::buildSnapshot)
                .toList();
    }

    public MarketSnapshotResponse getSnapshot(String symbol) {

        MarketInstrument instrument = marketInstrumentRepository
                .findBySymbol(symbol)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Market instrument not found: " + symbol
                        ));

        return buildSnapshot(instrument);
    }

    private MarketSnapshotResponse buildSnapshot(
            MarketInstrument instrument
    ) {

        MarketObservation observation =
                marketObservationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {
            return new MarketSnapshotResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
                    instrument.getCategory(),
                    instrument.getRegion(),
                    instrument.getCurrency(),
                    null,
                    null,
                    null,
                    null,
                    MarketStatus.UNKNOWN,
                    null,
                    null,
                    null,
                    DataFreshness.UNAVAILABLE
            );
        }

        DataFreshness freshness =
                marketFreshnessService.evaluate(
                        observation.getObservedAt()
                );

        return new MarketSnapshotResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCategory(),
                instrument.getRegion(),
                instrument.getCurrency(),
                observation.getCurrentValue(),
                observation.getDailyChange(),
                observation.getDailyChangePercentage(),
                observation.getPreviousClose(),
                MarketStatus.UNKNOWN,
                observation.getObservedAt(),
                observation.getSource(),
                observation.isDelayed(),
                freshness
        );
    }
}