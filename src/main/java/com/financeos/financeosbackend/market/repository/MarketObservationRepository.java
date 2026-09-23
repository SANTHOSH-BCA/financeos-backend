package com.financeos.financeosbackend.market.repository;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.entity.MarketObservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketObservationRepository
        extends JpaRepository<MarketObservation, Long> {

    List<MarketObservation> findByInstrumentOrderByObservedAtDesc(
            MarketInstrument instrument
    );

    Optional<MarketObservation> findTopByInstrumentOrderByObservedAtDesc(
            MarketInstrument instrument
    );
}