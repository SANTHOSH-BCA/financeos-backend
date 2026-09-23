package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.CommodityType;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommodityMarketInitializer implements CommandLineRunner {

    private final MarketInstrumentRepository instrumentRepository;

    public CommodityMarketInitializer(
            MarketInstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void run(String... args) {

        for (CommodityType commodity : CommodityType.values()) {
            createIfMissing(commodity);
        }
    }

    private void createIfMissing(CommodityType commodity) {

        if (instrumentRepository
                .findBySymbol(commodity.getSymbol())
                .isPresent()) {
            return;
        }

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(commodity.getSymbol());
        instrument.setName(commodity.getDisplayName());
        instrument.setCategory(MarketCategory.COMMODITY);
        instrument.setRegion(MarketRegion.GLOBAL);
        instrument.setCurrency(commodity.getCurrency());
        instrument.setActive(true);

        instrumentRepository.save(instrument);
    }
}