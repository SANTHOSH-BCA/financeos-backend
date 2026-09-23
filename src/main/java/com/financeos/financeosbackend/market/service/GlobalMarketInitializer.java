package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.GlobalMarketIndex;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GlobalMarketInitializer implements CommandLineRunner {

    private final MarketInstrumentRepository instrumentRepository;

    public GlobalMarketInitializer(
            MarketInstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void run(String... args) {

        for (GlobalMarketIndex index : GlobalMarketIndex.values()) {
            createIfMissing(index);
        }
    }

    private void createIfMissing(GlobalMarketIndex index) {

        if (instrumentRepository
                .findBySymbol(index.getSymbol())
                .isPresent()) {
            return;
        }

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(index.getSymbol());
        instrument.setName(index.getDisplayName());
        instrument.setCategory(MarketCategory.GLOBAL_INDEX);
        instrument.setRegion(index.getRegion());
        instrument.setCurrency(index.getCurrency());
        instrument.setActive(true);

        instrumentRepository.save(instrument);
    }
}