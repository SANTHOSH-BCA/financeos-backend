package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.dto.MarketData;
import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import com.financeos.financeosbackend.market.repository.MarketObservationRepository;
import org.springframework.stereotype.Service;

@Service
public class MarketObservationService {

    private final MarketObservationRepository observationRepository;

    public MarketObservationService(
            MarketObservationRepository observationRepository) {

        this.observationRepository = observationRepository;
    }

    public MarketObservation createObservation(
            MarketInstrument instrument,
            MarketData marketData) {

        MarketObservation observation = new MarketObservation();

        observation.setInstrument(instrument);
        observation.setCurrentValue(marketData.currentValue());
        observation.setDailyChange(marketData.dailyChange());
        observation.setDailyChangePercentage(
                marketData.dailyChangePercentage()
        );
        observation.setPreviousClose(marketData.previousClose());
        observation.setObservedAt(marketData.observedAt());
        observation.setSource(marketData.source());
        observation.setDelayed(marketData.delayed());

        return observationRepository.save(observation);
    }
}