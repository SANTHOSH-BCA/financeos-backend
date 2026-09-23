package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketMoverResponse;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MarketMoverService {

    private final MarketInstrumentRepository marketInstrumentRepository;
    private final MarketObservationRepository marketObservationRepository;

    public MarketMoverService(
            MarketInstrumentRepository marketInstrumentRepository,
            MarketObservationRepository marketObservationRepository
    ) {
        this.marketInstrumentRepository = marketInstrumentRepository;
        this.marketObservationRepository = marketObservationRepository;
    }

    public List<MarketMoverResponse> getTopGainers() {

        return getLatestSnapshots()
                .stream()
                .filter(snapshot -> snapshot.dailyChangePercentage() != null)
                .sorted(
                        Comparator.comparing(
                                MarketMoverResponse::dailyChangePercentage
                        ).reversed()
                )
                .toList();
    }

    public List<MarketMoverResponse> getTopDecliners() {

        return getLatestSnapshots()
                .stream()
                .filter(snapshot -> snapshot.dailyChangePercentage() != null)
                .sorted(
                        Comparator.comparing(
                                MarketMoverResponse::dailyChangePercentage
                        )
                )
                .toList();
    }

    private List<MarketMoverResponse> getLatestSnapshots() {

        return marketInstrumentRepository.findByActiveTrue()
                .stream()
                .map(this::toMoverResponse)
                .filter(response -> response.currentValue() != null)
                .toList();
    }

    private MarketMoverResponse toMoverResponse(
            MarketInstrument instrument
    ) {

        MarketObservation observation =
                marketObservationRepository
                        .findTopByInstrumentOrderByObservedAtDesc(instrument)
                        .orElse(null);

        if (observation == null) {
            return new MarketMoverResponse(
                    instrument.getSymbol(),
                    instrument.getName(),
                    instrument.getCategory(),
                    instrument.getRegion(),
                    null,
                    null,
                    null,
                    null
            );
        }

        return new MarketMoverResponse(
                instrument.getSymbol(),
                instrument.getName(),
                instrument.getCategory(),
                instrument.getRegion(),
                observation.getCurrentValue(),
                observation.getDailyChange(),
                observation.getDailyChangePercentage(),
                observation.getObservedAt()
        );
    }
}