package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.CryptoSnapshotResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.enums.CryptoAsset;
import com.financeos.financeosbackend.market.enums.MarketStatus;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CryptoSnapshotService {

    private final MarketInstrumentService marketInstrumentService;
    private final MarketObservationRepository observationRepository;

    public CryptoSnapshotService(
            MarketInstrumentService marketInstrumentService,
            MarketObservationRepository observationRepository) {

        this.marketInstrumentService = marketInstrumentService;
        this.observationRepository = observationRepository;
    }

    public CryptoSnapshotResponse getSnapshot(String symbol) {

        MarketInstrument instrument =
                marketInstrumentService.getBySymbol(symbol);

        MarketObservation observation =
                observationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {
            return new CryptoSnapshotResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
                    instrument.getCurrency(),
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

        return new CryptoSnapshotResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCurrency(),
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

    public List<CryptoSnapshotResponse> getSnapshots() {

        return Arrays.stream(CryptoAsset.values())
                .map(asset -> getSnapshot(asset.getSymbol()))
                .toList();
    }
}