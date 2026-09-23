package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.FxSnapshotResponse;
import com.financeos.financeosbackend.market.entity.FxPair;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FxSnapshotService {

    private final MarketInstrumentRepository marketInstrumentRepository;
    private final MarketObservationRepository marketObservationRepository;

    public FxSnapshotService(
            MarketInstrumentRepository marketInstrumentRepository,
            MarketObservationRepository marketObservationRepository
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
        this.marketObservationRepository = marketObservationRepository;
    }

    public List<FxSnapshotResponse> getSnapshots() {
        return Arrays.stream(FxPair.values())
                .map(pair -> getSnapshot(pair.getSymbol()))
                .toList();
    }

    public FxSnapshotResponse getSnapshot(String symbol) {

        FxPair pair = Arrays.stream(FxPair.values())
                .filter(value -> value.getSymbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported FX pair: " + symbol));

        MarketInstrument instrument = marketInstrumentRepository
                .findBySymbol(pair.getSymbol())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "FX instrument not initialized: " + pair.getSymbol()));

        MarketObservation observation = marketObservationRepository
                .findTopByInstrumentOrderByObservedAtDesc(instrument)
                .orElse(null);

        if (observation == null) {
            return new FxSnapshotResponse(
                    pair.getSymbol(),
                    pair.getBaseCurrency(),
                    pair.getQuoteCurrency(),
                    null,
                    null,
                    null,
                    null,
                    MarketStatus.UNKNOWN,
                    null,
                    null,
                    null
            );
        }

        return new FxSnapshotResponse(
                pair.getSymbol(),
                pair.getBaseCurrency(),
                pair.getQuoteCurrency(),
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
}