package com.financeos.financeosbackend.market.service;

import com.financeos.financeosbackend.market.entity.MarketInstrument;
import com.financeos.financeosbackend.market.enums.IndianMarketIndex;
import com.financeos.financeosbackend.market.enums.MarketCategory;
import com.financeos.financeosbackend.market.enums.MarketRegion;
import com.financeos.financeosbackend.market.repository.MarketInstrumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class IndianMarketInitializer implements CommandLineRunner {

    private final MarketInstrumentRepository instrumentRepository;

    public IndianMarketInitializer(
            MarketInstrumentRepository instrumentRepository) {

        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void run(String... args) {

        createIfMissing(IndianMarketIndex.NIFTY_50);
        createIfMissing(IndianMarketIndex.SENSEX);
    }

    private void createIfMissing(IndianMarketIndex index) {

        if (instrumentRepository.findBySymbol(index.getSymbol()).isPresent()) {
            return;
        }

        MarketInstrument instrument = new MarketInstrument();

        instrument.setSymbol(index.getSymbol());
        instrument.setName(index.getDisplayName());
        instrument.setCategory(MarketCategory.INDIAN_INDEX);
        instrument.setRegion(MarketRegion.INDIA);
        instrument.setCurrency(index.getCurrency());
        instrument.setActive(true);

        instrumentRepository.save(instrument);
    }
}