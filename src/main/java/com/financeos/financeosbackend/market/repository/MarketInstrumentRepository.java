package com.financeos.financeosbackend.market.repository;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketInstrumentRepository
        extends JpaRepository<MarketInstrument, Long> {

    Optional<MarketInstrument> findBySymbol(String symbol);

    List<MarketInstrument> findByActiveTrue();

    List<MarketInstrument> findBySymbolIn(List<String> symbols);
}