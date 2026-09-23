package com.financeos.financeosbackend.market.repository;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndianMarketRepository
        extends JpaRepository<MarketInstrument, Long> {

    List<MarketInstrument> findByCategoryAndRegion(
            MarketCategory category,
            MarketRegion region
    );
}